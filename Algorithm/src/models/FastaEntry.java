package models;

/**
 * Class to store FASTA format entry
 * >name
 * nucleotides chain ex. AAUGUGT
 */
public class FastaEntry {

    /**
     * Name of sequence
     */
    public String name;
    /**
     * Sequence
     */
    public String chain;

    /**
     * Constructor
     *
     * @param _name Name of sequence
     * @param _chain Sequence
     */
    public FastaEntry(String _name, String _chain) {
        this.name = _name;
        this.chain = _chain;
    }

    @Override
    public String toString() {
        return "FastaEntry{" +
                "name='" + name + '\'' +
                ", chain='" + chain + '\'' +
                '}';
    }
}
