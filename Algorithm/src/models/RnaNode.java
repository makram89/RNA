package models;

import utils.Config;
import utils.RuntimeAdapter;
import utils.RuntimeAdapterBuilder;

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
    private final Config config;

    /**
     * Input chain
     * ex. ACUUCAGUCA
     */
    private final String chain;
    /**
     * Information in which level in tree structure this node is
     * ex. entry node has stage = 1, next nodes/leafs that are created have stage = 2, etc.
     */
    private final Integer stage;
    /**
     * Length of fragment from which this node is created
     */
    private final Integer prevChainLength;
    /**
     * mi value of cut that creat this fragment
     */
    private final Double prevMiMeasure;

    /**
     * @see RuntimeAdapter
     */
    private final RuntimeAdapter runtimeAdapter;

    /**
     * Is this node end Node (leaf)
     */
    private Boolean endNode = false;
    /**
     * List contains nodes for further processing, based on the current node
     */
    private final ArrayList<RnaNode> nextRnaNodes = new ArrayList<>();

    /**
     * Index where analyzed fragment starts
     */
    private final int index ;


    /**
     * Node constructor
     *
     * @param chain Nucleotides chain to process
     * @param stage Level in tree
     * @param prevChainLength Length of fragment that was cut
     * @param prevMiMeasure Mi value of cut that creates this node
     * @param _index index where sequence starts in original chain
     */
    public RnaNode(String chain, Integer stage, Integer prevChainLength, Double prevMiMeasure, int _index, Config _config) {
        this.chain = chain;
        this.stage = stage;
        this.prevChainLength = prevChainLength;
        this.prevMiMeasure = prevMiMeasure;
        this.index = _index;
        this.config = _config;
        runtimeAdapter = new RuntimeAdapterBuilder().version(config.version).build();


    }

    /**
     * method that is obligated to make all processing
     */
    public void process() {

        if (chain.length() < config.minChainLength) {
            endNode = true;
        } else {
            try {

                runtimeAdapter.createHelpFile(config.fasta_entry_file, chain);
                runtimeAdapter.predictStructure();

                ArrayList<RNASingleChain> rnaSingleChains = runtimeAdapter.getSingleChains();
//                System.out.println(rnaSingleChains.toString());
                for (RNASingleChain fragment : rnaSingleChains) {
                    findCutPlaces(fragment);

                }
                if (nextRnaNodes.size() == 0) {
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

        ArrayList<NucleotidesBinding> possibleCutsMi = new ArrayList<>();
        for (int i = 1; i < fragment.sequence.length(); i++) {
            String pair = fragment.sequence.substring(i - 1, i + 1);
            if (config.pairs.miValues.get(pair) != null) {
                double mi = (double) config.pairs.miValues.get(pair);
                mi = mi * prevMiMeasure;

//                System.out.println("Mi value: " + mi );
//              Choosing if place is good enough
                if (mi >= config.sigma) {
                    possibleCutsMi.add(new NucleotidesBinding(i, mi));
                }
            }
        }

//choosing only best options
        Collections.sort(possibleCutsMi);
        double avrg = 0;
        for (NucleotidesBinding element : possibleCutsMi)
        {
            avrg+=element.mi;
        }
        avrg = avrg/possibleCutsMi.size();
        for (NucleotidesBinding element : possibleCutsMi)
        {

            if (element.mi >= avrg) {
                choose(element, fragment);
            }
            else break;
        }




    }

    /**
     * Method to find best cut
     * @param info chosen place to cut
     * @param fragment whole fragment
     */
    public void choose(NucleotidesBinding info, RNASingleChain fragment) {

        String nodeChain1 = chain.substring(0, info.i + fragment.indexes[0]);
        String nodeChain2 = chain.substring(info.i + fragment.indexes[0]);


//                    System.out.println("Presenting new fragments");
//                    System.out.println(nodeChain1);
//                    System.out.println(nodeChain2);
//                    System.out.println(pair);

        RnaNode node1 = new RnaNode(nodeChain1, stage + 1, chain.length(), info.mi, index, config);
        RnaNode node2 = new RnaNode(nodeChain2, stage + 1, chain.length(), info.mi, index + info.i + fragment.indexes[0],config);
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
        return "{" +
                "\"chain\" : \"" + chain + '\"' +
                ", \"stage\" : " + stage +
                ", \"prevChainLength\" : " + prevChainLength +
                ", \"prevMiMeasure\" : " + prevMiMeasure +
                ", \"index\" : " + index +
                "}\n";
    }

    public String getChain(){

        return this.chain;
    }

    public int getLength(){
        return this.chain.length();
    }

    public Double getPrevMiMeasure(){
        return prevMiMeasure;
    }
}
