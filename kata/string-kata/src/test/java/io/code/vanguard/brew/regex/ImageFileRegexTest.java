package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - Image File Extractor")
@Tag("String")
public class ImageFileRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text contains a valid image file (jpg, png, gif), captures the entire filename and extension. " +
            "E.g.: 'See avatar.png' returns 'avatar.png'")
    @Order(1)
    void testStandardImageFile() {
        verifyNoArgKata(new ImageFileRegexKata(),
                "profile_pic.jpg",
                regex -> regexGroupsAsString.apply(regex, "I just uploaded profile_pic.jpg to the server."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains multiple different image types, captures all of them using the OR logic. " +
            "E.g.: 'a.jpg and b.png' returns 'a.jpg,b.png'")
    @Order(2)
    void testMultipleImageTypes() {
        verifyNoArgKata(new ImageFileRegexKata(),
                "logo.png,banner.gif,background.jpg",
                regex -> regexGroupsAsString.apply(regex, "Assets needed: logo.png, banner.gif, and background.jpg"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains files with non-image extensions, ignores them completely. " +
            "E.g.: 'data.csv' returns ''")
    @Order(3)
    void testNonImageExtensions() {
        verifyNoArgKata(new ImageFileRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Please review report.pdf and script.js before the meeting."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the image filename is part of a larger file path, isolates just the filename. " +
            "E.g.: 'C:/photos/cat.png' returns 'cat.png'")
    @Order(4)
    void testFilePaths() {
        // Because \w+ doesn't match slashes, it naturally stops at the directory separator!
        verifyNoArgKata(new ImageFileRegexKata(),
                "cat_meme.gif",
                regex -> regexGroupsAsString.apply(regex, "Found a great file at /downloads/memes/cat_meme.gif yesterday."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the extension partially matches but contains extra characters, ignores it. " +
            "E.g.: 'image.jpg_backup' returns ''")
    @Order(5)
    void testPartialExtensionMatch() {
        verifyNoArgKata(new ImageFileRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Restoring database.png_old and image.gif.bak"),
                Objects::equals);
    }
}