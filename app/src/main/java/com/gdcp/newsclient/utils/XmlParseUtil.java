package com.gdcp.newsclient.utils;

import android.util.Xml;

import com.gdcp.newsclient.bean.AddItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus- on 2017/7/2.
 */

public class XmlParseUtil {
    public static List<AddItem> getAddItemInfo(String xml){
        System.out.println("-----ddddddd");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes());
        XmlPullParser pullParser = Xml.newPullParser();
        try {
            pullParser.setInput(byteArrayInputStream, "utf-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        List<AddItem> addItemList = null;
        AddItem addItem = null;
        int type = 0;
        try {
            type = pullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if (pullParser.getName().equals("ul")) {
                        addItemList = new ArrayList<>();
                    } else if (pullParser.getName().equals("li")) {
                        addItem = new AddItem();
                    }else if (pullParser.getName().equals("a")) {
                        String id=pullParser.getAttributeValue(0);
                        String title= null;
                        try {
                            title = pullParser.nextText();
                            System.out.println("--dd"+id);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int index=id.indexOf("T1");
                        int lastIndex=id.indexOf("/0");
                        addItem.setTitle(title);
                        addItem.setChannelId(id.substring(index,lastIndex));

                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (pullParser.getName().equals("li")) {
                        addItemList.add(addItem);
                        addItem=null;
                    }

                break;

            }

            try {
                type=pullParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return addItemList;
    }
}
