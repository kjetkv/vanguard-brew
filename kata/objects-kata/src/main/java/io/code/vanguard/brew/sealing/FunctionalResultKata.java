package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKata;
import io.code.vanguard.brew.sealing.FunctionalResultKata.Result;

public class FunctionalResultKata implements BasicKata<Result, String> {

    public interface Result {
    }

    public static class Success implements Result {
        public Success(String t) {

        }
    }

    public static class Failure<T> implements Result {
        public Failure(T t) {

        }
    }

    @Override
    public String solve(Result result) {
        return null;
    }
}