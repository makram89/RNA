package Utils;

import models.RNASingleChain;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class ScriptsAdapter {

    public String[] ScriptName = {"RNAfold", "RNACentroid"};

    public Integer version;

    private final Config config;

    ScriptsAdapter(int ver) {
        config = new Config();
        version = ver;
    }

    public ArrayList<RNASingleChain> getSingleChains(String input) {

        try {
            String command = "./src/rna_lines_extractor.py";
            String arg = config.dot_bracket_file;

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

    public void predictStructure() throws IOException {


//        Process p = Runtime.getRuntime().exec("RNAfold ./src/found_pre_mirs.fasta --noPS -otemp.txt");

        createFile(config.dot_bracket_file);
        String command = "RNAfold ";
        String arg1 = " --noPS ";
        String arg2 = config.fasta_entry_file;
        String arg3 = "-o" + config.dot_bracket_file;


        Process p = Runtime.getRuntime().exec(command + arg2 + arg1 + arg3);

//        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String ret = in.readLine();
//        System.out.println("value is : " + ret);
//        while ((ret = in.readLine()) != null) {
//            System.out.println("value is : " + ret);
//
//        }

    }

    public void createFile(String fileName) throws IOException {

        File file = new File(fileName);

        if (file.createNewFile()) {
            System.out.println(fileName + " File Created in Project root directory");
        } else {
            System.out.println("File " + fileName + " already exists in the project root directory");
            System.out.println("Trunc...");

            try (FileChannel outChan = new FileOutputStream(file, true).getChannel()) {
                outChan.truncate(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void createHelpFile(String fileName, String nChain, String dotChain) throws IOException {
//  Create file to help script use


//        TODO make config with files name etc
//        String fileName = config.dot_bracket_file;

        createFile(fileName);
        File file = new File(fileName);


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


}
