package com.isnowfox.core.net.message.coder;

import com.isnowfox.core.junit.BaseTest;
import com.isnowfox.core.net.message.MessageException;
import com.isnowfox.core.net.message.MessageProtocol;
import com.isnowfox.core.net.message.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author zuoge85 on 2015/2/8.
 */
public class CrcEncryptCoderTest extends BaseTest {
    public void test() throws IOException, MessageException {
        assertEquals(Integer.toHexString(CrcEncryptCoder.crc7Check("ABDEFG".getBytes("ascii"))), "55");
        assertEquals(Integer.toHexString(CrcEncryptCoder.crc7Check("a".getBytes("ascii"))), "5f");
        assertEquals(Integer.toHexString(CrcEncryptCoder.crc7Check("a435465gdsfdgdflgjsdfb uv90u4o5j34yfsdafawo de 我打发ad萨嘎德国贰3 忑4他大锅饭的个gsfngrhgfds\\nfdgfadg".getBytes("utf8"))), "71");

//        CrcEncryptCoder coder = new CrcEncryptCoder(16, 0xB);
//        item(coder,"a");
//        item(coder,"a435465gdsfdgdflgjsdfb uv90u4o5j34yfsdafawo de 我打发ad萨嘎德国贰3 忑4他大锅饭的个gsfngrhgfds\\nfdgfadg");
//        item(coder, "a435465gdsfdgdflgjsdfb uv90u4o5j34yfsdafawo de 我打发ad萨嘎德国贰3 忑4他大锅饭的个gsfngrhgfds\\nfdgfadg");
    }

//    protected void item(   CrcEncryptCoder coder, String str) throws IOException, MessageException {
//        PooledByteBufAllocator alloc = new PooledByteBufAllocator(true);
//        ByteBuf outBuf = Unpooled.buffer();
//
//        ByteBuf dataBuf = Unpooled.copiedBuffer(str.getBytes("utf8"));
//
//        Packet msg = new Packet(dataBuf.writerIndex(), (byte) 0, dataBuf);
//        coder.write(outBuf, msg);
//        msg.release();
//
//        int readLen = outBuf.readMedium();
//        int len = outBuf.writerIndex() - MessageProtocol.LENGTH_BYTE_NUMS;
//        assertEquals(readLen, len);
//        Packet p = coder.read(len, outBuf);
//        byte[] readBytes = new byte[p.getLength()];
//        p.getBuf().getBytes(0, readBytes);
//
//        String readStr = new String(readBytes, "utf8");
//        p.release();
//        assertEquals(readStr, str);
//    }
}
