package Utils;

import models.RNASingleChain;

import java.io.BufferedReader;
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
}
