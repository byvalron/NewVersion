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

   public String username = "root";
   public String password = "root";
   public String hostname = "192.168.1.1";
   public int port = 22;

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

    private Session getSession() throws Exception {
        Logger.getInstance().Log("Start getSession");
        try {
            ChannelExec testChannel = (ChannelExec) session.openChannel("exec");
            testChannel.setCommand("true");
            testChannel.connect();
            //Log.d(TAG, "Tested session successfully, use it again");
            Logger.getInstance().Log("Tested session successfully, use it again");
            //LogWrapper.getInstance().addToListLog("Tested session successfully, use it again");
            testChannel.disconnect();
        }
        catch (Throwable t) {

            sshChannel = new JSch();

            session = sshChannel.getSession(username, hostname, port);

            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            Logger.getInstance().Log("Try connect...");
          //  LogWrapper.getInstance().addToListLog("Try connect...");
            session.connect();

          //  String err = context.getResources().getString(R.string.errConnectionSuccessful);
           // LogWrapper.getInstance().addToListLog(err);

        }
        return session;
    }

    public String firstConnect(Context context){
        Logger.getInstance().Log("firstConnect");
        if(isFirstConnect){
            return "";
        }
        isFirstConnect = true;

        return connect(context);
    }

    public String connect(Context context){
        Logger.getInstance().Log("Start connect");
        String err = "";
        if(isConnecting) {
            Logger.getInstance().Log("error_connecting_in_process");
           // err = context.getResources().getString(R.string.error_connecting_in_process);
          //  LogWrapper.getInstance().addToListLog(err);
            return err;
        }
        isConnecting = true;

        if (sshChannel != null)
        {
            disConnect();
        }

        if (hostname != null
                && !hostname.isEmpty()
                && !hostname.equals("null"))
        {
            try {
                session = getSession(/*context*/);
            }
            catch (JSchException e) {
               // err = e.getMessage();
               // LogWrapper.getInstance().addToListLog(err);
                return err;
            }
            catch (Exception e) {

              //  err = e.getMessage();
               // LogWrapper.getInstance().addToListLog(err);
                return err;
            }
            finally {
                isConnecting = false;
            }
        }
        else
        {
            Logger.getInstance().Log("errUnknownIP");
           // err = context.getResources().getString(R.string.errUnknownIP);
           // LogWrapper.getInstance().addToListLog(err);
        }

        isConnecting = false;
        return err;
    }

    public void disConnect(){

        if (sshChannel != null && session!= null && session.isConnected())
        {
            Logger.getInstance().Log("Set disConnect");
           // LogWrapper.getInstance().addToListLog("Set disConnect");
            session.disconnect();
        }

        session = null;
        sshChannel = null;

    }

    public String runCommand(String command){
        Logger.getInstance().Log("Start runCommand");
        String err = "";
/*        if (!InternetConnection.isOnline(mContext)) {
            disConnect();
           // err = mContext.getString(R.string.error_no_connection_internet);
            return err;
        }*/

        if(isConnecting) {
            Logger.getInstance().Log("error_connecting_in_process");
           // err = mContext.getResources().getString(R.string.error_connecting_in_process);
           // LogWrapper.getInstance().addToListLog(err);
            return err;
        }

        if(session == null || !session.isConnected()){
            Logger.getInstance().Log("errNoConnect");
           // err = mContext.getResources().getString(R.string.errNoConnect);

            return err;
        }

        Channel channel= null;
        try {
            channel = session.openChannel("exec");
        }
        catch (JSchException e) {

          //  err = e.getMessage();
           // LogWrapper.getInstance().addToListLog(err);
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
                        Logger.getInstance().Log(new String(tmp, 0, i));
                      //  LogWrapper.getInstance().addToListLog(new String(tmp, 0, i));
                    }
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    int status = channel.getExitStatus();
                    System.out.println("exit-status: " + status);
                    if(status != 0) {
                        Logger.getInstance().Log("exit-status: " + status);
                       // LogWrapper.getInstance().addToListLog("exit-status: " + status);
                    }
                    break;
                }
                try{
                    Thread.sleep(1000);
                }
                catch(Exception e){
                   // LogWrapper.getInstance().addToListLog(e.getMessage());
                }
            }
        }
        catch (JSchException e) {

            err = e.getMessage();
           // LogWrapper.getInstance().addToListLog(err);
            return err;
        }
        catch (IOException e) {

            //err = e.getMessage();
          //  LogWrapper.getInstance().addToListLog(err);
            return err;
        }

        channel.disconnect();
        return err;
    }

    public void Destroy(){
        disConnect();
    }
}
