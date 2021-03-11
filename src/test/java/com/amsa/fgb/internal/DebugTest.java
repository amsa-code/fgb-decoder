package com.amsa.fgb.internal;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.UncheckedIOException;

import org.junit.Test;

public class DebugTest {
    
    @Test
    public void test() {
        Debug.lastHexStr = "020C0200007FDFFF128F7700000000";
        Debug.startSearching();
        Debug.found();
        Debug.writeFoundToComplianceKit();
        Debug.stopSearching();
    }
    
    @Test
    public void testWriteToFile() {
        File file = new File("target/temp.json");
        file.delete();
        Debug.writeToFile("[]", file);
        assertTrue(file.exists());
    }
    
    @Test(expected=UncheckedIOException.class)
    public void testWriteToFileFolderDoesNotExist() {
        File file = new File("target/non-existent-folder/temp.json");
        Debug.writeToFile("[]", file);
    }

}
