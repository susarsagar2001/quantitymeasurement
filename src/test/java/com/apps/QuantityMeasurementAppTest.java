package com.apps;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.apps.QuantityMeasurementApp.Feet;
import com.apps.QuantityMeasurementApp.Inches;

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
}