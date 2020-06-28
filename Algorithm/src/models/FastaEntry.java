package models;

/**
 * Class to store FASTA format entry
 * >name
 * nucleotides chain ex. AAUGUGT
 */
public class FastaEntry {

    public String name;
    public String chain;

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
