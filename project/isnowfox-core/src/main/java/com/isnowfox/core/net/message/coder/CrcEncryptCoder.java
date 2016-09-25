package com.isnowfox.core.net.message.coder;

import com.isnowfox.core.io.Input;
import com.isnowfox.core.io.MarkCompressInput;
import com.isnowfox.core.io.ProtocolException;
import com.isnowfox.core.net.message.MessageException;
import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.core.net.message.Packet;
import com.isnowfox.util.ZipUtils;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zuoge85 on 2015/2/8.
 */
public class CrcEncryptCoder {
    protected final static Logger log = LoggerFactory.getLogger(CrcEncryptCoder.class);

    public static final byte[] CRC_7_BYTE = {
            0x00, 0x12, 0x24, 0x36, 0x48, 0x5a, 0x6c, 0x7e,
            0x19, 0x0b, 0x3d, 0x2f, 0x51, 0x43, 0x75, 0x67,
            0x32, 0x20, 0x16, 0x04, 0x7a, 0x68, 0x5e, 0x4c,
            0x2b, 0x39, 0x0f, 0x1d, 0x63, 0x71, 0x47, 0x55,
            0x64, 0x76, 0x40, 0x52, 0x2c, 0x3e, 0x08, 0x1a,
            0x7d, 0x6f, 0x59, 0x4b, 0x35, 0x27, 0x11, 0x03,
            0x56, 0x44, 0x72, 0x60, 0x1e, 0x0c, 0x3a, 0x28,
            0x4f, 0x5d, 0x6b, 0x79, 0x07, 0x15, 0x23, 0x31,
            0x41, 0x53, 0x65, 0x77, 0x09, 0x1b, 0x2d, 0x3f,
            0x58, 0x4a, 0x7c, 0x6e, 0x10, 0x02, 0x34, 0x26,
            0x73, 0x61, 0x57, 0x45, 0x3b, 0x29, 0x1f, 0x0d,
            0x6a, 0x78, 0x4e, 0x5c, 0x22, 0x30, 0x06, 0x14,
            0x25, 0x37, 0x01, 0x13, 0x6d, 0x7f, 0x49, 0x5b,
            0x3c, 0x2e, 0x18, 0x0a, 0x74, 0x66, 0x50, 0x42,
            0x17, 0x05, 0x33, 0x21, 0x5f, 0x4d, 0x7b, 0x69,
            0x0e, 0x1c, 0x2a, 0x38, 0x46, 0x54, 0x62, 0x70
    };
    public static final int ZIP_MASK = 0x80;
    public static final int RANDOM_BIT = 8;

    private final int zipSize;
    private final int initEncryptValue;

    private int readValue;
    private int readCount = 0;
    private int writeValue;
    private int writeCount = 0;

    private final ReentrantLock checkLock = new ReentrantLock();

    public CrcEncryptCoder(int zipSize, int encryptValue) {
        this.zipSize = zipSize;
        this.initEncryptValue = encryptValue;
        readValue = initEncryptValue;
        writeValue = initEncryptValue;
    }


    public Packet read(int len, ByteBuf in) throws IOException, MessageException {
        if (checkLock.tryLock()) {
            try {
                readCount++;
                ByteBuf buf = in.alloc().buffer(len);
                in.readBytes(buf, len);
                //开始加密和crc7
                int crc7 = 0;
                readValue = item(readValue, RANDOM_BIT);
                int dataByte = 0;
                for (int i = 0; i < len - 1; i++) {
                    dataByte = buf.getUnsignedByte(i) ^ readValue;
                    crc7 = crc7Item(dataByte, crc7);

//                    System.out.println("read item" + dataByte + "  " + crc7 + "  " + buf.getByte(i) );
                    buf.setByte(i, dataByte);
                }

//                log.debug(this + "read crc7 {} {} count {} len {}", crc7, readValue, readCount, len);
//                System.out.println("read" + crc7);
                readValue = item(readValue, RANDOM_BIT);
                //结束解密
                dataByte = buf.getUnsignedByte(len - 1);
                dataByte = dataByte ^ readValue;
                boolean isZip = (dataByte & ZIP_MASK) > 0;
                dataByte &= ~ZIP_MASK;
                if (crc7 != dataByte) {
                    throw new RuntimeException("crc7验证失败！");
                }
                Packet p;
                if (isZip) {
                    ByteBuf decBuf = ZipUtils.decompressionDeflater(buf, len - 1);
                    buf.release();
                    p = new Packet(decBuf.writerIndex(), (byte) MessageProtocol.TYPE_NORMAL, decBuf, 0);
                } else {
                    p = new Packet(len - 1, (byte) MessageProtocol.TYPE_NORMAL, buf, 0);
                }
                return p;
            } finally {
                checkLock.unlock();
            }
        } else {
            throw new RuntimeException("错误的！为啥不是线程安全的？");
        }
    }


    public void write(ByteBuf out, Packet msg) throws IOException, MessageException {
        if (checkLock.tryLock()) {
            try {
                writeCount++;
                int len = msg.getLength();
                int offset = 0;
                ByteBuf data;
                boolean isZip;
                if (len >= zipSize) {
                    data = ZipUtils.compressDeflater(msg.getBuf(), msg.getBufOffset(), len);
                    log.debug("压缩率:{}, {}k, {}k", (double) data.readableBytes() / (double) len, data.readableBytes()/1204d , len/1024d);
                    len = data.readableBytes();
                    isZip = true;
                } else {
                    data = msg.getBuf();
                    offset = msg.getBufOffset();
                    isZip = false;
                }
                if (len > MessageProtocol.MESSAGE_MAX) {
                    throw MessageException.newLengthException(len);
                }
                //开始加密和crc7
                int crc7 = 0;
                writeValue = item(writeValue, RANDOM_BIT);

                out.writeMedium(len + 1);
                for (int i = offset; i < len; i++) {
                    int dataByte = data.getByte(i);
                    crc7 = crc7Item(dataByte, crc7);
                    out.writeByte(dataByte ^ writeValue);
                }

                if (isZip) {
                    crc7 |= ZIP_MASK;
                    data.release();
                }

                writeValue = item(writeValue, RANDOM_BIT);
                crc7 ^= writeValue;
                out.writeByte(crc7);
            } finally {
                checkLock.unlock();
            }
        } else {
            throw new RuntimeException("错误的！为啥不是线程安全的？");
        }
    }

    private int item(int seed, int bits) {
        seed = (seed ^ 0x1315D) & 0xFFFFFF;
        seed = (seed * 0x1315D + 0xB) & 0xFFFFFF;
        return seed >>> (24 - bits);
    }


    public static int crc7Check(byte[] by1) {
        int result = 0;
        for (int i = 0; i < by1.length; i++) {
            int data = by1[i];
            if (data < 0) {
                result = CRC_7_BYTE[((256 + data) >>> 1) ^ result];
            } else {
                result = CRC_7_BYTE[(data >>> 1) ^ result];
            }
            int b = data & 0x01;
            if (b == 0) {
                result ^= 0x00;
            } else {
                result ^= 0x09;
            }
            if (result > 127) {
                throw new RuntimeException("result max");
            }
//            System.out.println("i " + result + "  " + i);
        }
        return result;
    }

    public static int crc7Item(int data, int crc7Value) {
        if (data < 0) {
            crc7Value = CRC_7_BYTE[((256 + data) >>> 1) ^ crc7Value];
        } else {
            crc7Value = CRC_7_BYTE[(data >>> 1) ^ crc7Value];
        }
        int b = data & 0x01;
        if (b == 0) {
            crc7Value ^= 0x00;
        } else {
            crc7Value ^= 0x09;
        }
        if (crc7Value > 127) {
            throw new RuntimeException("result max");
        }
        return crc7Value;
    }

}
