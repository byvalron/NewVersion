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

/*   public String username = "root";
   public String password = "root";
   public String hostname = "192.168.1.1";
   public int port = 22;*/

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
           // SettingActivity.getInstance().Log("Tested session successfully, use it again");
            testChannel.disconnect();
        }
        catch (Throwable t) {
            sshChannel = new JSch();

            session = sshChannel.getSession(SettingSingleton.getInstance().username, SettingSingleton.getInstance().hostname, SettingSingleton.getInstance().port);

            session.setPassword(SettingSingleton.getInstance().password);
            session.setConfig("StrictHostKeyChecking", "no");

            //SettingActivity.getInstance().Log("Try connect...");
            session.connect();
            //SettingActivity.getInstance().Log("errConnectionSuccessful");
        }
        return session;
    }

    public String firstConnect(Context context){
       //SettingActivity.getInstance().Log("firstConnect");
        if(isFirstConnect){

            return "";
        }
        isFirstConnect = true;

        return connect(context);
    }

    public String connect(Context context){
        //SettingActivity.getInstance().Log("Start connect");
        String err = "";
        if(isConnecting) {
            //SettingActivity.getInstance().Log("error_connecting_in_process");

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
                //SettingActivity.getInstance().Log(err);
                return err;
            }
            catch (Exception e) {

                err = e.getMessage();
                //SettingActivity.getInstance().Log(err);
                return err;
            }
            finally {
                isConnecting = false;
            }
        }
        else
        {
          //  SettingActivity.getInstance().Log("errUnknownIP");
        }

        isConnecting = false;
        return err;
    }

    public String disConnect(){
        String err = "";
      //  SettingActivity.getInstance().Log("Try disConnect");
        if (sshChannel != null && session!= null && session.isConnected())
        {
           // SettingActivity.getInstance().Log("Set disConnect");
            session.disconnect();
        }

        session = null;
        sshChannel = null;
        return err;
    }

    public String runCommand(String command){
       // SettingActivity.getInstance().Log("Start runCommand");
        String err = "";

        if(isConnecting) {
            //SettingActivity.getInstance().Log("error_connecting_in_process");
            return err;
        }

        if(session == null || !session.isConnected()){
           // SettingActivity.getInstance().Log("errNoConnect");
            return err;
        }

        Channel channel= null;
        try {
            channel = session.openChannel("exec");
        }
        catch (JSchException e) {
            err = e.getMessage();
           // SettingActivity.getInstance().Log(err+"111");
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
                       // SettingActivity.getInstance().Log(new String(tmp, 0, i));
                    }
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    int status = channel.getExitStatus();
                    System.out.println("exit-status: " + status);
                    if(status != 0) {
                        //SettingActivity.getInstance().Log("exit-status: " + status);
                    }
                    break;
                }
                try{
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    //SettingActivity.getInstance().Log(e.getMessage()+"222");
                }
            }
        }
        catch (JSchException e) {

            err = e.getMessage();
           // SettingActivity.getInstance().Log(err+"333");
            return err;
        }
        catch (IOException e) {
            err = e.getMessage();
           // SettingActivity.getInstance().Log(err+"444");
            return err;
        }

        channel.disconnect();
        return err;
    }

    public void Destroy(){
        disConnect();
        //SettingActivity.getInstance().Log("Start disConnect");
    }
}
