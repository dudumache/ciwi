package com.ciwi.fileback.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author liugs@2013-11-27
 */
public class FtpUtil {
	
	private String host;
	private String username;
	private String password;
	private int port = 21;
	private FTPClient ftpClient;
	private String initDirectoryPath;
	private String charSet;
	
	public FtpUtil(String host,String username,String password,int port,String initDirectoryPath) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.initDirectoryPath = initDirectoryPath;
	}
	
	public FtpUtil(String host,String username,String password,String charSet,int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.charSet = charSet;
	}
	
	public FtpUtil(String host,String username,String password,int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}
	
	public FtpUtil(String host,String username,String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public FtpUtil(String host,String username,String password,String charSet) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.charSet = charSet;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getInitDirectoryPath() {
		return initDirectoryPath;
	}

	public void setInitDirectoryPath(String initDirectoryPath) {
		this.initDirectoryPath = initDirectoryPath;
	}
	
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	
	public void createFtpClient(){
		if(ftpClient == null){
			ftpClient = new FTPClient();
			//ftpClient.setConnectTimeout(30000);
			ftpClient.setDataTimeout(60000);
		}else
			closeFtpConnection();//重新设置了连接信息后需把之前的连接关闭
		
		try {
			ftpClient.connect(host,port);
			ftpClient.login(username,password);
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			if(charSet != null && !"".equals(charSet))
				ftpClient.setControlEncoding(charSet);
			if(initDirectoryPath != null && initDirectoryPath.length() > 0)
				ftpClient.changeWorkingDirectory(initDirectoryPath);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeFtpConnection(){
		if(ftpClient.isConnected())
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public boolean uploadFile(String srcPath,String desDir){
		boolean isExist = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(srcPath);
			bis = new BufferedInputStream(fis);
			ftpClient.changeWorkingDirectory("/");//回到根目录
			storeFile(desDir,bis);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private void storeFile(String remote,InputStream is){
		if(remote.contains("/")){
			int index = remote.indexOf("/");
			String dir = remote.substring(0,index);
			try {
				if(!ftpClient.changeWorkingDirectory(changeCharset(dir))){
					ftpClient.makeDirectory(changeCharset(dir));
					ftpClient.changeWorkingDirectory(changeCharset(dir));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			remote = remote.substring(index+1);
			storeFile(remote,is);
		} else
			try {
				ftpClient.deleteFile(changeCharset(remote));
				ftpClient.storeFile(changeCharset(remote),is);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private String changeCharset(String str){
		String newStr = null;
		try {
			newStr = new String(str.getBytes(),this.charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newStr;
	}
}
