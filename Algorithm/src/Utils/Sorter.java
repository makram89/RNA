package Utils;

public class Sorter implements Comparable<Sorter>{
    public int i;
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