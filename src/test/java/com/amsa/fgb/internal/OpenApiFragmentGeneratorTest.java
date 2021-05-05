package com.amsa.fgb.internal;

import java.util.Arrays;

import org.junit.Test;

import com.amsa.fgb.internal.AttributeType;

public class OpenApiFragmentGeneratorTest {

    private static final String QUOTE = "'";

    @Test
    public void test() {
        StringBuilder s = new StringBuilder();
        s.append("    Detection:\n");
        s.append("      type: object\n");
        s.append("      properties:\n");
        Arrays.stream(AttributeType.values()) //
        .sorted((a, b) -> a.toString().compareTo(b.toString())) //
                .map(x -> "        " + QUOTE + x + QUOTE + ":\n" + "          type: " + x.jsonType().name().toLowerCase() + "\n") //
                .forEach(x -> s.append(x));
        System.out.println(s);
    }

}
