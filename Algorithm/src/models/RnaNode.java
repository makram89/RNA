package models;

import Utils.Config;
import Utils.ScriptsAdapter;
import Utils.ScriptsAdapterBuilder;

import java.io.IOException;
import java.util.ArrayList;

public class RnaNode {

    final private Config config = new Config();

    private String chain;
    private Integer stage;
    private Integer prevChainLength;
    private Double prevMiMeasure;

    private String dotBracketChain;


    private final ScriptsAdapter scriptsAdapter;

    private ArrayList sequences;

    private ArrayList<RnaNode> nextRnaNodes = new ArrayList<>();


    public RnaNode(String chain, Integer stage, Integer prevChainLength, Double prevMiMeasure){
        this.chain = chain;
        this.stage = stage;
        this.prevChainLength = prevChainLength;
        this.prevMiMeasure = prevMiMeasure;


        scriptsAdapter = new ScriptsAdapterBuilder().version(1).build();
        try {
            scriptsAdapter.predictStructure();
            scriptsAdapter.getSingleChains();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void getSingleChains(){
//        get chaisn via adapter
//        Need letters ad dot-bracket
    }

    public void findCutPlaces(){

    }


    public ArrayList<RnaNode> getNext() {

        return new ArrayList ();
    }

    public ArrayList<RnaNode> getOutput() {
        return new ArrayList ();
    }
}
