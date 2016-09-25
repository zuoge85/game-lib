package com.isnowfox.game.platform;

/**
 * @author zuoge85 on 2015/2/4.
 */
public class UserInfo {
    private String uuid;
    private int yellow;// = String.valueOf(map.get("yellow"));
    private boolean year;// = String.valueOf(map.get("year"));
    private Object param;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getYellow() {
        return yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public boolean isYear() {
        return year;
    }

    public void setYear(boolean year) {
        this.year = year;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uuid='" + uuid + '\'' +
                ", yellow=" + yellow +
                ", year=" + year +
                ", param=" + param +
                '}';
    }
}
