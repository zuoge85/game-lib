package com.isnowfox.web.impl;

import com.isnowfox.util.MimeUtils;
import com.isnowfox.web.Config;
import com.isnowfox.web.Dispatcher;
import com.isnowfox.web.Response;
import com.isnowfox.web.Server;
import com.isnowfox.web.codec.Uri;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

public class DispatcherFactory {
    public static final Dispatcher create(final Config config, final Server server) throws Exception {
        Config.StaticHandleType type = config.getStaticType();
        if (type == Config.StaticHandleType.NONE) {
            return new BaseDispatcher(config, server) {
                @Override
                public boolean disposeStaticFile(Uri uri, Response resp) {
                    return false;
                }
            };
        } else if (type == Config.StaticHandleType.NOT_CACHE) {
            return new BaseDispatcher(config, server) {
                @Override
                public boolean disposeStaticFile(Uri uri, Response resp) throws Exception {
                    return noCacheDisposeStaticFile(config, uri.getPath(), resp);
                }
            };
        }
        return null;
    }

    /**
     * TODO 需要重构和优化
     *
     * @param path
     * @param resp
     * @return 是否找到文件
     * @throws Exception
     */
    private static final boolean noCacheDisposeStaticFile(Config config, String path, Response resp) throws Exception {
        File dir = new File(config.getStaticFilePath());
        File file = new File(dir, path);
        if (file.exists()) {
            if (file.isDirectory()) {
                file = new File(file, "index.html");
                if (!file.exists()) {
                    return false;
                }
            }
            FileInputStream fi = new FileInputStream(file);
            try {
                byte[] data = IOUtils.toByteArray(fi);
                String contentType = MimeUtils.getMimeType(
                        FilenameUtils.getExtension(file.getName()));
                resp.setContentType(contentType);
                //resp.addHeader("Cache-Control", "max-age=7200");//2个小时的缓存
                resp.oneWrite(data);
            } finally {
                fi.close();
            }
            return true;
        } else {
            resp.sendError(404);
        }
        return false;
    }
}
