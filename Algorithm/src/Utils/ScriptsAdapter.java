package Utils;

import models.RNASingleChain;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class ScriptsAdapter {

    public String[] ScriptName = {"RNAfold", "RNACentroidR"};

    public Integer version;

    ScriptsAdapter(int ver) {
        version = ver;
    }

    public ArrayList<RNASingleChain> getSingleChains(String input) {


        try {
            String command = "./src/rna_lines_extractor.py";
            String arg = " ./src/exampleOutputRNAFold.txt";

            Process p = Runtime.getRuntime().exec("python3 " + command + arg);

//            ProcessBuilder pb = new ProcessBuilder("python3", command, "" + arg);
//            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = in.readLine();
            System.out.println("value is : " + ret);
            while ((ret = in.readLine()) != null) {
                System.out.println("value is : " + ret);

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Bad luck!");
        }
        return new ArrayList();
    }

    public void createHelpFile(String nChain, String dotChain) throws IOException {
//  Create file to help script use

        String fileSeparator = System.getProperty("file.separator");

        //file name only
//        TODO make config with files name etc
        File file = new File("DO_NOT_REMOVE_FILE");
        if (file.createNewFile()) {
            System.out.println("DO_NOT_REMOVE_FILE File Created in Project root directory");
        } else {
            System.out.println("File DO_NOT_REMOVE_FILE already exists in the project root directory");
            System.out.println("Trunc...");
            try (FileChannel outChan = new FileOutputStream(file, true).getChannel()) {
                outChan.truncate(0);
            }
        }

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(nChain + "\n" + dotChain);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {

            }
        }


    }


    public void folderXD() throws IOException {
        Process p = Runtime.getRuntime().exec("RNAfold ./src/found_pre_mirs.fasta --noPS -otemp.txt");

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ret = in.readLine();
        System.out.println("value is : " + ret);
        while ((ret = in.readLine()) != null) {
            System.out.println("value is : " + ret);

        } 

    }
}
