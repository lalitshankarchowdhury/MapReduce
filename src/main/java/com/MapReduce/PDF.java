package com.MapReduce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

class PDF {
    /** Filter and create a list of PDF files in user supplied directory **/
    static List<File> getPDFFiles(String dataPath) throws FileNotFoundException {
        File dataDir = new File(dataPath);
        File[] dirItems = dataDir.listFiles();
        ArrayList<File> pdfFiles = new ArrayList<File>();
        if (dirItems != null) {
            for (File dirItem : dirItems) {
                if (dirItem.isFile() && dirItem.getName().toLowerCase().endsWith(".pdf")) {
                    pdfFiles.add(dirItem);
                }
            }
        } else {
            throw new FileNotFoundException("Invalid directory path");
        }
        return pdfFiles;
    }

    /** Return a list of parsed PDF text **/
    static List<String> getPDFText(List<File> pdfFiles) throws IOException {
        List<String> pdfTextList = new ArrayList<String>();
        for (File pdfFile : pdfFiles) {
            PDDocument pdfDocument = PDDocument.load(pdfFile);
            pdfTextList.add(new PDFTextStripper().getText(pdfDocument));
            pdfDocument.close();
        }
        return pdfTextList;
    }
}