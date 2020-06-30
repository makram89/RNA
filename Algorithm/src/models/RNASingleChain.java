package models;


import java.util.Arrays;

/**
 * Class to handle output form python script
 */
public class RNASingleChain {

    public Integer id;
    public String sequence;
    public Integer[] indexes = new Integer[2];

    @Override
    public String toString() {
        return "RNASingleChain{" +
                "id=" + id +
                ", sequence='" + sequence + '\'' +
                ", indexes=" + Arrays.toString(indexes) +
                '}';
    }
}
