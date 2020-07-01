package models;

import Utils.Config;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;
import Utils.Sorter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;


public class RnaNode {

    final private Config config = new Config();

    private String chain;
    private Integer stage;
    private Integer prevChainLength;
    private Double prevMiMeasure;

    private String dotBracketChain;

    private final ScriptsAdapter scriptsAdapter;

    //    Może coż żebyt zwracać tylko siebie ??
    private Boolean endNode = false;

    private ArrayList<RnaNode> nextRnaNodes = new ArrayList<>();
    private ArrayList<RNASingleChain> rnaSingleChains;


    public RnaNode(String chain, Integer stage, Integer prevChainLength, Double prevMiMeasure) {
        this.chain = chain;
        this.stage = stage;
        this.prevChainLength = prevChainLength;
        this.prevMiMeasure = prevMiMeasure;


        scriptsAdapter = new ScriptsAdapterBuilder().version(1).build();

    }

    /**
     * method that is obligated to make all proccesing
     */
    public void process() {

        if (chain.length() < config.minChainLength) {
//            endNode.add(this);
            endNode = true;
        } else {
            try {

                scriptsAdapter.createHelpFile(config.fasta_entry_file, chain);
                scriptsAdapter.predictStructure();
                rnaSingleChains = scriptsAdapter.getSingleChains();
//                System.out.println(rnaSingleChains.toString());
                for (RNASingleChain fragment : rnaSingleChains) {
                    findCutPlaces(fragment);

                }
                if (nextRnaNodes.size() == 0) {
//                    endNode.add(this);
                    endNode = true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void findCutPlaces(RNASingleChain fragment) {

        ArrayList<Sorter> possibleCutsMi = new ArrayList<>();
        for (int i = 1; i < fragment.sequence.length(); i++) {
            String pair = fragment.sequence.substring(i - 1, i + 1);
            if (config.pairs.miValues.get(pair) != null) {
                double mi = (double) config.pairs.miValues.get(pair);
                mi = mi * prevMiMeasure;
//                System.out.println("Mi value: " + mi );
                if (mi >= config.sigma) {
                    possibleCutsMi.add(new Sorter(i, mi));
                }
            }
        }


        Collections.sort(possibleCutsMi);
        if(possibleCutsMi.size()>0) {
            choose(possibleCutsMi.get(0), fragment);
        }


    }

    public void choose(Sorter info, RNASingleChain fragment) {

        String nodeChain1 = chain.substring(0, info.i + fragment.indexes[0]);
        String nodeChain2 = chain.substring(info.i + fragment.indexes[0]);


//                    System.out.println("Presenting new fragments");
//                    System.out.println(nodeChain1);
//                    System.out.println(nodeChain2);
//                    System.out.println(pair);

        RnaNode node1 = new RnaNode(nodeChain1, stage + 1, chain.length(), info.mi);
        RnaNode node2 = new RnaNode(nodeChain2, stage + 1, chain.length(), info.mi);
        nextRnaNodes.add(node1);
        nextRnaNodes.add(node2);

    }


    public ArrayList<RnaNode> getNext() {

        return nextRnaNodes;
    }

    public RnaNode getOutput() {
        return this;
    }

    public Boolean isEndNode() {
        return endNode;
    }

    @Override
    public String toString() {
        return "RnaNode{" +
                "chain='" + chain + '\'' +
                ", stage=" + stage +
                ", prevChainLength=" + prevChainLength +
                ", prevMiMeasure=" + prevMiMeasure +
                '}';
    }
}
