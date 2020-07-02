package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Class that store mi values for nucleotides pairs
 */
public final class Pair {

    /**
     *
     * @deprecated
     */
    public ArrayList<String> possiblePairs;

    /**
     * Dict Pair-value
     * Stores possible pairs with Mi value
     */
    public Dictionary miValues;

    /**
     * Constructor to initialize vlaues
     */
    public Pair() {
//        possiblePairs = new ArrayList<>(Arrays.asList("UA", "CA", "UC", "CC", "UG", "CG", "UU", "CU"));
        miValues = new Hashtable();
        miValues.put("UA", 0.953);
        miValues.put("CA", 0.932);
        miValues.put("UC", 0.846);
        miValues.put("CC", 0.846);

        miValues.put("UG", 0.240);
        miValues.put("CG", 0.240);
        miValues.put("UU", 0.240);
        miValues.put("CU", 0.240);
    }
}
