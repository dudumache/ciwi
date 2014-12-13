package com.ciwi.fileback.dao;

import com.ciwi.fileback.handl.BackUpConfig;
import com.ciwi.fileback.util.SftpUtil;

public class BackUpToSshService implements BackUpDao {

	private BackUpConfig config;
	private int remoteIndex;
	private SftpUtil sftp;
	
	public BackUpToSshService(BackUpConfig config){
		this.config = config;
	}
	
	public void connect(BackUpConfig config) {
		this.sftp = new SftpUtil(config.getBackServer_ip(),
				this.config.getBackServer_user(),
				this.config.getBackServer_password());
		this.sftp.getSftpConnection();
	}
	
	public void upload(String location){//linux环境需要稍加修改路径
		this.sftp.uploadFile(location,"/"+this.config.getBackServer_path()+location.substring(this.remoteIndex));
	}
	
	public void disconnect(){
		this.sftp.closeSftpConnection();
		this.sftp.closeSessionConnection();
	}
	
	public void setRemoteIndex(int remoteIndex){
		this.remoteIndex = remoteIndex;
	}

}
