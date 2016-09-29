package com.isnowfox.web.impl;

import com.isnowfox.web.Config;
import com.isnowfox.web.Response;
import org.apache.commons.io.output.StringBuilderWriter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * 非线程安全,必须按照顺序写数据
 *
 * @author zuoge85@gmail.com
 */
public final class HttpResponse implements Response {
    private static final String SET_COOKIE = "Set-Cookie";
    private static final ChannelBuffer EMPTY_BUFFER = ChannelBuffers.copiedBuffer(new byte[]{});


    private String contentType = "text/html; charset=UTF-8";
    private final Channel channel;
    private StringBuilderWriter writer;
    //	private final Config config;
    private byte[] data;
    private boolean isNotFlush = true;
    private boolean close = false;
    private CookieEncoder cookieEncoder = new CookieEncoder(true);
    private boolean cookie = false;
    private Charset charset;
    //private ChannelHandlerContext chc;

    private DefaultHttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

    /**
     * 是否初始化输出类型,如果是初始化了,那么不能在执行初始化,否则将抛出IllegalStateException
     */
    private boolean isInitOut = false;

    public HttpResponse(Channel channel, ChannelHandlerContext chc, Config config) {
        this.channel = channel;
//		this.config = config;
        //this.chc=chc;
        charset = config.getResponseCharset();
        contentType = "text/html; charset=" + charset.name();
    }

    /**
     * @throws IllegalStateException
     */
    @Override
    public Writer getWriter() {
        if (null != writer) {
            return writer;
        } else if (isInitOut) {
            throw new IllegalStateException("已经使用了其他输出方式!");
        } else {
            isInitOut = true;
            return (writer = new StringBuilderWriter());
        }
    }

    @Override
    public Appendable getAppendable() {
        if (null != writer) {
            return writer.getBuilder();
        } else if (isInitOut) {
            throw new IllegalStateException("已经使用了其他输出方式!");
        } else {
            isInitOut = true;
            return (writer = new StringBuilderWriter()).getBuilder();
        }
    }

    @Override
    public void sendError(int sc) {
        sendError(HttpResponseStatus.valueOf(sc));
    }

    @Override
    public void sendError(int sc, String reasonPhrase) {
        sendError(new HttpResponseStatus(sc, reasonPhrase));
    }

    @Override
    public void sendError(HttpResponseStatus status, String message) {
        sendError(status, message, null);
    }

    @Override
    public void sendError(HttpResponseStatus status, String message,
                          Throwable tw) {
        if (isNotFlush) {
            isNotFlush = false;
            int code = status.getCode();
            ChannelBuffer chBuf = ChannelBuffers.copiedBuffer(String.format("<html>" +
                    "<head><title>%d %s</title></head>" +
                    "<body bgcolor=\"white\">" +
                    "<center><h1>%d %s</h1></center>" +
                    "</body>" +
                    "</html>", code, message, code, status.getReasonPhrase()), charset);
            response.setHeader("Content-Length", chBuf.writerIndex());
            if (cookie) {
                response.addHeader(SET_COOKIE, cookieEncoder.encode());
            }
            response.setContent(chBuf);
            response.setStatus(status);
            channel.write(response);
        }
    }

    @Override
    public void sendError(HttpResponseStatus status) {
        sendError(status, null, null);
    }

    @Override
    public void sendRedirect(String location) {
        if (isNotFlush) {
            isNotFlush = false;
            HttpResponseStatus status = HttpResponseStatus.FOUND;
            ChannelBuffer chBuf = ChannelBuffers.EMPTY_BUFFER;
            response.setHeader("Content-Length", 0);
            response.setHeader("Location", location);
            if (cookie) {
                response.addHeader(SET_COOKIE, cookieEncoder.encode());
            }
            response.setStatus(status);
            response.setContent(chBuf);
            channel.write(response);
        }
    }

    /**
     * 一次性输出,只能输出异常
     *
     * @param data
     * @throws IllegalStateException
     */
    @Override
    public void oneWrite(byte[] data) {
        if (isInitOut) {
            throw new IllegalStateException("已经使用了其他输出方式!");
        } else if (null == this.data) {
            this.data = data;
            isInitOut = true;
        } else {
            throw new IllegalStateException("oneWrite 只能调用一次");
        }
    }

    private void flush() throws IOException {
        if (isNotFlush) {
            isNotFlush = false;

            ChannelBuffer chBuf = null;
            if (contentType != null) {
                response.setHeader("Content-Type", contentType);
            }
            if (null != writer) {
                chBuf = ChannelBuffers.copiedBuffer(writer.getBuilder(), charset);
                response.setHeader("Content-Length", chBuf.writerIndex());
            } else if (null != data) {
                chBuf = ChannelBuffers.copiedBuffer(data);
                response.setHeader("Content-Length", data.length);
            } else {
                chBuf = EMPTY_BUFFER;
                response.setHeader("Content-Length", 0);
            }
            if (cookie) {
                response.addHeader(SET_COOKIE, cookieEncoder.encode());
            }
            response.setContent(chBuf);
            channel.write(response);
        }
    }

    /**
     * 因为是异步在这里关闭channel会有异常,且数据不一定已经发送了.
     */
//	private void close() throws IOException {
////		ChannelFuture cf=channel.getCloseFuture();
////		System.out.format("isCancelled:%b,isDone:%b,isSuccess:%b\n",cf.isCancelled(),cf.isDone(),cf.isSuccess());
////		System.out.format("isBound:%b,isConnected:%b,isOpen:%b,isReadable:%b,isWritable:%b\n", 
////				channel.isBound(),channel.isConnected(),channel.isOpen(),channel.isReadable(),channel.isWritable());
//		//channel.close();
////		chc.setAttachment(new Object());
//		close=true;
//	}
    @Override
    public void flushAndClose() throws IOException {
        // TODO Auto-generated method stub
        close = true;
        flush();
        channel.close();
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public boolean isClose() {
        return close;
    }

    public void addHeader(final String name, final Object value) {
        response.addHeader(name, value);
    }

    public void setHeader(final String name, final Object value) {
        response.setHeader(name, value);
    }

    public void setHeader(final String name, final Iterable<?> values) {
        response.setHeader(name, values);
    }

    public void removeHeader(final String name) {
        response.removeHeader(name);
    }

    public void clearHeaders() {
        response.clearHeaders();
    }

    public String getHeader(final String name) {
        List<String> values = getHeaders(name);
        return values.size() > 0 ? values.get(0) : null;
    }

    public List<String> getHeaders(final String name) {
        return response.getHeaders(name);
    }

    public List<Map.Entry<String, String>> getHeaders() {
        return response.getHeaders();
    }

    public boolean containsHeader(final String name) {
        return response.containsHeader(name);
    }

    public Set<String> getHeaderNames() {
        return response.getHeaderNames();
    }

    public void add(Cookie cookie) {
        cookieEncoder.addCookie(cookie);
        this.cookie = true;
    }

    public void addCookie(String name, String value) {
        cookieEncoder.addCookie(name, value);
        this.cookie = true;
    }

    public Charset getCharset() {
        return charset;
    }
}
