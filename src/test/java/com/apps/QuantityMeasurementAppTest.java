package com.apps;

import java.util.Objects;

/**
 * QuantityMeasurementApp
 *
 * UC1  : Feet equality (legacy class)
 * UC2  : Inches equality (legacy class)
 * UC3  : Generic Length + Enum (DRY) with base unit = inches
 * UC4  : Extended Unit Support (YARDS, CENTIMETERS)
 * UC5  : Unit-to-Unit Conversion API (same category: length)
 * UC6  : Addition of two Length units (sum in the unit of the first operand by default)
 *
 * Notes:
 * - Length.LengthUnit conversion factors are defined relative to INCHES (base).
 * - Equality compares values after converting both operands to inches using a small epsilon.
 * - UC5 adds convertTo(...) (instance) and convert(...) (static) to perform conversions.
 * - UC6 adds add(...) for summing lengths of possibly different units.
 *   Arithmetic normalizes both operands to base, adds, converts to result unit, and rounds to 2 decimals.
 */
public class QuantityMeasurementAppTest {

    // ===== UC3/UC4/UC5/UC6: Unified Length with nested enum =====
    public static class Length {

        public enum LengthUnit {
            FEET(12.0),              // 1 ft = 12 in
            INCHES(1.0),             // base unit
            YARDS(36.0),             // 1 yd = 3 ft = 36 in
            CENTIMETERS(1.0 / 2.54); // 1 cm ≈ 0.3937007874 in

            private final double factorToInches;

            LengthUnit(double factorToInches) {
                this.factorToInches = factorToInches;
            }

            /** Convert a value in this unit to base unit (inches). */
            public double toInches(double value) {
                return value * factorToInches;
            }

            /** Convert a value in inches (base) to this unit. */
            public double fromInches(double inches) {
                return inches / factorToInches;
            }
        }

        // Tiny tolerance for floating-point equality
        private static final double EPS = 1e-6;

        private final double value;
        private final LengthUnit unit;

        public Length(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (Double.isNaN(value) || Double.isInfinite(value))
                throw new IllegalArgumentException("Value must be finite");
            this.value = value;
            this.unit = unit;
        }

        public double getValue() { return value; }
        public LengthUnit getUnit() { return unit; }

        // Convert to base (inches)
        private double toBase() { return unit.toInches(value); }

        // ===== UC5: Instance conversion (immutability: return a new Length) =====
        /**
         * Convert this Length to the target unit.
         * Returns a new instance with the numeric value rounded to 2 decimals.
         */
        public Length convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            if (targetUnit == this.unit) return this; // micro-optimization

            double baseInches = this.toBase();
            double converted = targetUnit.fromInches(baseInches);
            double rounded = round(converted, 2);
            return new Length(rounded, targetUnit);
        }

        // ===== UC5: Static utility conversion =====
        /**
         * Convert raw numeric value from one unit to another.
         * Returns only the numeric result (rounded to 2 decimals).
         */
        public static double convert(double value, LengthUnit from, LengthUnit to) {
            if (from == null || to == null) throw new IllegalArgumentException("Units cannot be null");
            if (Double.isNaN(value) || Double.isInfinite(value))
                throw new IllegalArgumentException("Value must be finite");

            double base = from.toInches(value);
            double converted = to.fromInches(base);
            return round(converted, 2);
        }

        // ===== UC6: Addition APIs =====
        /**
         * Adds another {@code Length} to this one.
         * The result is returned in this instance's unit (unit of the first operand).
         * Immutability: returns a new Length.
         */
        public Length add(Length that) {
            if (that == null) throw new IllegalArgumentException("Length to add cannot be null");
            double sumInches = this.toBase() + that.toBase();
            double sumInThisUnit = this.unit.fromInches(sumInches);
            return new Length(round(sumInThisUnit, 2), this.unit);
        }

        /** Static helper for addition; result unit = {@code a.unit}. */
        public static Length add(Length a, Length b) {
            if (a == null || b == null) throw new IllegalArgumentException("Lengths cannot be null");
            return a.add(b);
        }

        /**
         * Static numeric addition with explicit result unit.
         * Sums v1 in u1 and v2 in u2, returns numeric value in resultUnit (rounded to 2 decimals).
         */
        public static double add(double v1, LengthUnit u1, double v2, LengthUnit u2, LengthUnit resultUnit) {
            if (u1 == null || u2 == null || resultUnit == null)
                throw new IllegalArgumentException("Units cannot be null");
            if (Double.isNaN(v1) || Double.isInfinite(v1) || Double.isNaN(v2) || Double.isInfinite(v2))
                throw new IllegalArgumentException("Values must be finite");

            double sumInches = u1.toInches(v1) + u2.toInches(v2);
            double inResult = resultUnit.fromInches(sumInches);
            return round(inResult, 2);
        }

        // Compare with tolerance (instead of exact Double.compare)
        public boolean compare(Length other) {
            if (other == null) return false;
            return Math.abs(this.toBase() - other.toBase()) < EPS;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return compare((Length) o);
        }

        // Keep hashCode consistent with epsilon-based equality
        @Override
        public int hashCode() {
            long quantized = Math.round(toBase() / EPS);
            return Long.hashCode(quantized);
        }

        @Override
        public String toString() {
            switch (unit) {
                case FEET: return round(value, 2) + " ft";
                case INCHES: return round(value, 2) + " in";
                case YARDS: return round(value, 2) + " yd";
                case CENTIMETERS: return round(value, 2) + " cm";
                default: return value + " ?";
            }
        }

        // Rounds to given decimal places (half-up behavior consistent with Math.round)
        private static double round(double v, int places) {
            double p = Math.pow(10, places);
            return Math.round(v * p) / p;
        }
    }

    // ===== UC1 & UC2 legacy classes (kept for backward compatibility) =====
    public static class Feet {
        private final double value;
        public Feet(double value) { this.value = value; }
        public double getValue() { return value; }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
        @Override public int hashCode() { return Objects.hash(value); }
        @Override public String toString() { return value + " ft"; }
    }

    public static class Inches {
        private final double value;
        public Inches(double value) { this.value = value; }
        public double getValue() { return value; }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
        @Override public int hashCode() { return Objects.hash(value); }
        @Override public String toString() { return value + " in"; }
    }

    // ===== Demo methods =====
    // UC1
    public static void demonstrateFeetEquality() {
        Feet f1 = new Feet(1.0); Feet f2 = new Feet(1.0);
        System.out.println("Input: " + f1 + " and " + f2);
        System.out.println("Output: Equal (" + f1.equals(f2) + ")");
    }
    public static void demonstrateFeetInequality() {
        Feet f1 = new Feet(1.0); Feet f3 = new Feet(2.0);
        System.out.println("Input: " + f1 + " and " + f3);
        System.out.println("Output: Not Equal (" + f1.equals(f3) + ")");
    }

    // UC2
    public static void demonstrateInchesEquality() {
        Inches i1 = new Inches(1.0); Inches i2 = new Inches(1.0);
        System.out.println("Input: " + i1 + " and " + i2);
        System.out.println("Output: Equal (" + i1.equals(i2) + ")");
    }

    // UC3
    public static void demonstrateFeetInchesComparison() {
        Length a = new Length(1.0, Length.LengthUnit.FEET);
        Length b = new Length(12.0, Length.LengthUnit.INCHES);
        System.out.println("Input: " + a + " and " + b);
        System.out.println("Output: Cross-Unit Equal (" + a.equals(b) + ")");
    }

    // UC4 demos
    public static boolean demonstrateLengthComparison(double v1, Length.LengthUnit u1,
                                                      double v2, Length.LengthUnit u2) {
        Length a = new Length(v1, u1);
        Length b = new Length(v2, u2);
        boolean result = a.equals(b);
        System.out.println("Input: " + a + " and " + b + " -> Equal? " + result);
        return result;
    }

    // ===== UC5 demos (Method Overloading) =====
    /** Convert a raw value from one unit to another and print the result. */
    public static Length demonstrateLengthConversion(double value,
                                                     Length.LengthUnit from,
                                                     Length.LengthUnit to) {
        double converted = Length.convert(value, from, to);
        Length src = new Length(value, from);
        Length dst = new Length(converted, to);
        System.out.println("Convert: " + src + " -> " + dst);
        return dst;
    }

    /** Convert an existing Length to a target unit and print the result. */
    public static Length demonstrateLengthConversion(Length length,
                                                     Length.LengthUnit to) {
        Length dst = length.convertTo(to);
        System.out.println("Convert: " + length + " -> " + dst);
        return dst;
    }

    // ===== UC6 demos (Addition) =====
    /** Add two Lengths; result unit = unit of first operand. */
    public static Length demonstrateLengthAddition(Length a, Length b) {
        Length sum = a.add(b);
        System.out.println("Add: " + a + " + " + b + " = " + sum + " (in " + a.getUnit() + ")");
        return sum;
    }

    /** Add raw values; result unit = unit of first operand. */
    public static Length demonstrateLengthAddition(double v1, Length.LengthUnit u1,
                                                   double v2, Length.LengthUnit u2) {
        Length a = new Length(v1, u1);
        Length b = new Length(v2, u2);
        return demonstrateLengthAddition(a, b);
    }

    /** Add raw values with explicit result unit (returns numeric). */
    public static double demonstrateLengthAddition(double v1, Length.LengthUnit u1,
                                                   double v2, Length.LengthUnit u2,
                                                   Length.LengthUnit resultUnit) {
        double sum = Length.add(v1, u1, v2, u2, resultUnit);
        System.out.println("Add: " + v1 + " " + u1 + " + " + v2 + " " + u2 + " = " + sum + " " + resultUnit);
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("=== UC1: Feet Equality ===");
        demonstrateFeetEquality();
        demonstrateFeetInequality();

        System.out.println("\n=== UC2: Inches Equality ===");
        demonstrateInchesEquality();

        System.out.println("\n=== UC3: Cross-Unit Equality (Feet ↔ Inches) ===");
        demonstrateFeetInchesComparison();

        System.out.println("\n=== UC4: Extended Units (Yards, Centimeters) ===");
        // Feet ↔ Inches
        demonstrateLengthComparison(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);
        // Yards ↔ Inches
        demonstrateLengthComparison(1.0, Length.LengthUnit.YARDS, 36.0, Length.LengthUnit.INCHES);
        // Feet ↔ Yards
        demonstrateLengthComparison(3.0, Length.LengthUnit.FEET, 1.0, Length.LengthUnit.YARDS);
        // Centimeters ↔ Inches (use precise inches for 100 cm)
        demonstrateLengthComparison(100.0, Length.LengthUnit.CENTIMETERS, 39.37007874, Length.LengthUnit.INCHES);
        // Centimeters ↔ Feet (30.48 cm = 1 ft)
        demonstrateLengthComparison(30.48, Length.LengthUnit.CENTIMETERS, 1.0, Length.LengthUnit.FEET);

        System.out.println("\n=== UC5: Unit-to-Unit Conversion ===");
        demonstrateLengthConversion(3.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);       // 36.00 in
        demonstrateLengthConversion(2.0, Length.LengthUnit.YARDS, Length.LengthUnit.INCHES);      // 72.00 in
        demonstrateLengthConversion(1.0, Length.LengthUnit.YARDS, Length.LengthUnit.CENTIMETERS); // 91.44 cm
        demonstrateLengthConversion(new Length(30.48, Length.LengthUnit.CENTIMETERS),
                Length.LengthUnit.FEET);                                      // 1.00 ft

        System.out.println("\n=== UC6: Addition of Lengths ===");
        // Matches your example list (result in unit of first operand)
        demonstrateLengthAddition(1.0, Length.LengthUnit.FEET, 2.0, Length.LengthUnit.FEET);            // -> 3.00 FEET
        demonstrateLengthAddition(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);         // -> 2.00 FEET
        demonstrateLengthAddition(12.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.FEET);         // -> 24.00 INCHES
        demonstrateLengthAddition(1.0, Length.LengthUnit.YARDS, 3.0, Length.LengthUnit.FEET);           // -> 2.00 YARDS
        demonstrateLengthAddition(36.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.YARDS);        // -> 72.00 INCHES
        demonstrateLengthAddition(2.54, Length.LengthUnit.CENTIMETERS, 1.0, Length.LengthUnit.INCHES);  // -> ~5.08 CM
        demonstrateLengthAddition(5.0, Length.LengthUnit.FEET, 0.0, Length.LengthUnit.INCHES);          // -> 5.00 FEET
        demonstrateLengthAddition(5.0, Length.LengthUnit.FEET, -2.0, Length.LengthUnit.FEET);           // -> 3.00 FEET

        // Example with explicit result unit numeric variant
        demonstrateLengthAddition(1.0, Length.LengthUnit.FEET, 30.48, Length.LengthUnit.CENTIMETERS,
                Length.LengthUnit.INCHES); // 1 ft + 30.48 cm = 24.00 in
    }
}