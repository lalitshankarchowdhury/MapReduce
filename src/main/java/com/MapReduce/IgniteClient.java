package com.MapReduce;

import java.util.Collections;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

class IgniteClient {
    private static IgniteConfiguration cfg;
    private static TcpDiscoveryMulticastIpFinder ipFinder;
    private static Ignite ignite;
    private static IgniteCache<Integer, String> cache;

    static {
        cfg = new IgniteConfiguration();
        ipFinder = new TcpDiscoveryMulticastIpFinder();
    }

    /** Initialize client node **/
    static void initClientNode() {
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        ignite = Ignition.start(cfg);
    }

    /** Put text in key-value cache **/
    static void putText(int key, String text) {
        cache = ignite.getOrCreateCache("documentCache");
        cache.put(key, text);
    }

    /** Get text from key-value cache **/
    static String getText(int key) {
        return cache.get(key);
    }

    /** Close client node **/
    static void closeClientNode() {
        ignite.close();
    }
}