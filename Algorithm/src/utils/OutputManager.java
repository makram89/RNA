package utils;

import models.RnaNode;
import utils.sorters.MiSorter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class OutputManager {


    public ArrayList<RnaNode> filterByLength(ArrayList<RnaNode> input, int lowerBound) {

        ArrayList<RnaNode> output = new ArrayList<>();
        for (RnaNode rnaNode : input) {
            if (rnaNode.getLength() >= lowerBound) {
                output.add(rnaNode);
            }
        }
        return output;
    }

    public ArrayList<RnaNode> filterByLength(ArrayList<RnaNode> input, int lowerBound, int upperBound) {
        ArrayList<RnaNode> output = new ArrayList<>();
        for (RnaNode rnaNode : input) {
            if (rnaNode.getLength() >= lowerBound && rnaNode.getLength()<= upperBound ) {
                output.add(rnaNode);
            }
        }

        return output;
    }


    public ArrayList<RnaNode> filterByMi(ArrayList<RnaNode> input, double lowerBound) {
        ArrayList<RnaNode> output = new ArrayList<>();
        for (RnaNode rnaNode : input) {
            if (rnaNode.getPrevMiMeasure() >= lowerBound) {
                output.add(rnaNode);
            }
        }
        return output;
    }


    public ArrayList<RnaNode> sortByMi(ArrayList<RnaNode> input) {
        input.sort(new MiSorter());
        return input;
    }

    public ArrayList<SequenceContainer> countOcurrencies(ArrayList<RnaNode> input) {

        HashMap<String, Integer> occMap = new HashMap<>();
        for (RnaNode rnaNode : input)
        {

            if(occMap.containsKey(rnaNode.getChain()))
            {
                occMap.put(rnaNode.getChain(),occMap.get(rnaNode.getChain())+1);
            }
            else {
                occMap.put(rnaNode.getChain(),1);
            }
        }
        ArrayList<SequenceContainer> output = new ArrayList<>();
        // 2. For-each Loop
        for (String key : occMap.keySet()) {
//            System.out.println(key + ": " + occMap.get(key));
            output.add(new SequenceContainer(key,  occMap.get(key)));
        }
        Collections.sort(output);

        return output;
    }


}
