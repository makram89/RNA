import Utils.Config;
import Utils.FastaHandler;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;
import models.FastaEntry;
import models.RnaNode;


import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {


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


        while (toProcess.size() > 0)
        {
            for (RnaNode rnaNode: toProcess) {
                rnaNode.process();
                toProcessPartial.addAll(rnaNode.getNext());
                if(rnaNode.isEndNode())
                {
                    outputFull.add(rnaNode.getOutput());
                }

            }
            toProcess = toProcessPartial;
            toProcessPartial = new ArrayList<>();
        }
//        summary
        System.out.println("Initial chain length: " + oneEntry.chain.length());
        System.out.println("Number of found degenerates: "+ outputFull.size());
        for(  RnaNode value : outputFull)
        {
            System.out.println(value.toString());
        }
    }
}

