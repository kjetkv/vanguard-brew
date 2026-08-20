package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKata;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.code.vanguard.brew.functional.LazyBrewerKata.Machinery;

public class LazyBrewerKata implements BasicKata<String, Machinery> {

    public static class ColdBrewBottle {
        private final String batchId;
        private final int weightInGrams;
        private boolean sealed = false;
        private boolean labeled = false;
        private final List<String> auditLog = new ArrayList<>();

        public ColdBrewBottle(String batchId, int weightInGrams) {
            this.batchId = batchId;
            this.weightInGrams = weightInGrams;
            this.auditLog.add("BREWED");
        }

        public void applySeal() {
            this.sealed = true;
            this.auditLog.add("SEALED");
        }

        public void applyLabel() {
            this.labeled = true;
            this.auditLog.add("LABELED");
        }

        public List<String> getAuditLog() {
            return auditLog;
        }

        @Override
        public String toString() {
            return "ColdBrewBottle{" +
                    "Batch=" + batchId +
                    ", Weight=" + weightInGrams +
                    ", Sealed=" + sealed +
                    ", Labeled=" + labeled +
                    "}";
        }
    }

    public record Machinery(
            Supplier<ColdBrewBottle> brewer,
            Consumer<ColdBrewBottle> sealer,
            Consumer<ColdBrewBottle> labeler,
            Consumer<ColdBrewBottle> packagingLine
    ) {
        @Override
        public @NonNull String toString() {
            return "Machinery{" +
                    "brewer=" + (Objects.isNull(brewer) ? "not installed" : "installed") +
                    ", sealer=" + (Objects.isNull(sealer) ? "not installed" : "installed") +
                    ", labeler=" + (Objects.isNull(labeler) ? "not installed" : "installed") +
                    ", packagingLine=" + (Objects.isNull(packagingLine) ? "not installed" : "installed") +
                    "}";
        }
    }

    @Override
    public Machinery solve(String batchId) {
        return null;
    }
}