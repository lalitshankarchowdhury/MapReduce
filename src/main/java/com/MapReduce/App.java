package com.MapReduce;

import java.io.File;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter directory path: ");
        var dataDir = new File("./");
        var dirList = dataDir.list();

        for (var dirItem : dirList) {
            System.out.println(dirItem.toString());
        }
    }
}
