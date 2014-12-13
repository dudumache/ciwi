package com.ciwi.fileback.main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.tellhow.filemanager.server.ServerSocketPerformer;
import com.tellhow.filemanager.server.util.AppConstants;

public class Server {
	
	  public static final String CMD_ADD = "cmd_add";
	  public static final String CMD_DEL = "cmd_del";
	  public static final String CMD_UPT = "cmd_upt";
	  public static final String CMD_LOAD = "cmd_load";
	  public static final String CMD_EXIST = "cmd_exist";
	  public static final String CMD_COPY = "cmd_copy";
	  public static final String RTN_SUCCESS = "ok";
	  public static final String RTN_DISSUCCESS = "false";
	  public static final String RTN_BUSY = "busy";
	  public static final String RTN_WAIT = "waiting";
	  public static final int PAR_LEN = 1024;
	  public static String strSplite = "&";

	  public static List list = new ArrayList();

	  public static void main(String[] args) throws Exception {
	    int count = 400;
	    if ((AppConstants.SORCKET_COUNT != null) && (!"".equals(AppConstants.SORCKET_COUNT))){
	      count = Integer.parseInt(AppConstants.SORCKET_COUNT);
	    }

	    ServerSocket server = new ServerSocket(Integer.parseInt(AppConstants.SORCKET_PORT),count);
	    Socket socket = null;
	    
	    Thread th = new Thread(new BackUpThread());
		th.start();
		
	    System.out.println("waiting incoming connection...");
	    while (true){
	      socket = server.accept();
	      list.add(socket);

	      new ServerSocketPerformer(socket).execute();
	    }
	  }

}
