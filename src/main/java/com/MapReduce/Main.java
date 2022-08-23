package com.MapReduce;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static void main(String[] args) throws IOException {
        System.out.print("Enter PDF data path: ");
        Scanner sc = new Scanner(System.in);
        String dataPath = sc.nextLine();
        for (File pdfFile : PDF.getPDFFiles(dataPath)) {
            System.out.println(PDF.getPDFText(pdfFile));
            sc.nextLine();
        }
        sc.close();
    }
}
