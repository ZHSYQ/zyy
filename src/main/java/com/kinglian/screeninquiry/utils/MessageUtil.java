/**
 * Copyright (C), 2018-2019,Kinglian
 * FileName: MessageUtil
 * Author:   weiyz
 * Date:     2019/1/30 18:02
 * Description: MessageUtil
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.kinglian.screeninquiry.utils;

import com.kinglian.screeninquiry.model.dto.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 〈MessageUtil〉
 * @author weiyz
 * @create 2019/1/30
 * @since 1.0.0
 */
public class MessageUtil {

    /**
     * text
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * music
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * news
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * text
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * image
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * link
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * location
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * voice
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * video
     */
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";


    public static final String REQ_MESSAGE_TYPE_SCAN = "SCAN";



    /**
     * shortvideo
     */
    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

    /**
     * event
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * subscribe
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * unsubscribe
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * CLICK
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    public static Map<String,String> parseXml(HttpServletRequest request){
        Map<String,String> messageMap=new HashMap<String, String>();
        InputStream inputStream=null;
        try {
            //读取request Stream信息
            inputStream=request.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SAXReader reader = new SAXReader();
        Document document=null;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element root=document.getRootElement();
        List<Element> elementsList=root.elements();
        for(Element e:elementsList){
            messageMap.put(e.getName(),e.getText());
        }
        try {
            inputStream.close();
            inputStream=null;
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return messageMap;
    }


    /**
     * 转换文本消息
     *
     * @param textMessage
     *
     * @return xml
     */
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }


    /**
     * xml转换为map
     * @param request
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException{
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        InputStream ins = null;
        try {
            ins = request.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {
            doc = reader.read(ins);
            Element root = doc.getRootElement();

            List<Element> list = root.elements();

            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }finally{
            ins.close();
        }

        return null;
    }

    /**
     *
     * 定义xstream让value自动加上CDATA标签
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                boolean cdata = false;
                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {

                    if (!name.equals("xml")) {
                        char[] arr = name.toCharArray();
                        if (arr[0] >= 'a' && arr[0] <= 'z') {
                            // arr[0] -= 'a' - 'A';
                            arr[0] = (char) ((int) arr[0] - 32);
                        }
                        name = new String(arr);
                    }
                    super.startNode(name, clazz);
                }

                @Override
                public void setValue(String text) {

                    if (text != null && !"".equals(text)) {
                        Pattern patternInt = Pattern
                                .compile("[0-9]*(\\.?)[0-9]*");
                        Pattern patternFloat = Pattern.compile("[0-9]+");
                        if (patternInt.matcher(text).matches()
                                || patternFloat.matcher(text).matches()) {
                            cdata = false;
                        } else {
                            cdata = true;
                        }
                    }
                    super.setValue(text);
                }

                protected void writeText(QuickWriter writer, String text) {

                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }

    });



}
