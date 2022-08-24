package com.MapReduce;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter PDF data path: ");
        Scanner sc = new Scanner(System.in);
        String dataPath = sc.nextLine();
        List<File> pdfFiles = PDF.getPDFFiles(dataPath);
        IgniteDB.initClientNode();
        for (int i = 0; i < pdfFiles.size(); i++) {
            IgniteDB.putText(i, PDF.getPDFText(pdfFiles.get(i)));
        }
        IgniteDB.closeClientNode();
        sc.close();
    }
}
