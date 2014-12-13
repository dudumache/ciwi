package com.ciwi.fileback.dao;

import com.ciwi.fileback.handl.BackUpConfig;

public interface BackUpDao {
	
	public void connect(BackUpConfig config);
	
	public void upload(String location);
	
	public void disconnect();
	
	public void setRemoteIndex(int remoteIndex);
	
}
