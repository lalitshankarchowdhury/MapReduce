package com.MapReduce;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("Enter PDF data path: ");
        Scanner sc = new Scanner(System.in);
        String dataPath = sc.nextLine();
        System.out.print("Enter Output CSV data path: ");
        String outputPath = sc.nextLine();
        List<File> pdfFileList = PDF.getPDFFiles(dataPath);
        IgniteClient.init();
        System.out.println("Parsing PDF files...");
        ParallelPDFRead thread1 = new ParallelPDFRead(0, pdfFileList.size() / 2, pdfFileList);
        ParallelPDFRead thread2 = new ParallelPDFRead(pdfFileList.size() / 2, pdfFileList.size(), pdfFileList);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Counting word frequencies...");
        long startTime = System.currentTimeMillis();
        TreeMap<String, Integer> wordFrequencies = IgniteClient.compute();
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
        System.out.println("Writing output...");
        CSV.writeOutput(outputPath, wordFrequencies);
        IgniteClient.close();
        sc.close();
    }

    private static class ParallelPDFRead extends Thread {
        private int low, high;
        private List<File> pdfFileList;

        ParallelPDFRead(int low, int high, List<File> pdfFileList) {
            this.low = low;
            this.high = high;
            this.pdfFileList = pdfFileList;
        }

        /** Method to load, parse and upload PDF files to Apache Ignite Cache **/
        @Override
        public void run() {
            for (int i = low; i < high; i++) {
                try {
                    IgniteClient.putText(i, PDF.getPDFText(pdfFileList.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}