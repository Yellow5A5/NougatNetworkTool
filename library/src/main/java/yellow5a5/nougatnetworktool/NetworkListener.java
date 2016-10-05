package yellow5a5.nougatnetworktool;

/**
 * Created by Yellow5A5 on 16/10/4.
 */

public interface NetworkListener {

    void onNoConnect(int type);

    void onConnect(int type);

}
