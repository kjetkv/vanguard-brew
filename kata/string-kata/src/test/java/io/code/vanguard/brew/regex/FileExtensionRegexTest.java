package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - File Extension Extractor")
@Tag("String")
public class FileExtensionRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the string ends with a standard 3-character extension, captures it. " +
            "E.g.: 'document.txt' returns '.txt'")
    @Order(1)
    void testStandardExtensionAtEnd() {
        verifyNoArgKata(new FileExtensionRegexKata(),
                ".pdf",
                regex -> regexGroupsAsString.apply(regex, "/top_secret/ufo_report.pdf"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the extension is in the middle of a sentence, ignores it because it is not at the end. " +
            "E.g.: 'Run app.exe now' returns ''")
    @Order(2)
    void testExtensionInMiddle() {
        verifyNoArgKata(new FileExtensionRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Execute order_66.bat immediately."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the file extension is longer than 3 characters, ignores it. " +
            "E.g.: 'photo.jpeg' returns ''")
    @Order(3)
    void testExtensionTooLong() {
        verifyNoArgKata(new FileExtensionRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "vacation_photos.jpeg"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the file extension is shorter than 3 characters, ignores it. " +
            "E.g.: 'script.js' returns ''")
    @Order(4)
    void testExtensionTooShort() {
        verifyNoArgKata(new FileExtensionRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "virus_payload.sh"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the filename has multiple dots, captures only the final valid 3-character extension. " +
            "E.g.: 'archive.tar.zip' returns '.zip'")
    @Order(5)
    void testMultipleDots() {
        verifyNoArgKata(new FileExtensionRegexKata(),
                ".zip",
                regex -> regexGroupsAsString.apply(regex, "jurassic_park_source_code.tar.zip"),
                Objects::equals);
    }
}