package utils;

/**
 * Scripts adapter builder
 */
public class RuntimeAdapterBuilder {

    private Integer _version;

    public RuntimeAdapterBuilder()
    {
//        If nothing overwrite it, its stays at 1
        _version = 1;
    }
    public RuntimeAdapter build(){

        return new RuntimeAdapter(_version);
    }
    public RuntimeAdapterBuilder version(int ver)
    {
        _version = ver;
        return this;
    }

}
