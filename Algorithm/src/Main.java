import models.FastaEntry;
import models.RnaNode;
import utils.Config;
import utils.FastaHandler;
import utils.OutputManager;
import utils.RuntimeAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        FastaHandler fastaHandler = new FastaHandler(args[0]);
//        FastaHandler fastaHandler = new FastaHandler("found_pre_mirs2.fasta");

        Config config = new Config();
        Main main = new Main();

//TODO  możliwość ustawienia początkowego mi i staege w celu przetworzenia dla konkretnego fragmentu
        /*
         * -v version default 1, 0 changes from RNAfold to ContextFold
         * -ML minimal length of sequence to process, ex. -ML 15 means that sequences shorter then 15 becomes endNodes without processing
         * -e set sigma (limit value)
         * -LB lower bound of sequence length in post processing
         * -HB higher bound of sequence length in post processing
         * -ALL process all possibilities
         * -s max level of stage. Ex. -s 4, includes stage 4
         *
         */

        /*
         * -v domyślnie v =1, v= 0 oznacza użycie ContextFold, v=1 RNAFold
         * -ML minimalnia długość przetwarzanego łańcucha. -ML 15 oznacza że krótszy niż 15 znaków fragment nie będzie przetwarzany
         * -e ustawienie minimalnej wartości sigma
         * -LB ustawielnie dolnej granicy długości dla zbioru wynikowego
         * -HB jak wyżej, ale górnej granicy
         * -ALL algorytm nie przetwarza wszystkie możlwiości ( spełniające warunek długości i wartości sigma)
         * -s maksymalny poziom przetwarzanych elementów. Przy -s 4, po przetworzeniu liści z poziomu 4 program zakończy działanie
         */
        for (int i = 1; i < args.length; i++) {
            System.out.println(args[i]);
            if (args[i].equals("-v")) {
                if (Integer.parseInt(args[i + 1]) == 0) {
                    config.version = 0;
                    String string = main.read_path();
                    if (!string.isEmpty())
                    {
                        config.contextFoldPath = string;
                    }
                }
                continue;
            }
            if (args[i].equals("-LB")) {
                config.lowerLengthBound = Integer.parseInt(args[i + 1]);
                config.isLowerFilter = true;
                continue;
            }
            if (args[i].equals("-HB")) {
                config.upperLengthBound = Integer.parseInt(args[i + 1]);
                config.isUpperFilter = true;
                continue;
            }
            if (args[i].equals("-e")) {
                config.sigma = Double.parseDouble(args[i + 1]);
                continue;
            }
            if (args[i].equals("-ML")) {
                config.minChainLength = Integer.parseInt(args[i + 1]);
                continue;
            }
            if (args[i].equals("-ALL")) {
                config.cutByAverage = false;
                continue;
            }

            if (args[i].equals("-s")) {
                config.maxStage = Integer.parseInt(args[i + 1]);
                continue;
            }
            if (args[i].equals("-script")) {
                config.python_script_path = args[i+1];
            }



        }

        for (FastaEntry entry : fastaHandler.getEntries()) {

            System.out.println(entry.toString());
            main.run(entry, config);
        }


    }

    public void run(FastaEntry oneEntry, Config config) {

        OutputManager outputManager = new OutputManager();
        RuntimeAdapter runtimeAdapter = new RuntimeAdapter(config);

        /*
         * oneEntry is passed entry with its name and cain. we wat to pass only chain
         * Stage 0 because it is starting node and there is no
         * we need to supply initial length by using its length
         * start mi should be 1, but could be changed (in future)
         * index 0, because it is start
         * config file with all chosen options
         */
        RnaNode startNode = new RnaNode(oneEntry.chain, 0, oneEntry.chain.length(), 1.0, 0, config);


        /*
         * Array with end nodes (leafs)
         */
        ArrayList<RnaNode> outputFull = new ArrayList<>();
        /*
         * Array with element to process
         */
        ArrayList<RnaNode> toProcess = new ArrayList<>();
        /*
         * Array with currently found next nodes
         */
        ArrayList<RnaNode> toProcessPartial = new ArrayList<>();

//        Adding first element to processing array
        toProcess.add(startNode);

//        To count stages
        int stage = 0;

//        Measuring processing time
        long startTime = System.currentTimeMillis();

        /*
        Główna pętla przetwarzania
        warunek zakończenia: brak węzłów do dalszego przetwarzania

         */
        while (toProcess.size() > 0) {
            System.out.println("Tree level:" + stage);
            stage++;
            System.out.println("Nodes to process: " + toProcess.size());
            for (RnaNode rnaNode : toProcess) {
                rnaNode.process();
                toProcessPartial.addAll(rnaNode.getNext());
                if (rnaNode.isEndNode()) {
                    outputFull.add(rnaNode.getOutput());
                }

            }
//            Ustawienie węzłów do przetworzenia w następnej iteracji i wyczyszczenie tablicy
            toProcess = toProcessPartial;
            toProcessPartial = new ArrayList<>();
            // Monitor zakończenia przetwarzania
            if (config.maxStage > 0 && stage > config.maxStage) {
                outputFull.addAll(toProcess);
                break;
            }

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

//        Saving raw data
        runtimeAdapter.saveOutput(oneEntry, outputManager.sortByMi(outputFull), config.folder_path, "");

//        using filters
        if (config.isLowerFilter) {
            if (config.isUpperFilter) {
                outputFull = outputManager.filterByLength(outputFull, config.lowerLengthBound, config.upperLengthBound);
                runtimeAdapter.saveOutput(oneEntry, outputFull, config.folder_path, "_both_bound");
            } else {
                outputFull = outputManager.filterByLength(outputFull, config.lowerLengthBound);
                runtimeAdapter.saveOutput(oneEntry, outputFull, config.folder_path, "_lower_bound");
            }
        }

        runtimeAdapter.saveSummary(oneEntry, outputManager.countOcurrencies(outputFull), config.folder_path);
        runtimeAdapter.deleteFiles();

    }

    public String read_path()
    {

        String st = "";
        try {
            File file = new File("./context_fold_path");
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));
            st = br.readLine();
            System.out.println(st);
        } catch (IOException e) {
            System.out.println("Propably thee is no file context_fold_path with path to ContextFold");
            e.printStackTrace();
        }


        return st;
    }



}

