package Utils;

import models.FastaEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class is used to manage FASTA format file.
 *
 *
 */
public class FastaHandler {

    private ArrayList<FastaEntry> entries = new ArrayList<FastaEntry>();
    private final String filePath;

    /**
     * @param path - path to FASTA file
     */
    public FastaHandler(String path) {
        filePath = path;
        read();
//        read file
    }

    /**
     *  method read file from path and store FASTA entries in array using FastaEntry class
     *
     * @see FastaEntry
     */
    private void read() {
        try {
            File file = new File(filePath);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            String helper = new String("");
            int lineNumber = 0;
            while ((st = br.readLine()) != null) {
//                System.out.println(st);
                if ((lineNumber % 2) == 1) {
                    entries.add(new FastaEntry(helper, st));
                } else {

                    helper = st;
                }
                lineNumber++;
            }


        } catch (IOException e) {
            System.out.println("Something goes wrong with file");
            e.printStackTrace();
        }
    }

    /**
     * Entries getter
     * @return ArrayList of FastaEntry objects
     */
    public ArrayList<FastaEntry> getEntries() {
        return entries;
    }

    /**
     * Getter for specific index FastaEntry
     *
     * @param index index of specific demanded entry
     * @return Specific FastaEntry from entries ArrayList
     */
    public FastaEntry getEntry(Integer index) {
        return entries.get(index);
    }

    /**
     *
     * @return size of entries ArrayList
     */
    public int getEntriesSize()
    {
        return entries.size();
    }


}
