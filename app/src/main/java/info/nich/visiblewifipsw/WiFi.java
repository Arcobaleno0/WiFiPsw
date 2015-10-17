package info.nich.visiblewifipsw;

public class WiFi implements Comparable<WiFi> {

    private String ssid;
    private String psw;
    private String security;


    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    @Override
    public int compareTo(WiFi another) {
        if(psw!=null&&another.psw==null)
            return -1;
        if(psw==null&&another.psw!=null)
            return 1;
        return 0;
    }
}
