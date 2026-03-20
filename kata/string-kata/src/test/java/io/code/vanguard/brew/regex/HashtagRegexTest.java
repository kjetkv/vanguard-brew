package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - Hashtag Extractor")
@Tag("String")
public class HashtagRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text contains a single standard hashtag, captures the entire hashtag. " +
            "E.g.: 'Loving this #kata' returns '#kata'")
    @Order(1)
    void testSingleHashtag() {
        verifyNoArgKata(new HashtagRegexKata(),
                "#success",
                regex -> regexGroupsAsString.apply(regex, "Just deployed to production! #success"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains multiple hashtags, captures all of them in order. " +
            "E.g.: '#coding is #fun' returns '#coding,#fun'")
    @Order(2)
    void testMultipleHashtags() {
        verifyNoArgKata(new HashtagRegexKata(),
                "#java,#regex",
                regex -> regexGroupsAsString.apply(regex, "Learning #java and #regex today."),
                Objects::equals);
    }

    @Test
    @DisplayName("When hashtags contain numbers and underscores, captures the entire block safely. " +
            "E.g.: '#top_10' returns '#top_10'")
    @Order(3)
    void testNumbersAndUnderscores() {
        verifyNoArgKata(new HashtagRegexKata(),
                "#force_be_with_you,#2024",
                regex -> regexGroupsAsString.apply(regex, "May the #force_be_with_you in #2024!"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the hash symbol is standing alone or followed by a space, ignores it completely. " +
            "E.g.: 'We are # 1' returns ''")
    @Order(4)
    void testInvalidHashtags() {
        verifyNoArgKata(new HashtagRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "We are # 1 in the # world"),
                Objects::equals);
    }

    @Test
    @DisplayName("When hashtags are glued to punctuation, isolates the hashtag and ignores the punctuation. " +
            "E.g.: 'Look (#cool)!' returns '#cool'")
    @Order(5)
    void testHashtagsWithPunctuation() {
        verifyNoArgKata(new HashtagRegexKata(),
                "#mindblown,#day",
                regex -> regexGroupsAsString.apply(regex, "Wow (#mindblown)! What a #day."),
                Objects::equals);
    }
}