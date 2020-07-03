package utils;

import models.RnaNode;
import utils.sorters.MiSorter;

import java.util.ArrayList;

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


        return output;
    }


    public ArrayList<RnaNode> filterByMi(ArrayList<RnaNode> input, double lowerBound) {

        return input;
    }


    public ArrayList<RnaNode> sortByMi(ArrayList<RnaNode> input) {
        input.sort(new MiSorter());
        return input;
    }


}
