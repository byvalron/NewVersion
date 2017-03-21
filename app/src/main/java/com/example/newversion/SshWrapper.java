package com.example.newversion;

import android.content.Context;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class SshWrapper {


    public final String TAG = "Test";
    private boolean isFirstConnect = false;
    private boolean isConnecting = false;

    private static SshWrapper   _instance;
    private SshWrapper() { }

    public synchronized static SshWrapper getInstance()
    {
        if (_instance == null)
        {
            _instance = new SshWrapper();
        }
        return _instance;
    }

    private JSch sshChannel = null;
    public Session session = null;

    private Session getSession(Context context) throws Exception {
        try {
            ChannelExec testChannel = (ChannelExec) session.openChannel("exec");
            testChannel.setCommand("true");
            testChannel.connect();
            LogSingleton.getInstance().addToListLog("Tested session successfully, use it again");
            testChannel.disconnect();
        }
        catch (Throwable t) {
            sshChannel = new JSch();

            session = sshChannel.getSession(SettingSingleton.getInstance().username, SettingSingleton.getInstance().hostname, SettingSingleton.getInstance().port);

            session.setPassword(SettingSingleton.getInstance().password);
            session.setConfig("StrictHostKeyChecking", "no");

            LogSingleton.getInstance().addToListLog("Try connect...");
            session.connect();
            LogSingleton.getInstance().addToListLog("errConnectionSuccessful");
        }
        return session;
    }

    public String firstConnect(Context context){
       LogSingleton.getInstance().addToListLog("firstConnect");
        if(isFirstConnect){

            return "";
        }
        isFirstConnect = true;

        return connect(context);
    }

    public String connect(Context context){
        LogSingleton.getInstance().addToListLog("Start connect");
        String err = "";
        if(isConnecting) {
            LogSingleton.getInstance().addToListLog("error_connecting_in_process");

            return err;
        }
        isConnecting = true;

        if (sshChannel != null)
        {
            disConnect();
        }

        if (SettingSingleton.getInstance().hostname != null
                && !SettingSingleton.getInstance().hostname.isEmpty()
                && !SettingSingleton.getInstance().hostname.equals("null"))
        {
            try {
                session = getSession(context);
            }
            catch (JSchException e) {
                err = e.getMessage();
                LogSingleton.getInstance().addToListLog(err);
                return err;
            }
            catch (Exception e) {

                err = e.getMessage();
                LogSingleton.getInstance().addToListLog(err);
                return err;
            }
            finally {
                isConnecting = false;
            }
        }
        else
        {
          LogSingleton.getInstance().addToListLog("errUnknownIP");
        }

        isConnecting = false;
        return err;
    }

    public String disConnect(){
        String err = "";
      LogSingleton.getInstance().addToListLog("Try disConnect");
        if (sshChannel != null && session!= null && session.isConnected())
        {
           LogSingleton.getInstance().addToListLog("Set disConnect");
            session.disconnect();
        }

        session = null;
        sshChannel = null;
        return err;
    }

    public String runCommand(String command){
       LogSingleton.getInstance().addToListLog("Start runCommand");
        String err = "";

        if(isConnecting) {
            LogSingleton.getInstance().addToListLog("error_connecting_in_process");
            return err;
        }

        if(session == null || !session.isConnected()){
           LogSingleton.getInstance().addToListLog("errNoConnect");
            return err;
        }

        Channel channel= null;
        try {
            channel = session.openChannel("exec");
        }
        catch (JSchException e) {
            err = e.getMessage();
           LogSingleton.getInstance().addToListLog(err);
            return err;
        }

        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);

        try {

            {
                channel.connect();
            }

            final InputStream in = channel.getInputStream();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i = in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                    if(i != 0) {
                       LogSingleton.getInstance().addToListLog(new String(tmp, 0, i));
                    }
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    int status = channel.getExitStatus();
                    System.out.println("exit-status: " + status);
                    if(status != 0) {
                        LogSingleton.getInstance().addToListLog("exit-status: " + status);
                    }
                    break;
                }
                try{
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    LogSingleton.getInstance().addToListLog(e.getMessage());
                }
            }
        }
        catch (JSchException e) {

            err = e.getMessage();
           LogSingleton.getInstance().addToListLog(err);
            return err;
        }
        catch (IOException e) {
            err = e.getMessage();
           LogSingleton.getInstance().addToListLog(err);
            return err;
        }

        channel.disconnect();
        return err;
    }

    public void Destroy(){
        disConnect();
        LogSingleton.getInstance().addToListLog("Start disConnect");
    }
}
