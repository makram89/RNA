package Utils;


import models.FastaEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FastaHandler {

    private ArrayList<FastaEntry> entries;
    private String filePath;

    public FastaHandler(String path) {
        filePath = path;
        read();
//        read file
    }

    public void read() {
        try {
            File file = new File(filePath);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
            {
                System.out.println(st);
                entries.add(new FastaEntry(st,br.readLine()));
            }



        }catch (IOException e)
        {
            System.out.println("Something goes wrong with file");
            e.printStackTrace();
        }
    }


    public ArrayList<FastaEntry> getEntries() {
        return entries;
    }

    public FastaEntry getEntry(Integer index) {
        return entries.get(index);
    }


}
