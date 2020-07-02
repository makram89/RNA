package Utils;

import models.Pair;

/**
 * Program config values
 */
public final class Config {

    /**
     * Path to dir with output
     */
    public String folder_path = "./output_dir";

    /**
     * file for pasting FASTA entry
     */
    public String fasta_entry_file = "DO_NOT_REMOVE_FILE_FASTA";

    /**
     * file to paste data to python script
     */
    public String dot_bracket_file = "DO_NOT_REMOVE_FILE";

    /**
     * Path to place where script is stored
     */
    public String python_script_path = "../scripts/rna_lines_extractor.py";

    public int minChainLength = 15;
    public double sigma = 0.01;

    public Pair pairs;

    public Config() {
        pairs = new Pair();
    }


}
