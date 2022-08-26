package com.MapReduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

class ParallelPDF {
    /** Filter and create a list of PDF files in user supplied directory **/
    static List<File> getPDFFiles(String dataPath) throws FileNotFoundException {
        File dataDir = new File(dataPath);
        File[] dirItems = dataDir.listFiles();
        ArrayList<File> pdfFileList = new ArrayList<File>();
        if (dirItems != null) {
            for (File dirItem : dirItems) {
                if (dirItem.isFile() && dirItem.getName().toLowerCase().endsWith(".pdf")) {
                    pdfFileList.add(dirItem);
                }
            }
        } else {
            throw new FileNotFoundException("Invalid directory path");
        }
        return pdfFileList;
    }

    /** Return a list of parsed PDF text **/
    static List<String> getPDFTexts(List<File> pdfFileList) throws IOException, InterruptedException {
        List<String> pdfTextList = new ArrayList<String>(Arrays.asList(new String[10]));
        ParallelPDFRead reader1 = new ParallelPDFRead(0, pdfFileList.size() / 2, pdfFileList, pdfTextList);
        ParallelPDFRead reader2 = new ParallelPDFRead(pdfFileList.size() / 2, pdfFileList.size(), pdfFileList,
                pdfTextList);
        reader1.start();
        reader2.start();
        reader1.join();
        reader2.join();
        return pdfTextList;
    }

    private static class ParallelPDFRead extends Thread {
        int low, high;
        List<File> pdfFileList;
        List<String> pdfTextList;

        ParallelPDFRead(int low, int high, List<File> pdfFileList, List<String> pdfTextList) {
            this.low = low;
            this.high = high;
            this.pdfFileList = pdfFileList;
            this.pdfTextList = pdfTextList;
        }

        /** Thread to load and parse PDF files **/
        @Override
        public void run() {
            for (int i = low; i < high; i++) {
                try {
                    PDDocument pdfDocument = PDDocument.load(pdfFileList.get(i));
                    pdfTextList.set(i, new PDFTextStripper().getText(pdfDocument));
                    pdfDocument.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}