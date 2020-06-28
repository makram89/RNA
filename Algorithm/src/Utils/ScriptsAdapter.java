package Utils;

import models.RNASingleChain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private void createHelFile(String nChain, String dotChain) throws IOException {
//  Create file to help script use

        String fileSeparator = System.getProperty("file.separator");

        //absolute file name with path
        String absoluteFilePath = fileSeparator + "Users" + fileSeparator + "pankaj" + fileSeparator + "file.txt";
        File file = new File(absoluteFilePath);
        if (file.createNewFile()) {
            System.out.println(absoluteFilePath + " File Created");
        } else System.out.println("File " + absoluteFilePath + " already exists");

        //file name only
        file = new File("file.txt");
        if (file.createNewFile()) {
            System.out.println("file.txt File Created in Project root directory");
        } else System.out.println("File file.txt already exists in the project root directory");

        //relative path
        String relativePath = "tmp" + fileSeparator + "file.txt";
        file = new File(relativePath);
        if (file.createNewFile()) {
            System.out.println(relativePath + " File Created in Project root directory");
        } else System.out.println("File " + relativePath + " already exists in the project root directory");
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
