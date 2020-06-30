import Utils.Config;
import Utils.FastaHandler;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;
import models.FastaEntry;
import models.RnaNode;


import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        System.out.println("Lets Play");

        FastaHandler temper = new FastaHandler("found_pre_mirs.fasta");
        FastaEntry oneEntry = temper.getEntry(0);
        System.out.println(oneEntry.toString());

        final Config config = new Config();
        ScriptsAdapter scriptsAdapter = new ScriptsAdapterBuilder().version(1).build();

        RnaNode startNode = new RnaNode(oneEntry.chain, 1, oneEntry.chain.length(), 1.0);

        ArrayList<RnaNode> outputFull = new ArrayList<>();
        ArrayList<RnaNode> toProcess = new ArrayList<>();

        ArrayList<RnaNode> toProcessPartial = new ArrayList<>();
//        ArrayList<RnaNode> partialOutput = new ArrayList<>();

        toProcess.add(startNode);
        System.out.println(config.pairs.miValues.get("UA"));


        while (toProcess.size() > 0)
        {
            for (RnaNode rnaNode: toProcess) {
                rnaNode.process();
                toProcessPartial = rnaNode.getNext();
                outputFull.addAll(rnaNode.getOutput());
            }
            toProcess = toProcessPartial;
        }
    }
}

