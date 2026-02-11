package com.apps;

import java.util.Objects;

/**
 * QuantityMeasurementApp – UC2: Feet and Inches measurement equality
 *
 * This class contains nested value types (Feet, Inches) and simple demos for equality checks.
 */
public class QuantityMeasurementApp {

    // ----- Feet -----
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value + " ft";
        }
    }

    // ----- Inches -----
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value + " in";
        }
    }

    // ----- Demo methods -----
    public static void demonstrateFeetEquality() {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        System.out.println("Input: " + f1 + " and " + f2);
        System.out.println("Output: Equal (" + f1.equals(f2) + ")");
    }

    public static void demonstrateFeetInequality() {
        Feet f1 = new Feet(1.0);
        Feet f3 = new Feet(2.0);
        System.out.println("Input: " + f1 + " and " + f3);
        System.out.println("Output: Not Equal (" + f1.equals(f3) + ")");
    }

    public static void demonstrateInchesEquality() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);
        System.out.println("Input: " + i1 + " and " + i2);
        System.out.println("Output: Equal (" + i1.equals(i2) + ")");
    }

    // Main method to run demos
    public static void main(String[] args) {
        System.out.println("=== UC1: Feet Equality ===");
        demonstrateFeetEquality();
        demonstrateFeetInequality();

        System.out.println("\n=== UC2: Inches Equality ===");
        demonstrateInchesEquality();
    }
}
