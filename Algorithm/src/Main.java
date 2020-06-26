import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.println("Lets Play");
        String command = "python ~/Dokumenty/RNA/scripts/rna_lines_extractor.py";
        Process p;

//        p = Runtime.getRuntime().exec(command + "~/Dokumenty/RNA/scripts/exampleOutputRNAFold.txt > output.txt");
        p = Runtime.getRuntime().exec("python ~/Dokumenty/RNA/scripts/rna_lines_extractor.py ~/Dokumenty/RNA/scripts/exampleOutputRNAFold.txt ");

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ret = in.readLine();
        System.out.println("value is : " + ret);

    }
}

