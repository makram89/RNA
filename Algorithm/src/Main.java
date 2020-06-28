import Utils.FastaHandler;

import java.io.*;

public class Main {


    public static void main(String[] args) {
        System.out.println("Lets Play");

        FastaHandler temper = new FastaHandler("./found_pre_mirs.fasta");
        System.out.println(temper.getEntry(0).toString());


    }
}

