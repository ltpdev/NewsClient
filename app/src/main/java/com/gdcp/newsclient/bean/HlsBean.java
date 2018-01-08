package com.gdcp.newsclient.bean;

/**
 * Created by asus- on 2018/1/7.
 */

public class HlsBean {


    /**
     * hls_url : http://hls3.douyucdn.cn/live/1863767rgsonfI04_550/playlist.m3u8?wsSecret=28175ce6514b23f29dcb16b0b09abfae&wsTime=1515312517&token=h5-douyu-0-1863767-d74ef24d3001ffbb45793ddc849d712d&did=h5_did
     */

    private DataBean data;
    /**
     * data : {"hls_url":"http://hls3.douyucdn.cn/live/1863767rgsonfI04_550/playlist.m3u8?wsSecret=28175ce6514b23f29dcb16b0b09abfae&wsTime=1515312517&token=h5-douyu-0-1863767-d74ef24d3001ffbb45793ddc849d712d&did=h5_did"}
     * error : 0
     */

    private int error;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class DataBean {
        private String hls_url;

        public String getHls_url() {
            return hls_url;
        }

        public void setHls_url(String hls_url) {
            this.hls_url = hls_url;
        }
    }
}
