package com.MapReduce;

import java.io.File;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

class PDF {
    static Scanner sc = new Scanner(System.in);

    /* Filter and create a list of PDF files in user supplied directory */
    static List<File> getPDFFiles() {
        System.out.print("Enter data directory path: ");
        var dataPath = sc.nextLine();
        var dataDir = new File(dataPath);
        var dirItems = dataDir.listFiles();
        var files = new ArrayList<File>();
        if (dirItems != null) {
            for (var dirItem : dirItems) {
                if (dirItem.isFile() && dirItem.getName().toLowerCase().endsWith(".pdf")) {
                    files.add(dirItem);
                }
            }
        }
        return files;
    }

    /* Return parsed PDF text */
    static String getPDFText(File pdfFile) throws IOException {
        var pdfDocument = PDDocument.load(pdfFile);
        var pdfText = new PDFTextStripper().getText(pdfDocument);
        pdfDocument.close();
        return pdfText;
    }

    public static void main(String[] args) throws IOException {
        for (var pdfFile : getPDFFiles()) {
            System.out.println(getPDFText(pdfFile));
            sc.nextLine();
        }
    }
}