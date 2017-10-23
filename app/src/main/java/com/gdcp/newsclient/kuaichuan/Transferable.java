package com.gdcp.newsclient.kuaichuan;

/**
 * Created by asus- on 2017/9/28.
 */

public interface Transferable {

    void init() throws Exception;


    /**
     *
     * @throws Exception
     */
    void parseHeader() throws Exception;


    /**
     *
     * @throws Exception
     */
    void parseBody() throws Exception;


    /**
     *
     * @throws Exception
     */
    void finish() throws Exception;
}
