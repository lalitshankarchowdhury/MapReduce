package com.MapReduce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

class IgniteClient {
    private static IgniteConfiguration cfg;
    private static TcpDiscoveryMulticastIpFinder ipFinder;
    private static Ignite ignite;
    private static IgniteCache<Integer, String> documentCache;
    private static IgniteCompute compute;

    static {
        cfg = new IgniteConfiguration();
        ipFinder = new TcpDiscoveryMulticastIpFinder();
    }

    /** Initialize client node **/
    static void init() {
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        ignite = Ignition.start(cfg);
        compute = ignite.compute();
    }

    /** Put text in key-value document cache **/
    static void putText(int key, String text) {
        documentCache = ignite.getOrCreateCache("documentCache");
        documentCache.put(key, text);
    }

    /** Get text from key-value document cache **/
    static String getText(int key) {
        return documentCache.get(key);
    }

    /** Run compute task on server nodes **/
    static TreeMap<String, Integer> compute() {
        return compute.execute(WordFreqTask.class, documentCache);
    }

    /** Destroy document cache, and close client node **/
    static void close() {
        documentCache.destroy();
        ignite.close();
    }

    private static class WordFreqTask
            extends ComputeTaskSplitAdapter<IgniteCache<Integer, String>, TreeMap<String, Integer>> {

        /** Implement split method, the `map` part of MapReduce **/
        @Override
        public List<ComputeJob> split(int gridSize, IgniteCache<Integer, String> documentCache) {
            int documentCount = documentCache.size();
            List<ComputeJob> jobList = new ArrayList<>(documentCount);
            for (int i = 0; i < documentCount; i++) {
                String documentText = documentCache.get(i);
                jobList.add(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        HashMap<String, Integer> wordFreqency = new HashMap<>();
                        String[] words = documentText.split("\\s");
                        for (String word : words) {
                            wordFreqency.put(word, wordFreqency.getOrDefault(word, 0) + 1);
                        }
                        return wordFreqency;
                    }
                });
            }
            return jobList;
        }

        /** Implement reduce method **/
        @Override
        public TreeMap<String, Integer> reduce(List<ComputeJobResult> results) {
            TreeMap<String, Integer> combinedWordFreqs = new TreeMap<>();
            for (ComputeJobResult result : results) {
                HashMap<String, Integer> wordFreqs = result.getData();
                for (String word : wordFreqs.keySet()) {
                    combinedWordFreqs.put(word, combinedWordFreqs.getOrDefault(word, 0) + wordFreqs.get(word));
                }
            }
            return combinedWordFreqs;
        }
    }
}