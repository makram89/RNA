import utils.*;
import models.FastaEntry;
import models.RnaNode;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {


        FastaHandler temper = new FastaHandler("found_pre_mirs2.fasta");
        FastaEntry oneEntry = temper.getEntry(0);
        System.out.println(oneEntry.toString());
        Main main = new Main();

        main.run(oneEntry);


    }

    public void run( FastaEntry oneEntry)
    {

        final Config config = new Config();
        OutputManager outputManager = new OutputManager();
        ScriptsAdapter scriptsAdapter = new ScriptsAdapterBuilder().version(1).build();

        /**
         * oneEntry is passed entry with its name and cain. we wat to pass only chain
         * Stage 1 because it is starting node
         * we need to supply initial length by using its length
         * start mi should be 1
         * index 0, because it is start
         * config file with all chosen options
         */
        RnaNode startNode = new RnaNode(oneEntry.chain, 1, oneEntry.chain.length(), 1.0, 0, config);


        /**
         * Array with end nodes (leafs)
         */
        ArrayList<RnaNode> outputFull = new ArrayList<>();
        /**
         * Array with element to process
         */
        ArrayList<RnaNode> toProcess = new ArrayList<>();
        /**
         * Array
         */
        ArrayList<RnaNode> toProcessPartial = new ArrayList<>();

//        Adding first element to processing array
        toProcess.add(startNode);

//
        int stage = 1;

//        Measuring processing time
        long startTime = System.currentTimeMillis();

        while (toProcess.size() > 0) {
            for (RnaNode rnaNode : toProcess) {
                rnaNode.process();
                toProcessPartial.addAll(rnaNode.getNext());
                if (rnaNode.isEndNode()) {
                    outputFull.add(rnaNode.getOutput()); }

            }
            toProcess = toProcessPartial;
            toProcessPartial = new ArrayList<>();
        }
        double elapsedTime = System.currentTimeMillis() - startTime;


        String unit = " msec";
        if (elapsedTime >= 1_000) {
            elapsedTime = elapsedTime / 1_000.0;
            unit = " sec";

            if (elapsedTime >= 60) {
                elapsedTime = elapsedTime / 60.0;
                unit = "min";

                if (elapsedTime >= 60) {
                    elapsedTime = elapsedTime / 60.0;
                    unit = " hr";
                }
            }
        }


        System.out.println("Processing finished, saving...: " + String.format("%.2f", elapsedTime) + unit);

//        summary
        System.out.println("Initial chain length: " + oneEntry.chain.length());
        System.out.println("Number of found degradants: " + outputFull.size());


        scriptsAdapter.saveOutput(oneEntry, outputManager.filterByLength(outputFull,config.lowerLengthBound), config.folder_path);

        outputManager.countOcurrencies(outputFull);

    }
}

