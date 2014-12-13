package com.ciwi.fileback.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUtil {
	
	private ChannelSftp channelSftp;
	private Session session;
	private String host;
	private String username;
	private String password;
	private String privateKey;//密钥
	private String passphrase;//密钥口令
	private int port = 22;
	private JSch jsch = new JSch();
	
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

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	public Session getSession() {
		return session;
	}
	
	public SftpUtil(String host,String username,String password,String privateKey,String passphrase,int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.port = port;
	}
	
	public SftpUtil(String host,String username,String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	private Session createSession(){
		if(session != null)
			return session;
		try {
			if(privateKey !=null && !"".equals(privateKey)){
				if(passphrase !=null && !"".equals(passphrase))
					jsch.addIdentity(privateKey,passphrase);
				else
					jsch.addIdentity(privateKey);
			}
			session = jsch.getSession(username,host,port);
			} catch (JSchException e) {
				closeSftpConnection();
				e.printStackTrace();
			}
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking","no");
			session.setConfig(config);
			try {
				session.setTimeout(900000);
				session.connect();
			} catch (JSchException e) {
				System.out.println("请检查网络环境是否正确！");
				closeSessionConnection();
				e.printStackTrace();
			}
			return session;
	}
	
	public ChannelSftp getSftpConnection(){
		createSession();
		try {
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		} catch (JSchException e) {
			System.out.println("请检查网络环境是否正确！");
			closeSftpConnection();
			e.printStackTrace();
		}
		return channelSftp;
	}
	
	public void closeSftpConnection(){
		if(channelSftp != null){
			channelSftp.exit();
			channelSftp.disconnect();
			channelSftp = null;
		}
	}
	
	public void closeSessionConnection(){
		if(session != null){
			session.disconnect();
			session = null;
		}
	}
	
	public boolean uploadFile(String srcPath,String desDir){
		boolean isExist = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(srcPath);
			bis = new BufferedInputStream(fis);
			channelSftp.cd("/");//回到根目录
			storeFile(desDir,bis);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SftpException e) {
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
			String dir = null;
			
			if(index == 0)//这个是根目录
				try{
					channelSftp.cd("/");
				}catch (SftpException e) {
					e.printStackTrace();
				}
			else{
				dir = remote.substring(0,index);
				try{
					channelSftp.mkdir(dir);
				} catch (SftpException e) {}//忽略异常
				try {
					channelSftp.cd(dir);
				} catch (SftpException e) {
					e.printStackTrace();
				}
			}
			remote = remote.substring(index+1);
			storeFile(remote,is);
		}else{
			try{
				channelSftp.rm(remote);
			} catch (SftpException e) {}//忽略异常
			try {
				channelSftp.put(is,remote);
			} catch (SftpException e) {
				e.printStackTrace();
			}
		}
	}
	
}
