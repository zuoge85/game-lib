package com.isnowfox.io.serialize.tool;

import com.isnowfox.core.net.message.MessageException;
import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.io.serialize.tool.model.*;
import com.isnowfox.io.serialize.tool.model.Package;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 线程不安全
 *
 * @author zuoge85
 */
public class MessageBuilder {
    public static final int MESSAGE_START_TYPE = 1;
    private Config cfg;
    private LogProxy log;
    private Utils utils;

    //private ArrayList<Message> list = new ArrayList<>();
    private MessageAnalyse analyse;
    private int maxType;
    private Map<String, Package> map = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    });
//	private Set<String> asImports = new HashSet<>();

//	private Engine engine = Engine.getEngine();

    public MessageBuilder(Config cfg, LogProxy log) throws Exception {
        this.cfg = cfg;
        this.log = log;
    }

    public void buildServer() throws Exception {
        analyse = new MessageAnalyse(cfg);
        utils = new Utils(map, cfg);

        analyse(new File(cfg.getPath()), null);

        update();

        buildServerFile();
        log.info("处理结束！");
    }

    public void buildClient() throws Exception {
        analyse = new MessageAnalyse(cfg);
        utils = new Utils(map, cfg);

        analyse(new File(cfg.getPath()), null);

        update();

        buildClientFile();
        log.info("处理结束！");
    }

    private void update() {
        //处理pack
//		for (Map.Entry<String, Package> e : map.entrySet()) {
//			for (Message m : e.getValue().getValues()) {
////				m.setJavaPackage(util.getJavaPack(m));
////				m.setAsPackage(util.getAsPack(m));
//			}
//		}
        /**
         * 处理 package 导入
         */

        for (Map.Entry<String, Package> e : map.entrySet()) {
            for (Message m : e.getValue().getValues()) {
                boolean isHasBytes = false;
                boolean isHasByteBuf = false;
                String thisType = utils.getAsPack(m) + "." + m.getName();
                for (Attribute attr : m.getAttributes()) {
                    if (attr.getType() == AttributeType.OTHER) {
                        String type = utils.getAsTypeNoArray(attr);
                        if (!thisType.equals(type)) {
                            m.addAsImport(type);
                        }
                    } else if (attr.getType() == AttributeType.BYTES) {
                        isHasBytes = true;
                    } else if (attr.getType() == AttributeType.BYTE_BUF) {
                        isHasByteBuf = true;
                    }
                }
                if (isHasBytes) {
                    m.addAsImport("com.isnowfox.core.util.ByteArrayUtils");
                }
                if (isHasByteBuf) {
                    m.addJavaImport("io.netty.buffer.ByteBuf");
                }
            }
        }
    }

    private void analyse(File dir, String pack) {
        for (File f : dir.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(Config.FILE_SUFFIX))) {
            if (f.isDirectory()) {
                String pk = pack == null ? f.getName() : pack + "." + f.getName();
                analyse(f, pk);
            } else {
                try (InputStream in = new FileInputStream(f)) {
                    LineIterator it = IOUtils.lineIterator(in, "utf8");
                    Message m = analyse.analyse(it, pack);
                    m.setName(FilenameUtils.getBaseName(f.getName()));
                    add(m);
                    log.info("分析完毕一个Message:%s %s", utils.getAsPack(m), m.getName());
                } catch (IOException e) {
                    throw new RuntimeException("分析文件错误！", e);
                }
            }
        }
    }

    private void add(Message msg) {
        String javaPackage = msg.getPackageName();
        Package p = map.get(javaPackage);
        if (p == null) {
            p = new Package();
            p.setName(javaPackage);

            map.put(javaPackage, p);
        }
        p.add(msg);
    }

    private void buildServerFile() throws Exception {
        //
        buildClientFile(m -> {
            executeJava(m);
            if (m.isServerHandler()) {
                executeJavaHandler(m);
            }
        });
        executeJavaHandlerInterface();
        //开始生成 messageFactory
        executeJavaMessageFactory();
        executeJavaHandlerFactory();

    }

    private void buildClientFile() throws Exception {

        buildClientFile(m -> {
            executeAs(m);
            if (m.isClientHandler()) {
                executeAsHandler(m);
            }
        });
        executeAsMessageFactory();
        executeAsHandlerFactory();
    }


    private void buildClientFile(ExecuteMessage execute) throws Exception {
        int type = 0;
        maxType = 0;
        for (Map.Entry<String, Package> e : map.entrySet()) {
            while (MessageProtocol.EXPAND_MESSAGE_TYPE == type) {
                ++type;
            }
            Package pack = e.getValue();

            if (pack.getName().startsWith("login")) {
                pack.setType(MessageProtocol.LOGIN_MESSAGE_TYPE);
            } else {
                while (MessageProtocol.LOGIN_MESSAGE_TYPE == type) {
                    ++type;
                }
                pack.setType(type);
            }
            maxType = Math.max(type, maxType);
            int id = 0;
            for (Message m : pack.getValues()) {
                try {
                    if (pack.getType() > MessageProtocol.TYPE_OR_ID_MAX) {
                        throw MessageException.newTypeValueRangeException(pack.getType());
                    }
                    if (id > MessageProtocol.TYPE_OR_ID_MAX) {
                        throw MessageException.newIdValueRangeException(id);
                    }
                    m.setType(pack.getType());
                    m.setId(id);
                    execute.exe(m);
                    ++id;
                } catch (Throwable th) {
                    throw new RuntimeException(m.getName() + "严重错误", th);
                }
            }
            ++type;
        }
        maxType = Math.max(MessageProtocol.LOGIN_MESSAGE_TYPE, maxType);
    }

    private void executeJavaHandlerInterface() throws Exception {
        File file = Utils.packToPath(cfg.getJavaSrcPath(), cfg.getJavaHandlerRootPackage(), "MessageHandler", ".java");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("pack", cfg.getJavaHandlerRootPackage());

            HttlUtils.render(getJavaTemplate("javaHandlerInterface.httl"), parameters, out);
            log.info("生成一个java MessageHandler文件%s", file.getName());
        }
    }

    private void executeJavaMessageFactory() throws IOException, ParseException {
        File file = Utils.packToPath(cfg.getJavaSrcPath(), cfg.getJavaRootPackage(), "MessageFactoryImpi", ".java");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("map", map);
            parameters.put("maxType", maxType);
            parameters.put("utils", utils);
            parameters.put("package", cfg.getJavaRootPackage());

            HttlUtils.render(getJavaTemplate("javaFactory.httl"), parameters, out);
            log.info("生成一个java文件%s", file.getName());
        }
    }

    private void executeJavaHandlerFactory() throws Exception {
        File file = Utils.packToPath(cfg.getJavaSrcPath(), cfg.getJavaHandlerRootPackage(), "MessageHandlerFactory", ".java");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("map", map);
            parameters.put("maxType", maxType);
            parameters.put("utils", utils);
            parameters.put("package", cfg.getJavaHandlerRootPackage());

            HttlUtils.render(getJavaTemplate("javaHandlerFactory.httl"), parameters, out);
            log.info("生成JavaHandlerFactory%s", file.getName());
        }
    }


    private void executeJavaHandler(Message m) throws Exception {
        File file = Utils.packToPath(cfg.getJavaSrcPath(), utils.getJavaHandlerPack(m), m.getName(), "Handler.java");
        file.getParentFile().mkdirs();
        if (!file.exists() || cfg.isOverrideHandler()) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("m", m);
                parameters.put("utils", utils);

                HttlUtils.render(getJavaTemplate("javaHandler.httl"), parameters, out);
                log.info("生成一个java handler文件%s", file.getName());
            }
        } else {
            log.info("存在的处理器文件%s,略过", file.getName());
        }
    }

    private void executeJava(Message m) throws IOException, ParseException {
        File file = Utils.packToPath(cfg.getJavaSrcPath(), utils.getJavaPack(m), m.getName(), ".java");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("m", m);
            parameters.put("utils", utils);

            HttlUtils.render(getJavaTemplate("java.httl"), parameters, out);
            log.info("生成一个java文件%s", file.getName());
        }
    }

    private void executeAsMessageFactory() throws IOException, ParseException {
        File file = Utils.packToPath(cfg.getAsSrcPath(), cfg.getAsRootPackage(), "MessageFactoryImpi", ".as");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("map", map);
            parameters.put("maxType", maxType);
            parameters.put("utils", utils);
            parameters.put("package", cfg.getAsRootPackage());

            HttlUtils.render(getAsTemplate("asFactory.httl"), parameters, out);
            log.info("生成一个as文件%s", file.getName());
        }
    }


    private void executeAsHandlerFactory() throws IOException, ParseException {
        File file = Utils.packToPath(cfg.getAsSrcPath(), cfg.getAsHandlerRootPackage(), "MessageHandlerFactory", ".as");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("map", map);
            parameters.put("maxType", maxType);
            parameters.put("utils", utils);
            parameters.put("package", cfg.getAsHandlerRootPackage());

            HttlUtils.render(getAsTemplate("asHandlerFactory.httl"), parameters, out);
            log.info("生成一个AsHandlerFactory%s", file.getName());
        }
    }


    private void executeAsHandler(Message m) throws Exception {
        File file = Utils.packToPath(cfg.getAsSrcPath(), utils.getAsHandlerPack(m), m.getName(), "Handler.as");
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("m", m);
                parameters.put("utils", utils);

                HttlUtils.render(getAsTemplate("asHandler.httl"), parameters, out);
                log.info("生成一个as handler文件%s", file.getName());
            }
        } else {
            log.info("存在的处理器文件%s,略过", file.getName());
        }
    }


    private void executeAs(Message m) throws IOException, ParseException {

        File file = Utils.packToPath(cfg.getAsSrcPath(), utils.getAsPack(m), m.getName(), ".as");
        file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("m", m);
            parameters.put("utils", utils);

            HttlUtils.render(getAsTemplate("as.httl"), parameters, out);
            log.info("生成一个as文件%s", file.getName());
        }
    }

    private String getAsTemplate(String fileName) {
        if (StringUtils.isEmpty(cfg.getAsDirName())) {
            return "/template/" + fileName;
        } else {
            return "/template/" + cfg.getAsDirName() + fileName;
        }
    }

    private String getJavaTemplate(String fileName) {
        if (StringUtils.isEmpty(cfg.getJavaDirName())) {
            return "/template/" + fileName;
        } else {
            return "/template/" + cfg.getJavaDirName() + fileName;
        }
    }
}
