package com.ciwi.fileback.dao;

import com.ciwi.fileback.handl.BackUpConfig;
import com.ciwi.fileback.util.FtpUtil;

public class BackUpToFtpService implements BackUpDao {
	
	private BackUpConfig config;
	private int remoteIndex;
	private FtpUtil ftp;
	
	public BackUpToFtpService(BackUpConfig config){
		this.config = config;
	}

	public void connect(BackUpConfig config) {
		this.ftp = new FtpUtil(config.getBackServer_ip(),
				this.config.getBackServer_user(),
				this.config.getBackServer_password(),
				this.config.getFileName_charset(),
				this.config.getBackServer_port());
		this.ftp.createFtpClient();
	}
	
	public void upload(String location){
		this.ftp.uploadFile(location,this.config.getBackServer_path()+location.substring(this.remoteIndex));
	}
	
	public void disconnect(){
		ftp.closeFtpConnection();
	}
	
	public void setRemoteIndex(int remoteIndex){
		this.remoteIndex = remoteIndex;
	}

}
