package com.MapReduce;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

class CSV {
    static void writeOutput(String filePath, TreeMap<String, Integer> wordFrequencies) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("Word", "Frequency");

        for (String word : wordFrequencies.keySet()) {
            printer.printRecord(word, wordFrequencies.get(word));
        }
        printer.flush();
        printer.close();
    }
}
