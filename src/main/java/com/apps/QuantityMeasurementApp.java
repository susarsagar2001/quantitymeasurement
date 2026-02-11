package com.apps;



import java.util.Objects;

public class QuantityMeasurementApp {

    // Inner class to represent Feet measurement
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        /**
         * Override equals() method to compare two Feet objects based on their value
         *
         * Important Checks:
         * 1. Reference Check: If both references point to the same object, return true
         * 2. Null Check: If the compared object is null, return false
         * 3. Type Check: If the compared object is not of type Feet, return false
         * 4. Value Comparison: Use Double.compare() for safe double comparison
         */
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
    }

    // Main method to demonstrate Feet equality check
    public static void main(String[] args) {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        Feet f3 = new Feet(2.0);

        System.out.println("1.0 ft == 1.0 ft ? " + f1.equals(f2)); // true
        System.out.println("1.0 ft == 2.0 ft ? " + f1.equals(f3)); // false
    }
}