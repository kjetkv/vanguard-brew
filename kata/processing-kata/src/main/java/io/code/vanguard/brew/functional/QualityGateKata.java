package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKata;

import java.util.function.Predicate;

import static io.code.vanguard.brew.functional.QualityGateKata.Bean;
import static io.code.vanguard.brew.functional.QualityGateKata.RuleType;

public class QualityGateKata implements BasicKata<RuleType, Predicate<Bean>> {

    public record Bean(String origin, int roastLevel, boolean isDefective) { }

    public enum RuleType {
        PREMIUM_ORIGIN,
        DARK_ROAST,
        NOT_DEFECTIVE,
        QUALITY_GATE
    }

    @Override
    public Predicate<Bean> solve(RuleType ruleRequest) {
        return null;
    }
}
