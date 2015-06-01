package com.anovelmous.app.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public final class NetworkProxyPreference extends StringPreference {
    public NetworkProxyPreference(SharedPreferences preferences, String key) {
        super(preferences, key);
    }

    /** Creates a {@code Proxy} for the current host to pass to a {@code Client}. */
    public @Nullable
    Proxy getProxy() {
        if (!isSet()) return null;

        String[] parts = get().split(":", 2);
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 80;

        return new Proxy(HTTP, InetSocketAddress.createUnresolved(host, port));
    }
}
