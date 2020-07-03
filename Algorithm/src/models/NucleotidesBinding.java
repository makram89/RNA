package models;

/**
 * Class used to store values each connection
 */
public class NucleotidesBinding implements Comparable<NucleotidesBinding>{
    /**
     * Index in currently computed fragment
     */
    public int i;
    /**
     * Value of connection
     */
    public double mi;

    public NucleotidesBinding(int _i, double _mi) {
        i = _i;
        mi = _mi;
    }


    @Override
    public String toString() {
        return "Sorter{" +
                "i=" + i +
                ", mi=" + mi +
                '}';
    }


    @Override
    public int compareTo(NucleotidesBinding o) {
        return mi < o.mi ? 1 : -1;
    }
}