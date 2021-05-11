package au.gov.amsa.fgb.internal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.Test;

import au.gov.amsa.fgb.internal.AttributeType;

public class OpenApiFragmentGeneratorTest {

    private static final String QUOTE = "'";

    @Test
    public void updateFgbOpenApiFragment() throws IOException {
        StringBuilder s = new StringBuilder();
        s.append("    Detection:\n");
        s.append("      type: object\n");
        s.append("      properties:\n");
        Arrays.stream(AttributeType.values()) //
                .sorted((a, b) -> a.toString().compareTo(b.toString())) //
                .map(x -> "        " + QUOTE + x + QUOTE + ":\n" + "          type: "
                        + x.jsonType().name().toLowerCase() + "\n") //
                .forEach(x -> s.append(x));
        File file = new File("src/main/resources/fgb-openapi-fragment.yml");
        Files.write(file.toPath(), s.toString().getBytes(StandardCharsets.UTF_8));
    }

}
