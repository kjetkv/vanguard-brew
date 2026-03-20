package io.code.vanguard.brew;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.code.vanguard.brew.Printerface.TOTAL_WIDTH;
import static io.code.vanguard.brew.Printerface.prettyFormat;
import static io.code.vanguard.brew.Printerface.printFrame;
import static io.code.vanguard.brew.Printerface.printText;
import static io.code.vanguard.brew.Printerface.printWithHeader;
import static io.code.vanguard.brew.Validators.verifySameExceptionClass;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicKataTestBase {

    @BeforeAll
    static void initClass(final TestInfo testInfo) {
        printFrame("╔" + "═".repeat(TOTAL_WIDTH - 2) + "╗");
        printText("KATA SUITE: " + testInfo.getDisplayName());
        printFrame("╚" + "═".repeat(TOTAL_WIDTH - 2) + "╝");
    }

    @BeforeEach
    void init(final TestInfo testInfo) {
        printFrame("=".repeat(TOTAL_WIDTH));
        printText(testInfo.getDisplayName());
        printFrame("-".repeat(TOTAL_WIDTH));

    }

    @AfterEach
    void tearDown() {
        printFrame("-".repeat(TOTAL_WIDTH));
        System.out.println();
    }

    /**
     * {@link BasicKata} verify method.
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param kata     The implementation logic
     * @param input    The input
     * @param expected The expected output
     * @param verifier The verifier used for comparing
     * @param <I>      Input type
     * @param <R>      Result type
     */
    protected <I, R> void verifyBasicKata(BasicKata<I, R> kata,
                                          I input,
                                          R expected,
                                          BiPredicate<R, R> verifier) {
        printWithHeader("Input", prettyFormat(input));
        printWithHeader("Expecting", prettyFormat(expected));

        R actual = kata.solve(input);

        doVerify(expected, verifier, actual);
    }

    /**
     * {@link BasicNoArgKata} verify method.
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param kata      The implementation logic
     * @param expected  The expected output
     * @param extractor The extractor to calculate the result based on the kata
     * @param verifier  The verifier used for comparing
     * @param <A>       Output type of the kata
     * @param <R>       Result type from the extractor
     */
    protected <R, A> void verifyNoArgKata(BasicNoArgKata<A> kata,
                                          R expected,
                                          Function<A, R> extractor,
                                          BiPredicate<R, R> verifier) {
        printWithHeader("Expecting", prettyFormat(expected));

        A actual = kata.solve();

        printWithHeader("Returned", prettyFormat(actual));

        R result = Optional.ofNullable(actual)
                .map(extractor)
                .orElse(null);

        doVerify(expected, verifier, result);
    }

    /**
     * Method for verifying the implemented {@link K}-class for this kata, where there are no actions.
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param kata           The implemented class
     * @param extractor      The actual result using the extractor to calculate the result
     * @param expectedResult The expected result from the extractor
     * @param verifier       The verifier used for comparing
     * @param <K>            The Kata-class
     * @param <R>            Result type from the extractor
     */
    protected <K, R> void verifyClass(K kata,
                                      Function<K, R> extractor,
                                      R expectedResult,
                                      BiPredicate<R, R> verifier) {

        verifyClass(kata, k -> { }, extractor, expectedResult, verifier);
    }

    /**
     * Method for verifying the implemented {@link K}-class for this kata.
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param kata           The implemented class
     * @param action         The actions to perform on the kata that is implemented
     * @param extractor      The actual result using the extractor to calculate the result
     * @param expectedResult The expected result from the extractor
     * @param verifier       The verifier used for comparing
     * @param <K>            The Kata-class
     * @param <R>            Result type from the extractor
     */
    protected <K, R> void verifyClass(K kata,
                                      Consumer<K> action,
                                      Function<K, R> extractor,
                                      R expectedResult,
                                      BiPredicate<R, R> verifier) {

        printWithHeader("Expecting", prettyFormat(expectedResult));

        action.accept(kata);

        R actualResult = extractor.apply(kata);

        doVerify(expectedResult, verifier, actualResult);
    }

    /**
     * {@link Throwable} verification method, always verifies same exception class
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param executableAction  The action to be called to trigger an exception
     * @param expectedException The expected exception to be thrown
     */

    protected <A extends Throwable, E extends Throwable> void verifyException(Runnable executableAction,
                                                                              E expectedException) {
        verifyException(executableAction, expectedException, verifySameExceptionClass);
    }

    /**
     * {@link Throwable} verification method
     * <p/>
     * A method to handle logging and assertions.
     * Use this instead of direct Assertions.assertEquals in your tests.
     *
     * @param executableAction  The action to be called to trigger an exception
     * @param expectedException The expected exception to be thrown
     * @param verifier          The verifier used for comparing
     * @param <A>               Type of actual excepting
     * @param <E>               Type of the expected exception
     */

    protected <A extends Throwable, E extends Throwable> void verifyException(Runnable executableAction,
                                                                              E expectedException,
                                                                              BiPredicate<E, A> verifier) {

        printWithHeader("Expecting", prettyFormat(expectedException));

        final A actualException = doExecute(executableAction);

        doVerify(expectedException, verifier, actualException);
    }

    private <A extends Throwable> A doExecute(Runnable executableAction) {
        try {
            executableAction.run();
            return null;
        } catch (Throwable t) {
            //noinspection unchecked
            return (A) t;
        }
    }


    private static <R, A> void doVerify(R expected,
                                        BiPredicate<R, A> verifier,
                                        A actual) {
        String prettyActual = prettyFormat(actual);
        printWithHeader("Actual", prettyActual);
        System.out.println();

        if (!verifier.test(expected, actual)) {
            String failureMessage =
                    "❌ Kata failed! Expected " + prettyFormat(expected) + " " + "but got " + prettyActual;
            printText(failureMessage);

            Assertions.fail(failureMessage);
        }

        printText("✅ Kata Passed!");
    }
}
