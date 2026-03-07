package io.code.vanguard.brew.constructing;

public class ImmutableFortressKata {

    private final String host = null;
    private final int port = -1;
    private final boolean useHttps = false;
    private final int maxConnections = -1;

    private ImmutableFortressKata(Builder builder) {
    }

    public static class Builder {
        public Builder host(String host) {
            return new Builder();
        }

        public Builder port(int port) {
            return new Builder();
        }

        public Builder useHttps(boolean useHttps) {
            return new Builder();
        }

        public Builder maxConnections(int maxConnections) {
            return new Builder();
        }

        public ImmutableFortressKata build() {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Server[host='%s', port=%d, https=%b, maxConn=%d]",
                host, port, useHttps, maxConnections);
    }
}