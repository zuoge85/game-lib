package com.isnowfox.web.adapter;

import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayOutputStream;

import com.isnowfox.web.*;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isnowfox.util.TimeSpan;
import com.isnowfox.web.codec.Uri;

public class WebAdapter extends SimpleChannelUpstreamHandler {
	private static final Logger log = LoggerFactory.getLogger(WebAdapter.class);
	private Dispatcher dispatcher;
	private boolean readingChunks;
	private Config config;

	public WebAdapter(Config config, Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.config = config;
	}

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	@Override
	public void messageReceived(final ChannelHandlerContext chc, final MessageEvent evt) throws Exception {
		if (!readingChunks) {
			HttpRequest request = (HttpRequest) evt.getMessage();

			if (is100ContinueExpected(request)) {
				send100Continue(evt);
			}

			if (request.isChunked()) {
				readingChunks = true;
			} else {
				ChannelBuffer content = request.getContent();
				if (content.readable()) {
					baos.write(content.array());
				}
				service(chc, evt);
			}
		} else {
			HttpChunk chunk = (HttpChunk) evt.getMessage();
			if (chunk.isLast()) {
				service(chc, evt);
			} else {
				ChannelBuffer content = chunk.getContent();
				baos.write(content.array());
			}
		}
	}

	private final void service(final ChannelHandlerContext chc, final MessageEvent evt) throws Exception {
		byte[] postData = baos.toByteArray();// post数据
		baos.reset();
		//
		// URLCodec
		HttpRequest request = (HttpRequest) evt.getMessage();
		TimeSpan ts = new TimeSpan();
		Uri uri = new Uri(request.getUri(), config.getRequestCharset(), config.getParamsMax(), config.getIndexPage());
		if (log.isDebugEnabled()) {
			log.debug("处理请求[method:{},uri:{}]", request.getMethod(), uri);
		}
		Response resp = new com.isnowfox.web.impl.HttpResponse(evt.getChannel(), chc, config);
		Request req = com.isnowfox.web.impl.HttpRequest.create(request, postData, evt.getChannel(), config.getRequestCharset());

		Context.getInstance().setRequest(req);
		try{
			dispatcher.service(uri, req, resp);
			if (log.isDebugEnabled()) {
				log.debug("请求处理结束,耗时[{}]", ts.end());
			}
		}finally {
			Context.getInstance().clearRequest();
		}

		// dispatcher.service(chc, evt, b);
		// WebServlet.service(this, chc, evt, b);
	}

	private void send100Continue(MessageEvent me) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		me.getChannel().write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext chc, ExceptionEvent e) throws Exception {
		// WebServlet.event(chc);
		// e.getCause().printStackTrace();
		log.error("Channel错误", e.getCause());
		e.getChannel().close();
		//
	}

	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
		super.writeComplete(ctx, e);
		Object o = ctx.getAttachment();
		if (o instanceof com.isnowfox.web.impl.HttpResponse) {
			com.isnowfox.web.impl.HttpResponse resp = (com.isnowfox.web.impl.HttpResponse) o;
			if (resp.isClose()) {
				log.debug("writeComplete close");
				// ctx.getChannel().close();
			}
		}
		log.info("writeComplete");
		// Channel channel=e.getChannel();
		// ChannelFuture cf=channel.getCloseFuture();
		// System.out.format("isCancelled:%b,isDone:%b,isSuccess:%b\n",cf.isCancelled(),cf.isDone(),cf.isSuccess());
		// System.out.format("isBound:%b,isConnected:%b,isOpen:%b,isReadable:%b,isWritable:%b\n",
		// channel.isBound(),channel.isConnected(),channel.isOpen(),channel.isReadable(),channel.isWritable());
		// //channel.close();
		// ctx.getChannel().close();

	}

	@Override
	public void channelDisconnected(ChannelHandlerContext chc, ChannelStateEvent e) throws Exception {
		super.channelDisconnected(chc, e);
		// WebServlet.event(chc);
		log.info("channelDisconnected");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		log.info("channelConnected");
	}

	// private final String getPath(String uri) {
	// int pathEndPos = uri.indexOf('?');
	// if (pathEndPos < 0) {
	// return uri;
	// } else {
	// return uri.substring(0, pathEndPos);
	// }
	// }

}
