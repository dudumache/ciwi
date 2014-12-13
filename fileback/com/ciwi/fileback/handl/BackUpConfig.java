package com.ciwi.fileback.handl;

public class BackUpConfig {
	
	private String backServer_ip;
	private int backServer_port;
	private String backServer_user;
	private String backServer_password;
	private String backServer_path;
	private long back_repeat;
	private int backServer_protocol;
	public static final int FTP_PROTOCOL = 1,SSH_PROTOCOL = 2;
	private boolean isback = true;
	private String[] location_path;
	private String lastTime_record;
	private String fileName_charset;
	
	public BackUpConfig(String BACKUP_SERVER,String BACKUP_THREAD_REPEAT,String BACKUP_LOCATION_PATH,String BACKUP_LASTTIME_RECORD,String BACKUP_FILENAME_CHARSET){
		analysisStr(BACKUP_SERVER);
		back_repeat = Long.parseLong(BACKUP_THREAD_REPEAT);
		location_path = BACKUP_LOCATION_PATH.split(";");
		lastTime_record = BACKUP_LASTTIME_RECORD;
		fileName_charset = BACKUP_FILENAME_CHARSET;
	}
	
	private void analysisStr(String BACKUP_SERVER){
		//协议ftp://或者ssh://开始搜索
		if(BACKUP_SERVER.startsWith("ftp"))
			backServer_protocol = FTP_PROTOCOL;
		else if(BACKUP_SERVER.startsWith("ssh"))
			backServer_protocol = SSH_PROTOCOL;
		
		//登录用户名
		int index1 = BACKUP_SERVER.indexOf(":",6);
		backServer_user = BACKUP_SERVER.substring(6,index1);
		//密码
		int index2 = BACKUP_SERVER.indexOf("@");
		backServer_password = BACKUP_SERVER.substring(index1+1,index2);
		//备份服务器IP
		int index3 = BACKUP_SERVER.indexOf(":",index2);
		backServer_ip = BACKUP_SERVER.substring(index2+1,index3);
		//服务端口
		int index4 = BACKUP_SERVER.indexOf("/",index3);
		backServer_port = Integer.parseInt(BACKUP_SERVER.substring(index3+1,index4));
		//存储目录
		int index5 = BACKUP_SERVER.indexOf("/",index4);
		backServer_path = BACKUP_SERVER.substring(index5+1);
	}
	
	public String getBackServer_ip() {
		return backServer_ip;
	}
	public int getBackServer_port() {
		return backServer_port;
	}
	public String getBackServer_user() {
		return backServer_user;
	}
	public String getBackServer_password() {
		return backServer_password;
	}
	public String getBackServer_path() {
		return backServer_path;
	}
	public long getBack_repeat() {
		return back_repeat;
	}
	public long getBackServer_protocol() {
		return backServer_protocol;
	}
	public boolean isback() {
		return isback;
	}
	public String[] getLocation_path() {
		return location_path;
	}
	public String getLastTime_record() {
		return lastTime_record;
	}
	public String getFileName_charset() {
		return fileName_charset;
	}
}
