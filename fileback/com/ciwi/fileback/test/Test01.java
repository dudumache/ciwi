package com.ciwi.fileback.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.ciwi.fileback.handl.BackUpConfig;
import com.ciwi.fileback.main.BackUpThread;
import com.ciwi.fileback.util.FtpUtil;
import com.ciwi.fileback.util.SftpUtil;

public class Test01 {
	
	public static void main01(String[] args) {//01
		File file = new File("E:/eOMP/workspace_d5000/tbp-web-app");//D:/ant-test
		checkDir(file);
	}
	
	private static void checkDir(File file){
		File[] list = file.listFiles();
		for(File tmp : list){
			if(tmp.isDirectory())
				checkDir(tmp);
			else{
				if(!tmp.getName().endsWith("jar"))
					continue;
				System.out.println(new Date(tmp.lastModified())+" --- "+tmp.getAbsolutePath());
			}
		}
	}
	
	public static void main02(String[] args) {//02
		BackUpConfig config = new BackUpConfig("ftp://Administrator:tellhow@10.36.12.196:21/access/gz","300","D:/AA","","");
		System.out.println(config);
	}
	
	public static void main03(String[] args) {//03
		FtpUtil ftp = new FtpUtil("10.36.12.196","Administrator","tellhow",21,"/access");
		ftp.createFtpClient();
		ftp.uploadFile("d:/20141201/江西pms全覆盖设备统计20141201.xls","access/江西pms全覆盖设备统计20141201.xls");
		ftp.closeFtpConnection();
	}
	
	public static void main04(String[] args) {//04
		storeFile("aa/bvb/cc/asas/aa123a.xml");
	}
	
	private static void storeFile(String remote){
		if(remote.contains("/")){
			int index = remote.indexOf("/");
			String dir = remote.substring(0,index);
			remote = remote.substring(index+1);
			
			System.out.println("创建目录："+dir);
			System.out.println("进入目录："+dir);
			storeFile(remote);
		}else
			System.out.println("存储文件："+remote);
	}
	
	public static void main05(String[] args) {//05
		String path1 = "E:/eOMP/workspace_d5000/tbp-web-app/WebContent/WEB-INF/lib/FastInfoset-1.2.7.jar";
		String path2 = "E:/eOMP/workspace_d5000/tbp-web-app";
		System.out.println(path1.substring(path2.length()+1));
	}
	
	public static void main(String[] args) {//06
		Thread th = new Thread(new BackUpThread());
		th.start();
	}
	
	public static void main07(String[] args) {//07
		File file = new File("E:/eOMP/workspace_d5000/tbp-web-app/fileback/lastBackUpTime");
		FileReader frd = null;
		FileWriter fw = null;
		BufferedReader brd = null;
		try {
			frd = new FileReader(file);
			brd = new BufferedReader(frd);
			System.out.println(brd.readLine());
			frd.close();
			brd.close();
			
			fw = new FileWriter(file);
			//fw.write("124",0,3);
			fw.write("150");
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main08(String[] args) {//08
		Calendar ca = Calendar.getInstance();
		ca.set(2014,11,11,0,0,0);
		System.out.println(ca.getTimeInMillis());
		long tt = 1418261050984l;
		System.out.println(new Date(tt));
	}
	
	public static void main09(String[] args) {//09
		SftpUtil sftp = new SftpUtil("10.36.12.187","root","rocky");
		sftp.getSftpConnection();
		sftp.uploadFile("d:/437.txt","/root/ciwi/abc/437.txt");
		sftp.closeSftpConnection();
		sftp.closeSessionConnection();
	}
	
}
