package am.shared.enums;

/**
 * Created by Ahmed Mater on 1/28/2017.
 */
public enum EC {
    AMT_0000, AMT_0001, AMT_0002, AMT_0003, AMT_0004, AMT_0005, AMT_0006, AMT_0007, AMT_0008, AMT_0009,
    AMT_0010, AMT_0011, AMT_0012, AMT_0013, AMT_0014, AMT_0015, AMT_0016, AMT_0017, AMT_0018, AMT_0019,
    AMT_0020, AMT_0021, AMT_0022, AMT_0023, AMT_0024, AMT_0025, AMT_0026, AMT_0027, AMT_0028, AMT_0029,
    AMT_0030, AMT_0031, AMT_0032, AMT_0033, AMT_0034, AMT_0035, AMT_0036, AMT_0037, AMT_0038, AMT_0039;

    @Override
    public String toString() {
        return super.toString().replaceAll("_","-");
    }
}
