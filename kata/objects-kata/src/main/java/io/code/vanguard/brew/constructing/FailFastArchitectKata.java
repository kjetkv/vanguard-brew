package io.code.vanguard.brew.constructing;

public class FailFastArchitectKata {
    private final Integer systemId;
    private final String role;

    public FailFastArchitectKata(Integer systemId,
                                 String role) {
        this.systemId = systemId;
        this.role = role;
    }

    public String getStatus() {
        return "System " + systemId + " initialized as " + role;
    }

    public static final class SmartSystemInitializer extends FailFastArchitectKata {

        public SmartSystemInitializer(String rawPayload) {
            super(null, null);
        }
    }
}
