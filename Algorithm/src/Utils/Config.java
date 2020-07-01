package Utils;

import models.Pair;

public final class Config {

    public String folder_path = "./converting_dir";

    public String fasta_entry_file = "DO_NOT_REMOVE_FILE_FASTA";
    public String dot_bracket_file = "DO_NOT_REMOVE_FILE";
    public String s_chain_file = "./DO_NOT_REMOVE_FILE_SINGLE";
    public String python_script_path = "../scripts/rna_lines_extractor.py";

    public int minChainLength = 15;
    public double sigma = 0.01;

    public Pair pairs;

    public Config(){
        pairs = new Pair();
    }




}