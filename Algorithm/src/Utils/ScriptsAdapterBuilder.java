package Utils;

/**
 * Scripts adapter builder
 */
public class ScriptsAdapterBuilder {

    private Integer _version;

    public ScriptsAdapterBuilder()
    {
//        If nothing overwrite it, its stays at 1
        _version = 1;
    }
    public ScriptsAdapter build(){

        return new ScriptsAdapter(_version);
    }
    public ScriptsAdapterBuilder version(int ver)
    {
        _version = ver;
        return this;
    }

}
