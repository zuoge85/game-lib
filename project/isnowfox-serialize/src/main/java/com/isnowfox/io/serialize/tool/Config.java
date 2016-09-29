package com.isnowfox.io.serialize.tool;

import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.net.URL;

public class Config {
    public static final String FILE_SUFFIX = ".m";
    private static final String DATA_TOOL_CONFIG_XML = "msgToolConfig.xml";
    private static final XStream xstream = new XStream();

    public static final Config load() {
        File file = new File(DATA_TOOL_CONFIG_XML);
        if (file.exists()) {
            return (Config) xstream.fromXML(file);
        } else {
            Config c = new Config();

            URL u = Config.class.getResource("/msg");
            if (u != null) {
                c.setPath(u.getFile());
            } else {
                File f = new File("msgConfig");
                c.setPath("D:\\tr\\tool\\msgConfig");
            }

            c.setJavaSrcPath("D:\\tr\\code\\server\\project\\zgame-server\\src\\main\\java");
            c.setAsSrcPath("D:\\tr\\code\\client\\project\\zgame-core\\src");
            c.setJavaRootPackage("game.net.message");
            c.setJavaHandlerRootPackage("game.net.handler");
            c.setAsRootPackage("com.isnowfox.net.message");
            c.setAsHandlerRootPackage("com.isnowfox.net.handler");

            return c;
        }
    }

    private String path;
    private String javaSrcPath;
    private String asSrcPath;

    private String javaRootPackage;
    private String asRootPackage;

    private String javaHandlerRootPackage;
    private String asHandlerRootPackage;

    private String javaCharacterPackage;
    private String javaCharacterClassName;

    private String asDirName;
    private String javaDirName;
    private boolean isOverrideHandler = false;


    public void save() throws FileNotFoundException, IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(DATA_TOOL_CONFIG_XML))) {
            xstream.toXML(this, out);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJavaSrcPath() {
        return javaSrcPath;
    }

    public void setJavaSrcPath(String javaSrcPath) {
        this.javaSrcPath = javaSrcPath;
    }

    public String getAsSrcPath() {
        return asSrcPath;
    }

    public void setAsSrcPath(String asSrcPath) {
        this.asSrcPath = asSrcPath;
    }

    public String getJavaRootPackage() {
        return javaRootPackage;
    }

    public void setJavaRootPackage(String javaRootPackage) {
        this.javaRootPackage = javaRootPackage;
    }

    public String getAsRootPackage() {
        return asRootPackage;
    }

    public void setAsRootPackage(String asRootPackage) {
        this.asRootPackage = asRootPackage;
    }

    public final String getJavaHandlerRootPackage() {
        return javaHandlerRootPackage;
    }

    public final void setJavaHandlerRootPackage(String javaHandlerRootPackage) {
        this.javaHandlerRootPackage = javaHandlerRootPackage;
    }

    public final String getAsHandlerRootPackage() {
        return asHandlerRootPackage;
    }

    public final void setAsHandlerRootPackage(String asHandlerRootPackage) {
        this.asHandlerRootPackage = asHandlerRootPackage;
    }

    public String getJavaCharacterPackage() {
        return javaCharacterPackage;
    }

    public void setJavaCharacterPackage(String javaCharacterPackage) {
        this.javaCharacterPackage = javaCharacterPackage;
    }

    public String getJavaCharacterClassName() {
        return javaCharacterClassName;
    }

    public void setJavaCharacterClassName(String javaCharacterClassName) {
        this.javaCharacterClassName = javaCharacterClassName;
    }

    public String getAsDirName() {
        return asDirName;
    }

    public void setAsDirName(String asDirName) {
        this.asDirName = asDirName;
    }

    public String getJavaDirName() {
        return javaDirName;
    }

    public void setJavaDirName(String javaDirName) {
        this.javaDirName = javaDirName;
    }

    public boolean isOverrideHandler() {
        return isOverrideHandler;
    }

    public void setOverrideHandler(boolean overrideHandler) {
        isOverrideHandler = overrideHandler;
    }

    @Override
    public String toString() {
        return "Config [path=" + path + ", javaSrcPath=" + javaSrcPath
                + ", asSrcPath=" + asSrcPath + ", javaRootPackage="
                + javaRootPackage + ", asRootPackage=" + asRootPackage
                + ", javaHandlerRootPackage=" + javaHandlerRootPackage
                + ", asHandlerRootPackage=" + asHandlerRootPackage + "]";
    }
}
