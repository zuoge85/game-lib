package com.isnowfox.util;

import com.isnowfox.core.io.IoUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * @author zuoge85 on 2014/12/8.
 */
public final class ZipUtils {

    /**
     * 前4个字节表示大小
     */
    public static ByteBuf decompressionMap(Blob blob, ByteBufAllocator alloc) throws IOException, SQLException {
        InputStream binaryStream = blob.getBinaryStream();
        int len = IoUtils.getBigEndianInt(binaryStream);
        GzipCompressorInputStream in = new GzipCompressorInputStream(binaryStream);
        ByteBuf zipBuf = alloc.buffer(len);
        zipBuf.writeBytes(in, len);
        return zipBuf;
    }

    public static ByteBuf decompression(ByteBuf byteBuf, int len) throws IOException {
        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(byteBuf, len);
        GzipCompressorInputStream in = new GzipCompressorInputStream(byteBufInputStream);

        ByteBuf zipBuf = byteBuf.alloc().buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(zipBuf);
        IOUtils.copy(in, out);
        return zipBuf;
    }

    public static ByteBuf decompressionDeflater(ByteBuf byteBuf, int len) throws IOException {
        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(byteBuf, len);
        DeflaterInputStream in = new DeflaterInputStream(byteBufInputStream, new Deflater(Deflater.BEST_COMPRESSION, true));

        ByteBuf zipBuf = byteBuf.alloc().buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(zipBuf);
        IOUtils.copy(in, out);
        return zipBuf;
    }

    public static ByteBuf compressDeflater(ByteBuf byteBuf, int bufOffset, int length) throws IOException {
//        BuffBet
        ByteBuf zipBuf = byteBuf.alloc().buffer();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);

        ByteBufOutputStream out = new ByteBufOutputStream(zipBuf);
        DeflaterOutputStream cout = new DeflaterOutputStream(out, deflater);
        byteBuf.getBytes(bufOffset, cout, length);
        cout.finish();
        cout.flush();
        cout.close();

//        int readableBytes = zipBuf.readableBytes();
//        byte[] result = new byte[readableBytes];
//        zipBuf.getBytes(0, result);
//        zipBuf.release();

        return zipBuf;
    }

    public static ByteBuf compress(ByteBuf byteBuf, int bufOffset, int length) throws IOException {
        GzipParameters cParams = new GzipParameters();
        cParams.setCompressionLevel(Deflater.BEST_COMPRESSION);
//        BuffBet
        ByteBuf zipBuf = byteBuf.alloc().buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(zipBuf);
        GzipCompressorOutputStream cout = new GzipCompressorOutputStream(out, cParams);
        byteBuf.getBytes(bufOffset, cout, length);
        cout.finish();
        cout.flush();
        cout.close();

//        int readableBytes = zipBuf.readableBytes();
//        byte[] result = new byte[readableBytes];
//        zipBuf.getBytes(0, result);
//        zipBuf.release();

        return zipBuf;
    }

    public static byte[] compressMapBestSpeed(ByteBuf byteBuf, int length) throws IOException {
        GzipParameters cParams = new GzipParameters();
        cParams.setCompressionLevel(Deflater.BEST_SPEED);
//        BuffBet
        ByteBuf zipBuf = byteBuf.alloc().buffer();
        zipBuf.writeInt(length);
        ByteBufOutputStream out = new ByteBufOutputStream(zipBuf);
        GzipCompressorOutputStream cout = new GzipCompressorOutputStream(out, cParams);
        byteBuf.getBytes(0, cout, length);
        cout.finish();
        cout.flush();
        cout.close();

        int readableBytes = zipBuf.readableBytes();
        byte[] result = new byte[readableBytes];
        zipBuf.getBytes(0, result);
        zipBuf.release();

        return result;
    }
}
