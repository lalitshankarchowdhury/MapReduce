package com.MapReduce;

import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter directory path: ");
        var sc = new Scanner(System.in);
        var dataPath = sc.nextLine();
        var dataDir = new File(dataPath);
        var dirList = dataDir.listFiles();
        if (dirList != null) {
            for (var dirItem : dirList) {
                if (dirItem.isFile()) {
                    System.out.println(dirItem.getName());
                }
            }
        }
        sc.close();
    }
}
