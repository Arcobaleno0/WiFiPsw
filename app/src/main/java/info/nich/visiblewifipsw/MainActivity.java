package info.nich.visiblewifipsw;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    final String cmd = "cat /data/misc/wifi/wpa_supplicant.conf";
    String out = null;
    public TextView tv;
    public ListView lv;
    private WifiAdapter wifiAdapter;
    public static List<WiFi> dataList;
    private List<WiFi> noPswList;
    private String copyData;
    private String myURL ="http://inich.info/2015/10/17/wifipasswordtool/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv = (TextView) findViewById(R.id.textView);
        lv = (ListView) findViewById(R.id.wifiTv);
        dataList = new ArrayList<WiFi>();
        noPswList = new ArrayList<WiFi>();
        toolbar.setTitle("WiFi密码查看");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_about:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_share:
                        Intent intent1 = new Intent();
                        intent1.setAction(Intent.ACTION_SEND);
                        intent1.putExtra(Intent.EXTRA_TEXT,myURL);
                        intent1.setType("text/plain");
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });

        //try to attain su and fetch the wifi list.
        boolean flag = info.nich.visiblewifipsw.RootCmd.haveRoot();
        if (flag) {
            try {
                StringBuilder sb = new StringBuilder();
                DataInputStream dis = info.nich.visiblewifipsw.RootCmd.execRootCmd(cmd);
                if (dis.read() == -1) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(R.string.unroot);
                }
                String temp;
                while ((temp = dis.readLine()) != null)
                    sb.append(temp);
                out = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //判断有否数据
            if (out == null) {
                tv.setVisibility(View.VISIBLE);
            }
            dataList = Parser.getWifi(out);
            for (WiFi wifi : dataList) {
                if (wifi.getPsw() == null)
                    noPswList.add(wifi);
            }

            wifiAdapter = new WifiAdapter(this, dataList);
            lv.setAdapter(wifiAdapter);
        } else {
            tv.setVisibility(View.VISIBLE);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), dataList.get(position).getSsid() + "的密码已复制到剪贴板", Toast.LENGTH_SHORT).show();
                copyData = dataList.get(position).getPsw();
                MainActivity.copy(copyData, MainActivity.this);
            }
        });

    }

    public static void copy(String content, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}