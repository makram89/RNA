package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

public class Pair {


    public ArrayList<String> possiblePairs;
    public Dictionary miValues;

    //    Defined mi values for each pair
    // From delived article
    public Pair() {
        possiblePairs = new ArrayList<String>(Arrays.asList("UA", "CA", "UC", "CC", "UG", "CG", "UU", "CU"));
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
