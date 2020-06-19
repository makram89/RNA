package models;

import java.util.ArrayList;

public class Node {
    public String chain;
    public Boolean isNodeFolding;

//    ???? how to make it
    public Boolean prevNode;
    public ArrayList<Node> nextNodes = new ArrayList<Node>();

    public Float miMeasure;


}
