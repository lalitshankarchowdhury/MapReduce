package com.MapReduce;

import java.io.File;
import java.io.IOException;
import java.util.List;

class PDFBenchmark {
    /** Benchmark single-threaded PDF read **/
    static void benchSingle(int n, List<File> pdfFileList) throws IOException {
        for (int i = 0; i < n; i++) {
            long startTime = System.currentTimeMillis();
            PDF.getPDFTexts(pdfFileList);
            System.out.println((System.currentTimeMillis() - startTime) / 1000.0 + "s");
        }
    }

    /** Benchmark multi-threaded PDF read **/
    static void benchMulti(int n, List<File> pdfFileList) throws IOException, InterruptedException {
        for (int i = 0; i < n; i++) {
            long startTime = System.currentTimeMillis();
            ParallelPDF.getPDFTexts(pdfFileList);
            System.out.println((System.currentTimeMillis() - startTime) / 1000.0 + "s");
        }
    }
}