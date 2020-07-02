package models;

import Utils.Config;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;
import Utils.Sorter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;

/**
 * Class is used to create tree
 *
 */
public class RnaNode {
    /**
     * Variable with config values
     */
    final private Config config = new Config();

    /**
     * Input chain
     * ex. ACUUCAGUCA
     */
    private String chain;
    /**
     * Information in which level in tree structure this node is
     * ex. entry node has stage = 1, next nodes/leafs that are created have stage = 2, etc.
     */
    private Integer stage;
    /**
     * Length of fragment from which this node is created
     */
    private Integer prevChainLength;
    /**
     * mi value of cut that creat this fragment
     */
    private Double prevMiMeasure;

    /**
     * @see Utils.ScriptsAdapter
     */
    private final ScriptsAdapter scriptsAdapter;

    /**
     * Is this node end Node (leaf)
     */
    private Boolean endNode = false;
    /**
     * Place to put fragments creating by cut
     */
    private ArrayList<RnaNode> nextRnaNodes = new ArrayList<>();

    /**
     * Node constructor
     *
     * @param chain Nucleotides chain to process
     * @param stage Level in tree
     * @param prevChainLength Length of fragment that was cut
     * @param prevMiMeasure Mi value of cut that creates this node
     */
    public RnaNode(String chain, Integer stage, Integer prevChainLength, Double prevMiMeasure) {
        this.chain = chain;
        this.stage = stage;
        this.prevChainLength = prevChainLength;
        this.prevMiMeasure = prevMiMeasure;
        scriptsAdapter = new ScriptsAdapterBuilder().version(1).build();

    }

    /**
     * method that is obligated to make all processing
     */
    public void process() {

        if (chain.length() < config.minChainLength) {
//            endNode.add(this);
            endNode = true;
        } else {
            try {

                scriptsAdapter.createHelpFile(config.fasta_entry_file, chain);
                scriptsAdapter.predictStructure();

                ArrayList<RNASingleChain> rnaSingleChains = scriptsAdapter.getSingleChains();
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

    /**
     * Method takes single chain fragment and ranks nucleotides pairs in context of cut
     * @param fragment Fragment that is currently computed
     */
    public void findCutPlaces(RNASingleChain fragment) {

        ArrayList<Sorter> possibleCutsMi = new ArrayList<>();
        for (int i = 1; i < fragment.sequence.length(); i++) {
            String pair = fragment.sequence.substring(i - 1, i + 1);
            if (config.pairs.miValues.get(pair) != null) {
                double mi = (double) config.pairs.miValues.get(pair);
                mi = mi * prevMiMeasure;
//                System.out.println("Mi value: " + mi );
                if (mi >= config.sigma) {
//                    possibleCutsMi.add(new Sorter(i, mi));
                    String nodeChain1 = chain.substring(0, i + fragment.indexes[0]);
                    String nodeChain2 = chain.substring(i + fragment.indexes[0]);

//                    System.out.println("Presenting new fragments");
//                    System.out.println(nodeChain1);
//                    System.out.println(nodeChain2);
//                    System.out.println(pair);
                    RnaNode node1 = new RnaNode(nodeChain1, stage + 1, chain.length(), mi);
                    RnaNode node2 = new RnaNode(nodeChain2, stage + 1, chain.length(), mi);
                    nextRnaNodes.add(node1);
                    nextRnaNodes.add(node2);

                }
            }
        }


//        Collections.sort(possibleCutsMi);
//        if(possibleCutsMi.size()>0) {
//            choose(possibleCutsMi.get(0), fragment);
//        }


    }

    /**
     * Method to find best cut
     * @param info chosen place to cut
     * @param fragment whole fragment
     */
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

    /**
     *
     * @return next possible nodes
     */
    public ArrayList<RnaNode> getNext() {

        return nextRnaNodes;
    }

    /**
     *
     * @return this node
     */
    public RnaNode getOutput() {
        return this;
    }

    /**
     *
     * @return is this node is an end node
     */
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
                "}\n";
    }
}
