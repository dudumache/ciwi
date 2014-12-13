package com.ciwi.fileback.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.ciwi.fileback.handl.BackUpConfig;
import com.ciwi.fileback.handl.BackUpHandl;

public class BackUpThread implements Runnable {

	public void run() {
		BackUpConfig config = loadConfig();
		BackUpHandl handl = null;
		if(config.isback()){
			handl = BackUpHandl.getInstance();
			handl.setConfig(config);
			handl.setService();
			while(true){
				handl.backUp();
				//System.out.println("开始睡觉");
				try {
					Thread.sleep(config.getBack_repeat());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else
			System.out.println("文件自动备份功能:禁用");
	}
	
	@SuppressWarnings("unchecked")
	private BackUpConfig loadConfig(){
			InputSource source = null;
			InputStream is = null;
			SAXBuilder sb = null;
			Document document = null;
			Element root = null,elementTemp = null;
			Iterator<Element> iterator = null;
			Map<String,String> map = new HashMap<String,String>();
			BackUpConfig config = null;
			
			try {
				//is = BackUpThread.class.getResourceAsStream("/SystemParameters.xml");
				//source = new InputSource(is);
				sb = new SAXBuilder();
				//document = sb.build(source);
				document = sb.build(new File("SystemParameters.xml"));
				root = document.getRootElement();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(is != null)
					try {
						is.close();is = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
			iterator = root.getChildren().iterator();
			while(iterator.hasNext()){
				elementTemp = iterator.next();
				if(elementTemp.getAttributeValue("name").startsWith("BACKUP"))
					map.put(elementTemp.getAttributeValue("name"),elementTemp.getAttributeValue("value"));
			}
			if(!map.isEmpty()){
				config = new BackUpConfig(map.get("BACKUP_SERVER"),
						map.get("BACKUP_THREAD_REPEAT"),
						map.get("BACKUP_LOCATION_PATH"),
						map.get("BACKUP_LASTTIME_RECORD"),
						map.get("BACKUP_FILENAME_CHARSET"));
			}
		return config;
	}
	
}
