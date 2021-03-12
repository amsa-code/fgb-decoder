package com.amsa.fgb.internal;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

public class CommonTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(Common.class);
    }

}
