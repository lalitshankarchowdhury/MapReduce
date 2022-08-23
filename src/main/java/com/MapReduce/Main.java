package com.MapReduce;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter PDF data path: ");
        Scanner sc = new Scanner(System.in);
        var dataPath = sc.nextLine();
        for (var pdfFile : PDF.getPDFFiles(dataPath)) {
            System.out.println(PDF.getPDFText(pdfFile));
            sc.nextLine();
        }
        sc.close();
    }
}
