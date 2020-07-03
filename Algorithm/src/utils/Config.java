package utils;

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

    public Pair pairs = new Pair();

    /**
     * For filtering
     */
    public int lowerLengthBound = 5;

    /**
     * for filtering
     */
    public int upperLengthBound = 50;

    /**
     * ver
     * ver 1 - RNA fold
     */
    public int version = 1;

    boolean isLowerFilter = false;
    boolean isUpperFilter = false;

    boolean cutByAverage = true;


    public Config() {
    }


}
