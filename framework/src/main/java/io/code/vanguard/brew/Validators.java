package io.code.vanguard.brew;

import java.util.function.BiPredicate;


/**
 * This interface has implemented validators
 * that can be used for validating data based on what is returned from the kata.
 */

public interface Validators {
    /**
     * Validate that the actual exception is the same as the expected.
     */
    BiPredicate<Throwable, Throwable> verifySameExceptionClass =
            (expected, actual) ->
                    expected != null && expected.getClass()
                                                .isInstance(actual);
}
