package me.ramk.newsapp.models;

/**
 * Created by Ram K Bharathi on 2/26/2018.
 */

public class NewsRequest {
    private int channelId;
    private String isoLanguageCode;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    public void setIsoLanguageCode(String isoLanguageCode) {
        this.isoLanguageCode = isoLanguageCode;
    }
}
