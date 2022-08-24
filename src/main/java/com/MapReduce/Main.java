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
        List<File> pdfFileList = ParallelPDF.getPDFFiles(dataPath);
        IgniteDB.initClientNode();
        List<String> pdfTextList = ParallelPDF.getPDFTexts(pdfFileList);
        for (int i = 0; i < pdfFileList.size(); i++) {
            IgniteDB.putText(i, pdfTextList.get(i));
        }
        for (int i = 0; i < pdfFileList.size(); i++) {
            System.out.println(IgniteDB.getText(i));
        }
        IgniteDB.closeClientNode();
        sc.close();
    }
}
