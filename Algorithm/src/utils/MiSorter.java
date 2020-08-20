package utils;

import models.RnaNode;

import java.util.Comparator;

/**
 * Używane do sortowania miejsc cięcia
 */
public class MiSorter implements Comparator<RnaNode> {

    @Override
    public int compare(RnaNode o1, RnaNode o2) {
        return o2.getPrevMiMeasure().compareTo(o1.getPrevMiMeasure());
    }
}
