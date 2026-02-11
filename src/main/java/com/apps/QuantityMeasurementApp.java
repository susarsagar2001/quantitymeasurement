package com.apps;

import java.util.Objects;

public class QuantityMeasurementApp {

    // ===== UC3 unified Length + nested enum =====
    public static class Length {

        public enum LengthUnit {
            FEET(12.0), INCHES(1.0);
            private final double factor;
            LengthUnit(double factor) { this.factor = factor; }
            public double toInches(double value) { return value * factor; }
        }

        private final double value;
        private final LengthUnit unit;

        public Length(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            this.value = value;
            this.unit = unit;
        }

        private double toBase() { return unit.toInches(value); }

        public boolean compare(Length other) {
            if (other == null) return false;
            return Double.compare(this.toBase(), other.toBase()) == 0;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return compare((Length) o);
        }

        @Override public int hashCode() { return Objects.hash(toBase()); }

        @Override public String toString() {
            return value + " " + (unit == LengthUnit.FEET ? "ft" : "in");
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
    public static void demonstrateInchesEquality() {
        Inches i1 = new Inches(1.0); Inches i2 = new Inches(1.0);
        System.out.println("Input: " + i1 + " and " + i2);
        System.out.println("Output: Equal (" + i1.equals(i2) + ")");
    }
    public static void demonstrateFeetInchesComparison() {
        Length a = new Length(1.0, Length.LengthUnit.FEET);
        Length b = new Length(12.0, Length.LengthUnit.INCHES);
        System.out.println("Input: " + a + " and " + b);
        System.out.println("Output: Cross-Unit Equal (" + a.equals(b) + ")");
    }

    public static void main(String[] args) {
        System.out.println("=== UC1: Feet Equality ===");
        demonstrateFeetEquality();
        demonstrateFeetInequality();

        System.out.println("\n=== UC2: Inches Equality ===");
        demonstrateInchesEquality();

        System.out.println("\n=== UC3: Cross-Unit Equality (DRY) ===");
        demonstrateFeetInchesComparison();
    }
}
