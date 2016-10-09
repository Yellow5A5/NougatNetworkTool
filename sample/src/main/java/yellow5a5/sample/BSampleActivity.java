package yellow5a5.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import yellow5a5.nougatnetworktool.NougatNetworkTool;

/**
 * Created by Yellow5A5 on 16/10/9.
 */

public class BSampleActivity extends Activity {


    private Button mTurnBackBtn;
    private Button mGetNetTypeBtn;
    private Button mGetIsConnect;
    private Button mSendBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_b);
        this.mTurnBackBtn = (Button) findViewById(R.id.btn_turn_back);
        this.mGetNetTypeBtn = (Button) findViewById(R.id.btn_get_network_type);
        this.mGetIsConnect = (Button) findViewById(R.id.btn_get_is_connect);
        this.mSendBroadcast = (Button) findViewById(R.id.btn_send_broadcast);

        mTurnBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGetNetTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(BSampleActivity.class.getName(), "mGetNetTypeBtn: " + (NougatNetworkTool.getInstance().getNetworkTypeClass()));
            }
        });

        mGetIsConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(BSampleActivity.class.getName(), "mGetIsConnect: " + NougatNetworkTool.getInstance().isNetworkAvailable());
            }
        });

        mSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
