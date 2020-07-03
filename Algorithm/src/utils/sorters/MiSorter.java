package utils.sorters;

import models.RnaNode;

import java.util.Comparator;

public class MiSorter implements Comparator<RnaNode> {

    @Override
    public int compare(RnaNode o1, RnaNode o2) {
        return o2.getPrevMiMeasure().compareTo(o1.getPrevMiMeasure());
    }
}
