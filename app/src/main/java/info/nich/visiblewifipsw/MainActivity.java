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
    private String copyData;
    private String out = null;
    private TextView tv;
    private ListView lv;
    private static List<WiFi> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setUpToolbar();
        setUpData();
        setClickListener();
    }

    private void setClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), dataList.get(position).getSsid() + "的密码已复制到剪贴板", Toast.LENGTH_SHORT).show();
                copyData = dataList.get(position).getPsw();
                MainActivity.copy(copyData, MainActivity.this);
            }

        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "把这个WiFi分享给好友", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, dataList.get(position).getSsid() + " 的密码是 " + dataList.get(position).getPsw());
                intent.setType("text/plain");
                startActivity(intent);
                return true;
            }
        });
    }

    private void setUpData() {
        //try to attain su and fetch the wifi list.
        boolean flag = RootCmd.haveRoot();
        if (flag) {
            try {
                StringBuilder sb = new StringBuilder();
                DataInputStream dis = RootCmd.execRootCmd(cmd);
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

            WifiAdapter wifiAdapter = new WifiAdapter(this, dataList);
            lv.setAdapter(wifiAdapter);
        } else {
            tv.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        tv = (TextView) findViewById(R.id.textView);
        lv = (ListView) findViewById(R.id.wifiTv);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.appName));
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
                        intent1.putExtra(Intent.EXTRA_TEXT, getApplicationContext().getString(R.string.appURL));
                        intent1.setType("text/plain");
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
    }

    public static void copy(String content, Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}