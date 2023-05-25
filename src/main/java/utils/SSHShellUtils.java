package utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: opensource
 * @time: 2021/3/28
 */
public class SSHShellUtils {
    private String host;
    private String username;
    private String password;
    private int port = 22;
    private int timeout = 60 * 60 * 1000;

    public SSHShellUtils(String host, String username, String password, int port, int timeout) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
    }

    public SSHShellUtils(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String execCommand(String cmd) {
        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        try {
            session = jSch.getSession(username, host, port);
            session.setPassword(password);
            session.setTimeout(timeout);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.connect();

            inputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            errInputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));

            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }
            while ((line = errInputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (errInputStreamReader != null) {
                    errInputStreamReader.close();
                }

                if (channelExec != null) {
                    channelExec.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return runLog.toString();
    }
}
