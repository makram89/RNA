package models;

import Utils.ScriptsAdapter;

import java.util.ArrayList;

public class RnaNode {

    private String chain;
    public Integer stage;
    public Integer prevChainLength;
    public Float prevMiMeasure;
    private String dotBracketChain;

    private ScriptsAdapter scriptsAdapter;

    private ArrayList sequences;
    public ArrayList<RnaNode> nextRnaNodes = new ArrayList<RnaNode>();

    public RnaNode(String inputChain){



    }

    public void getSingleChains()


}
