package cn.ttpai.mqlistener.core.util;

import org.apache.log4j.Logger;

/**
 * @ 服务器类型探测
 * 
 * @Date 2011/04/13
 **/
public class ServerUtil {

    public static final String GERONIMOCLASS = "/org/apache/geronimo/system/main/Daemon.class";

    public static final String JBOSSCLASS = "/org/jboss/Main.class";

    public static final String JETTYCLASS = "/org/mortbay/jetty/Server.class";

    public static final String JONASCLASS = "/org/objectweb/jonas/server/Server.class";

    public static final String OC4JCLASS = "/oracle/jsp/oc4jutil/Oc4jUtil.class";

    public static final String ORIONCLASS = "/com/evermind/server/ApplicationServer.class";

    public static final String PRAMATICLASS = "/com/pramati/Server.class";

    public static final String RESINCLASS = "/com/caucho/server/resin/Resin.class";

    public static final String REXIPCLASS = "/com/tcc/Main.class";

    public static final String SUN7CLASS = "/com/iplanet/ias/tools/cli/IasAdminMain.class";

    public static final String SUN8CLASS = "/com/sun/enterprise/cli/framework/CLIMain.class";

    public static final String TOMCATCLASS = "/org/apache/catalina/startup/Bootstrap.class";

    public static final String WEBLOGICCLASS = "/weblogic/Server.class";

    public static final String WEBSPHERECLASS = "/com/ibm/websphere/product/VersionInfo.class";

    public static String getServerId() {
        ServerUtil sd = instance;
        if (sd.serverId == null) {
            if (ServerUtil.isGeronimo()) {
                sd.serverId = "geronimo";
            } else if (ServerUtil.isJboss()) {
                sd.serverId = "jboss";
            } else if (ServerUtil.isJonas()) {
                sd.serverId = "jonas";
            } else if (ServerUtil.isOc4j()) {
                sd.serverId = "oc4j";
            } else if (ServerUtil.isOrion()) {
                sd.serverId = "orion";
            } else if (ServerUtil.isResin()) {
                sd.serverId = "resin";
            } else if (ServerUtil.isWebLogic()) {
                sd.serverId = "weblogic";
            } else if (ServerUtil.isWebSphere()) {
                sd.serverId = "websphere";
            }
            if (ServerUtil.isJetty()) {
                if (sd.serverId == null) {
                    sd.serverId = "jetty";
                } else {
                    sd.serverId += "-jetty";
                }
            } else if (ServerUtil.isTomcat()) {
                if (sd.serverId == null) {
                    sd.serverId = "tomcat";
                } else {
                    sd.serverId += "-tomcat";
                }
            }
            if (log.isInfoEnabled()) {
                log.info("Detected server " + sd.serverId);
            }
            if (sd.serverId == null) {
                throw new RuntimeException("Server is not supported");
            }
        }
        return sd.serverId;
    }

    public static boolean isGeronimo() {
        ServerUtil sd = instance;
        if (sd.geronimo == null) {
            Class c = sd.getClass();
            if (c.getResource(GERONIMOCLASS) != null) {
                sd.geronimo = Boolean.TRUE;
            } else {
                sd.geronimo = Boolean.FALSE;
            }
        }
        return sd.geronimo.booleanValue();
    }

    public static boolean isJboss() {
        ServerUtil sd = instance;
        if (sd.jBoss == null) {
            Class c = sd.getClass();
            if (c.getResource(JBOSSCLASS) != null) {
                sd.jBoss = Boolean.TRUE;
            } else {
                sd.jBoss = Boolean.FALSE;
            }
        }
        return sd.jBoss.booleanValue();
    }

    public static boolean isJetty() {
        ServerUtil sd = instance;
        if (sd.jetty == null) {
            Class c = sd.getClass();
            if (c.getResource(JETTYCLASS) != null) {
                sd.jetty = Boolean.TRUE;
            } else {
                sd.jetty = Boolean.FALSE;
            }
        }
        return sd.jetty.booleanValue();
    }

    public static boolean isJonas() {
        ServerUtil sd = instance;
        if (sd.jonas == null) {
            Class c = sd.getClass();
            if (c.getResource(JONASCLASS) != null) {
                sd.jonas = Boolean.TRUE;
            } else {
                sd.jonas = Boolean.FALSE;
            }
        }
        return sd.jonas.booleanValue();
    }

    public static boolean isOc4j() {
        ServerUtil sd = instance;
        if (sd.oc4j == null) {
            Class c = sd.getClass();
            if (c.getResource(OC4JCLASS) != null) {
                sd.oc4j = Boolean.TRUE;
            } else {
                sd.oc4j = Boolean.FALSE;
            }
        }
        return sd.oc4j.booleanValue();
    }

    public static boolean isOrion() {
        ServerUtil sd = instance;
        if (sd.orion == null) {
            Class c = sd.getClass();
            if (c.getResource(ORIONCLASS) != null) {
                sd.orion = Boolean.TRUE;
            } else {
                sd.orion = Boolean.FALSE;
            }
        }
        return sd.orion.booleanValue();
    }

    public static boolean isPramati() {
        ServerUtil sd = instance;
        if (sd.pramati == null) {
            Class c = sd.getClass();
            if (c.getResource(PRAMATICLASS) != null) {
                sd.pramati = Boolean.TRUE;
            } else {
                sd.pramati = Boolean.FALSE;
            }
        }
        return sd.pramati.booleanValue();
    }

    public static boolean isResin() {
        ServerUtil sd = instance;
        if (sd.resin == null) {
            Class c = sd.getClass();
            if (c.getResource(RESINCLASS) != null) {
                sd.resin = Boolean.TRUE;
            } else {
                sd.resin = Boolean.FALSE;
            }
        }
        return sd.resin.booleanValue();
    }

    public static boolean isRexIp() {
        ServerUtil sd = instance;
        if (sd.rexIp == null) {
            Class c = sd.getClass();
            if (c.getResource(REXIPCLASS) != null) {
                sd.rexIp = Boolean.TRUE;
            } else {
                sd.rexIp = Boolean.FALSE;
            }
        }
        return sd.rexIp.booleanValue();
    }

    public static boolean isSun() {
        if (isSun7() || isSun8()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSun7() {
        ServerUtil sd = instance;
        if (sd.sun7 == null) {
            Class c = sd.getClass();
            if (c.getResource(SUN7CLASS) != null) {
                sd.sun7 = Boolean.TRUE;
            } else {
                sd.sun7 = Boolean.FALSE;
            }
        }
        return sd.sun7.booleanValue();
    }

    public static boolean isSun8() {
        ServerUtil sd = instance;
        if (sd.sun8 == null) {
            Class c = sd.getClass();
            if (c.getResource(SUN8CLASS) != null) {
                sd.sun8 = Boolean.TRUE;
            } else {
                sd.sun8 = Boolean.FALSE;
            }
        }
        return sd.sun8.booleanValue();
    }

    public static boolean isTomcat() {
        ServerUtil sd = instance;
        if (sd.tomcat == null) {
            Class c = sd.getClass();
            if (c.getResource(TOMCATCLASS) != null) {
                sd.tomcat = Boolean.TRUE;
            } else {
                sd.tomcat = Boolean.FALSE;
            }
        }
        return sd.tomcat.booleanValue();
    }

    public static boolean isWebLogic() {
        ServerUtil sd = instance;
        if (sd.webLogic == null) {
            Class c = sd.getClass();
            if (c.getResource(WEBLOGICCLASS) != null) {
                sd.webLogic = Boolean.TRUE;
            } else {
                sd.webLogic = Boolean.FALSE;
            }
        }
        return sd.webLogic.booleanValue();
    }

    public static boolean isWebSphere() {
        ServerUtil sd = instance;
        if (sd.webSphere == null) {
            Class c = sd.getClass();
            if (c.getResource(WEBSPHERECLASS) != null) {
                sd.webSphere = Boolean.TRUE;
            } else {
                sd.webSphere = Boolean.FALSE;
            }
        }
        return sd.webSphere.booleanValue();
    }

    private ServerUtil() {
    }

    private static Logger log = Logger.getLogger(ServerUtil.class);

    private static ServerUtil instance = new ServerUtil();

    private String serverId;

    private Boolean geronimo;

    private Boolean jBoss;

    private Boolean jetty;

    private Boolean jonas;

    private Boolean oc4j;

    private Boolean orion;

    private Boolean pramati;

    private Boolean resin;

    private Boolean rexIp;

    private Boolean sun7;

    private Boolean sun8;

    private Boolean tomcat;

    private Boolean webLogic;

    private Boolean webSphere;

}
