package me.xneox.epicguard.core.proxy;

import javax.annotation.Nonnull;

/**
 * Represents a service used for proxy detection.
 */
public class ProxyService {
    private final String url;
    private final String responseRegex;

    public ProxyService(String url, String responseRegex) {
        this.url = url;
        this.responseRegex = responseRegex;
    }

    /**
     * @return URL of this service.
     */
    @Nonnull
    public String url() {
        return this.url;
    }

    /**
     * @return Configured regex expression used for deciding whenever the detection is positive or not.
     */
    @Nonnull
    public String responseContains() {
        return this.responseRegex;
    }
}
