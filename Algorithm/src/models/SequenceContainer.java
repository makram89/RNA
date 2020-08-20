package models;

/**
 * Klasa do tworzenia pliku wyjściowego
 */
public class SequenceContainer implements Comparable<SequenceContainer> {

    public final String chain;
    public int counter;

    public SequenceContainer(String chain, int _counter) {
        this.chain = chain;
        this.counter = _counter;
    }

    @Override
    public String toString() {
        return "{\"SEQ\" : \"" + chain + "\"," +
                " \"occurred\" : " + counter + "}\n" ;

    }

    @Override
    public int compareTo(SequenceContainer o) {
            return counter < o.counter ? 1 : -1;
    }
}
