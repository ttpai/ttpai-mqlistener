package cn.ttpai.mqlistener.core.util;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;

/**
 * @author zhaopeng
 *
 */
public class AppUtil {

    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

    private static final String SPARATOR = "/";

    private AppUtil() {
    }

    /**
     * 输出日志信息
     * 
     * @param msg
     */
    private static void loggerInfo(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public static String getLocalPort() throws MalformedObjectNameException {
        if (ServerUtil.isResin()) {
            loggerInfo("当前为resin服务器");
            return "8080";
        } else if (ServerUtil.isTomcat()) {
            loggerInfo("当前为tomcat服务器");
            return getTomcatLocalPort();
        }
        return "8080";
    }

    /**
     * 获取端口号。tomcat容器可以使用
     * 
     * @return
     * @throws MalformedObjectNameException
     *                                          获取当前机器的端口号
     */
    private static String getTomcatLocalPort() throws MalformedObjectNameException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        return objectNames.iterator().next().getKeyProperty("port");
    }

    /**
     * 获取本地IP地址
     *
     * @throws SocketException
     */
    public static String getLocalIp() throws UnknownHostException, SocketException {
        if (isWindowsOs()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    private static boolean isWindowsOs() {
        boolean isWindowsOs = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOs = true;
        }
        return isWindowsOs;
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                loggerInfo("获取到的linux地址:Address info:" + JSON.toJSONString(intf));
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        loggerInfo("获取到的linux地址： inetAddress:" + inetAddress.getHostAddress());
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                    && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            if (logger.isDebugEnabled()) {
                logger.info("获取ip地址异常", ex);
            }
            ip = "127.0.0.1";
        }
        loggerInfo("获取到的ip地址是：" + ip);
        return ip;
    }

}
