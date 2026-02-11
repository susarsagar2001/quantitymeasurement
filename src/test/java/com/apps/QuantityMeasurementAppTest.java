package com.apps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.apps.QuantityMeasurementApp.Feet;
import com.apps.QuantityMeasurementApp.Inches;

// >>> UC3 unified class + enum (nested in QuantityMeasurementApp)
import com.apps.QuantityMeasurementApp.Length;
import com.apps.QuantityMeasurementApp.Length.LengthUnit;

public class QuantityMeasurementAppTest {

    // ---------- FEET (kept from your original) ----------
    @Test
    public void testFeetEquality_SameValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        assertEquals(feet1, feet2);
    }

    @Test
    public void testFeetEquality_DifferentValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(2.0);
        assertNotEquals(feet1, feet2);
    }

    @Test
    public void testFeetEquality_NullComparison() {
        Feet feet1 = new Feet(1.0);
        assertNotEquals(null, feet1);
        assertFalse(feet1.equals(null));
    }

    @Test
    public void testFeetEquality_DifferentClass() {
        Feet feet1 = new Feet(1.0);
        assertFalse(feet1.equals("1.0"));
    }

    // ---------- FEET (additional from second file) ----------
    @Test
    public void testFeetEquality_SameReference() {
        Feet a = new Feet(1.0);
        Feet sameRef = a;
        assertTrue(a.equals(sameRef));
    }

    // ---------- INCHES (added for UC2 completeness) ----------
    @Test
    public void testInchesEquality_SameValue() {
        Inches a = new Inches(1.0);
        Inches b = new Inches(1.0);
        assertEquals(a, b);
    }

    @Test
    public void testInchesEquality_DifferentValue() {
        Inches a = new Inches(1.0);
        Inches b = new Inches(2.0);
        assertNotEquals(a, b);
    }

    @Test
    public void testInchesEquality_NullComparison() {
        Inches a = new Inches(1.0);
        assertNotEquals(null, a);
        assertFalse(a.equals(null));
    }

    @Test
    public void testInchesEquality_DifferentClass() {
        Inches a = new Inches(1.0);
        assertFalse(a.equals("1.0"));
    }

    @Test
    public void testInchesEquality_SameReference() {
        Inches a = new Inches(1.0);
        Inches sameRef = a;
        assertTrue(a.equals(sameRef));
    }

    // ======================================================
    // =====================  UC3  ==========================
    // ========== Generic Length + Enum, DRY design =========
    // ======================================================

    // 1) Same-Unit Equality (Feet)
    @Test
    public void testEquality_FeetToFeet_SameValue() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(1.0, LengthUnit.FEET);
        assertEquals(a, b);
    }

    // 2) Same-Unit Equality (Inches)
    @Test
    public void testEquality_InchToInch_SameValue() {
        Length a = new Length(1.0, LengthUnit.INCHES);
        Length b = new Length(1.0, LengthUnit.INCHES);
        assertEquals(a, b);
    }

    // 3) Cross-Unit Equality (Feet == Inches)
    @Test
    public void testEquality_FeetToInch_EquivalentValue() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        assertEquals(a, b);
    }

    // 4) Cross-Unit Equality (Inches == Feet), reverse symmetry
    @Test
    public void testEquality_InchToFeet_EquivalentValue() {
        Length a = new Length(12.0, LengthUnit.INCHES);
        Length b = new Length(1.0, LengthUnit.FEET);
        assertEquals(a, b);
    }

    // 5) Same-Unit Inequality (Feet)
    @Test
    public void testEquality_FeetToFeet_DifferentValue() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(2.0, LengthUnit.FEET);
        assertNotEquals(a, b);
    }

    // 6) Same-Unit Inequality (Inches)
    @Test
    public void testEquality_InchToInch_DifferentValue() {
        Length a = new Length(1.0, LengthUnit.INCHES);
        Length b = new Length(2.0, LengthUnit.INCHES);
        assertNotEquals(a, b);
    }

    // 7) Cross-Unit Inequality (values not equivalent)
    @Test
    public void testEquality_CrossUnit_Inequality() {
        Length a = new Length(2.0, LengthUnit.FEET);     // 24 inches
        Length b = new Length(23.0, LengthUnit.INCHES);  // 23 inches
        assertNotEquals(a, b);
    }

    // 8) Invalid Unit (null) should throw
    @Test
    public void testEquality_InvalidUnit_NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Length(1.0, null));
    }

    // 9) Same Reference (reflexive)
    @Test
    public void testEquality_SameReference_Length() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length sameRef = a;
        assertTrue(a.equals(sameRef));
    }

    // 10) Null Comparison (equals should return false)
    @Test
    public void testEquality_NullComparison_Length() {
        Length a = new Length(1.0, LengthUnit.FEET);
        assertFalse(a.equals(null));
    }
}