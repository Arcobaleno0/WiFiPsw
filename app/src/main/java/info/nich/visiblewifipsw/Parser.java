package info.nich.visiblewifipsw;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    static Pattern NETWORK = Pattern.compile("network\\=\\{\\s+ssid\\=\"(.+?)\"(\\s+psk\\=\"(.+?)\")?");

    public static List<WiFi> getWifi(String out) {
        Matcher matcher = null;
        try {
            matcher = NETWORK.matcher(new String(out.getBytes("ISO-8859-1"),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<WiFi> netList = new ArrayList<WiFi>();
        while(true){
            if(!matcher.find()){
                Collections.sort(netList);
                return netList;
            }
            WiFi wifi = new WiFi();
            wifi.setSsid(matcher.group(1));
            wifi.setPsw(matcher.group(3));
            netList.add(wifi);
        }
    }
}
