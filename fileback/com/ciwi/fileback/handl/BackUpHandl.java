package com.ciwi.fileback.handl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ciwi.fileback.dao.BackUpDao;
import com.ciwi.fileback.dao.BackUpToFtpService;
import com.ciwi.fileback.dao.BackUpToSshService;

public class BackUpHandl {
	
	private static BackUpHandl handl = new BackUpHandl();
	private BackUpConfig config;
	private BackUpDao dao;
	private long lastModified,nowModified;
	private File lastTime_File;
	
	private BackUpHandl(){}
	
	public static BackUpHandl getInstance(){
		return handl;
	}
	
	public void setConfig(BackUpConfig config) {
		this.config = config;
	}
	
	public void setService(){
		if(config.getBackServer_protocol() == BackUpConfig.FTP_PROTOCOL)
			dao = new BackUpToFtpService(config);
		else if(config.getBackServer_protocol() == BackUpConfig.SSH_PROTOCOL)
			dao = new BackUpToSshService(config);
	}

	public void backUp(){
		dao.connect(config);
		readLastTime(config.getLastTime_record());//获取上次备份的时间
		for(String location : config.getLocation_path()){
			dao.setRemoteIndex(location.length());
			checkDir(new File(location));
		}
		dao.disconnect();
		writeNowTime();
	}
	
	private void checkDir(File file){
		File[] list = file.listFiles();
		String remote = null;
		
		for(File tmp : list){
			if(tmp.isDirectory())
				checkDir(tmp);
			else{
				if(lastModified < tmp.lastModified()){
					remote = tmp.getAbsolutePath().replace("\\","/");
					dao.upload(remote);
					System.out.println("backup:"+remote);
				}
			}
		}
	}
	
	private void readLastTime(String filePath){
		if(lastTime_File == null)
			lastTime_File = new File(filePath);
		FileReader frd = null;
		BufferedReader brd = null;
		try {
			frd = new FileReader(lastTime_File);
			brd = new BufferedReader(frd);
			lastModified = Long.parseLong(brd.readLine());
			frd.close();
			brd.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		nowModified = System.currentTimeMillis();
		//System.out.println("back.last.backup.time : "+lastModified);
	}
	
	private void writeNowTime(){
		FileWriter fw = null;
		try {
			fw = new FileWriter(lastTime_File);
			fw.write(String.valueOf(nowModified));
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
