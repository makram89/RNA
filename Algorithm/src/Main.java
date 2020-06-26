import java.io.*;

public class Main {


    public static void main(String[] args) {
        System.out.println("Lets Play");
        try {
            String command = "./src/rna_lines_extractor.py";
            String arg = " ./src/exampleOutputRNAFold.txt";
            Process p = Runtime.getRuntime().exec("RNAfold ./src/found_pre_mirs.fasta --noPS -otemp.txt");

//            ProcessBuilder pb = new ProcessBuilder("python3", command,  arg);
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


    }
}

