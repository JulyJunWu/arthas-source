package com.taobao.arthas.common;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;

import javax.net.ServerSocketFactory;


/**
 *
 * @author hengyunabc 2018-11-07
 *
 */
public class SocketUtils {

    private SocketUtils() {
    }

    /**
     * 查找是否已经存在监听对应端口的进程,有则返回对应的进程pid,没有的话返回-1
     * @param port
     * @return
     */
    public static long findTcpListenProcess(int port) {
        try {
            if (OSUtils.isWindows()) {
                //  其实就是在操作系统上执行 netstat -ano -p TCP 返回取得结果返回
                String[] command = { "netstat", "-ano", "-p", "TCP" };
                List<String> lines = ExecutingCommand.runNative(command);
                for (String line : lines) {
                    if (line.contains("LISTENING")) {
                        // TCP 0.0.0.0:49168 0.0.0.0:0 LISTENING 476
                        String[] strings = line.trim().split("\\s+");
                        if (strings.length == 5) {
                            //  是否存在监听该端口port的进程
                            if (strings[1].endsWith(":" + port)) {
                                return Long.parseLong(strings[4]);
                            }
                        }
                    }
                }
            }

            if (OSUtils.isLinux() || OSUtils.isMac()) {
                String pid = ExecutingCommand.getFirstAnswer("lsof -t -s TCP:LISTEN -i TCP:" + port);
                if (!pid.trim().isEmpty()) {
                    return Long.parseLong(pid);
                }
            }
        } catch (Throwable e) {
            // ignore
        }

        return -1;
    }

    public static boolean isTcpPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1,
                            InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
