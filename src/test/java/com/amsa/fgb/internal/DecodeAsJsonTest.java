package com.amsa.fgb.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amsa.fgb.internal.DecodeAsJson.Offset;

public class DecodeAsJsonTest {
    
    @Test
    public void testOffsetParse() {
        Offset o = new Offset("+15 20 -22 15");
        assertEquals(15.333, o.latDiff, 0.001);
        assertEquals(-22.25, o.lonDiff, 0.001);
    }

}
