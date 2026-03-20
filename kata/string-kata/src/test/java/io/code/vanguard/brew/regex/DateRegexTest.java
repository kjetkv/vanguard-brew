package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - The Date Extractor")
@Tag("String")
public class DateRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text contains a standard YYYY-MM-DD date, captures it completely. " +
            "E.g.: 'Today is 2026-02-25' returns '2026-02-25'")
    @Order(1)
    void testStandardDate() {
        verifyNoArgKata(new DateRegexKata(),
                "1997-08-29",
                regex -> regexGroupsAsString.apply(regex, "Skynet became self-aware on 1997-08-29."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains multiple dates, captures all of them in order. " +
            "E.g.: 'From 2020-01-01 to 2021-01-01' returns '2020-01-01,2021-01-01'")
    @Order(2)
    void testMultipleDates() {
        verifyNoArgKata(new DateRegexKata(),
                "3018-12-25,3019-03-25",
                regex -> regexGroupsAsString.apply(regex, "The Fellowship departed on 3018-12-25 and the Ring was destroyed on 3019-03-25."),
                Objects::equals);
    }

    @Test
    @DisplayName("When dates use slashes or dots instead of dashes, ignores them completely. " +
            "E.g.: 'Created 2024/01/01' returns ''")
    @Order(3)
    void testWrongSeparators() {
        verifyNoArgKata(new DateRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Update 1999/12/31 and 1999.12.31 failed."),
                Objects::equals);
    }

    @Test
    @DisplayName("When a date is missing the day or month, ignores the partial match. " +
            "E.g.: 'Year 2024-05' returns ''")
    @Order(4)
    void testIncompleteDates() {
        verifyNoArgKata(new DateRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "The Mayan calendar ended in 2012-12 roughly."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains dates formatted as DD-MM-YYYY, ignores them safely. " +
            "E.g.: 'Event on 25-02-2026' returns ''")
    @Order(5)
    void testReversedFormat() {
        verifyNoArgKata(new DateRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Remember the 05-11-1605 gunpowder treason."),
                Objects::equals);
    }
}
