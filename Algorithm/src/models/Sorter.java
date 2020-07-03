package models;

/**
 * Class used to store values each connection
 */
public class Sorter implements Comparable<Sorter>{
    /**
     * Index in currently computed fragment
     */
    public int i;
    /**
     * Value of connection
     */
    public double mi;

    public Sorter(int _i, double _mi) {
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
    public int compareTo(Sorter o) {
        return mi < o.mi ? 1 : -1;
    }
}