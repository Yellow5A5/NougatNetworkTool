package yellow5a5.nougatnetworktool;

/**
 * Created by Yellow5A5 on 16/10/9.
 */

class NetworkUtil {

    private static final String TYPE_WIFI = "WIFI";
    private static final String TYPE_MOBILE = "MOBILE";

    static int typeNameToTypeClass(String typeName) {
        int typeClass = 0;
        switch (typeName) {
            case NetworkUtil.TYPE_WIFI:
                typeClass = Constants.NETWORK_CLASS_WIFI;
                break;

            case NetworkUtil.TYPE_MOBILE:
                typeClass = Constants.NETWORK_CLASS_MOBILE;
                break;
        }
        return typeClass;
    }
}
