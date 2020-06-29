import Utils.FastaHandler;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;
import models.FastaEntry;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        System.out.println("Lets Play");

        FastaHandler temper = new FastaHandler("found_pre_mirs.fasta");
        FastaEntry oneEntry = temper.getEntry(0);
        System.out.println(oneEntry.toString());

        ScriptsAdapter helper = new ScriptsAdapterBuilder().version(1).build();
        try {
            helper.createHelpFile(oneEntry.name, oneEntry.chain);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

