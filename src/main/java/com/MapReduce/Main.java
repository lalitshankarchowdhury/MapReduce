package com.MapReduce;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("Enter PDF data path: ");
        Scanner sc = new Scanner(System.in);
        String dataPath = sc.nextLine();
        List<File> pdfFileList = PDF.getPDFFiles(dataPath);
        IgniteClient.initClientNode();
        ParallelPDF reader1 = new ParallelPDF(0, pdfFileList.size() / 2, pdfFileList);
        ParallelPDF reader2 = new ParallelPDF(pdfFileList.size() / 2, pdfFileList.size(), pdfFileList);
        reader1.start();
        reader2.start();
        reader1.join();
        reader2.join();
        for (int i = 0; i < pdfFileList.size(); i++) {
            System.out.println(IgniteClient.getText(i));
        }
        IgniteClient.closeClientNode();
        sc.close();
    }

    private static class ParallelPDF extends Thread {
        private int low, high;
        private List<File> pdfFileList;

        ParallelPDF(int low, int high, List<File> pdfFileList) {
            this.low = low;
            this.high = high;
            this.pdfFileList = pdfFileList;
        }

        /** Method to loaad, parse and upload PDF files to Apache Ignite Cache **/
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