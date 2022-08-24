package com.MapReduce;

import java.util.Collections;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

class IgniteDB {
    static IgniteConfiguration cfg;
    static TcpDiscoveryMulticastIpFinder ipFinder;
    static Ignite ignite;
    static IgniteCache<Integer, String> cache;

    /** Initialize client node **/
    static void initClientNode() {
        cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);
        ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        ignite = Ignition.start(cfg);
    }

    /** Put text in key-value cache **/
    static void putText(int key, String text) {
        cache = ignite.getOrCreateCache("documentCache");
        cache.put(key, text);
    }

    /** Close client node **/
    static void closeClientNode() {
        ignite.close();
    }
}