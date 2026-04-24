package io.code.vanguard.brew;

import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;

/**
 * An interface containing all that is needed for printing.
 */
public interface Printerface {

    int TOTAL_WIDTH = 85;
    int CONTENT_WIDTH = TOTAL_WIDTH - 2;
    int HEADER_WIDTH = 10;

    /**
     * Print things that needs to be the frame for other text, will fill the {@link Printerface#TOTAL_WIDTH}
     *
     * @param theFrameLine The line to print for the frame
     */
    static void printFrame(String theFrameLine) {
        printWrapped("", theFrameLine, 0, TOTAL_WIDTH);
    }

    /**
     * Print text that are to be more like a blob of text, will fill until {@link Printerface#CONTENT_WIDTH}.
     * Text longer than the given width will be wrapped.
     *
     * @param text The text to print
     */
    static void printText(String text) {
        printWrapped("", text, 0, CONTENT_WIDTH);
    }

    /**
     * Print text with a header, will fill until {@link Printerface#CONTENT_WIDTH}.
     * Text longer than the given width will be wrapped, and blanks are filled to match the header.
     *
     * @param header The header for the text
     * @param text   The text to print
     */
    static void printWithHeader(String header,
                                String text) {
        printWrapped(header, text, HEADER_WIDTH, CONTENT_WIDTH);
    }

    static <I> String prettyFormat(I input) {
        switch (input) {
            case null -> {
                return "null";
            }
            case String[] strings -> {
                return Arrays.toString(strings);
            }
            case String s when s.isEmpty() -> {
                return "''";
            }
            case Throwable exception when ofNullable(exception.getMessage()).orElse("").isBlank() -> {
                return exception.getClass().getSimpleName();
            }
            case Throwable exception -> {
                return "%s (%s)".formatted(exception.getClass().getSimpleName(), exception.getMessage());
            }
            default -> {
                return input.toString();
            }
        }
    }

    private static void printWrapped(String header,
                                     String text,
                                     int headerWidth,
                                     int textWidth) {
        final AtomicBoolean isFirstLine = new AtomicBoolean(true);

        String regex = "(?s).{1," + (textWidth - headerWidth) + "}(?!\\S)";

        Pattern.compile(regex)
                .matcher(text)
                .results()
                .map(MatchResult::group)
                .forEachOrdered(doPrint(header, headerWidth, isFirstLine));
    }

    private static @NonNull Consumer<String> doPrint(String header,
                                                     int headerWidth,
                                                     AtomicBoolean isFirstLine) {
        return line -> {
            if (headerWidth == 0) {
                System.out.printf("%n%s", line.trim());
            } else {
                System.out.printf("%n%-" + headerWidth + "s %s",
                        isFirstLine.getAndSet(false) ? header : "",
                        line.trim());
            }
        };
    }
}
