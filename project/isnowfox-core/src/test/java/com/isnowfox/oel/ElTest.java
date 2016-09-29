package com.isnowfox.oel;

import com.isnowfox.core.junit.BaseTest;
import com.isnowfox.el.ElEngine;
import com.isnowfox.el.UnknownKeyException;
import com.isnowfox.el.exp.LongExpression;
import com.isnowfox.util.RandomUtils;
import com.isnowfox.util.TimeSpan;

import java.util.Date;

/**
 * el 测试用例
 *
 * @author zuoge85
 */
public class ElTest extends BaseTest {
    private static final int NUMS = 100000000;

    private TestObject createObj() {

        TestObject obj = new TestObject();
        Date date = new Date();
        int number = RandomUtils.randInt();
        String string = "测试用例";

        obj.dateField = date;
        obj.setDateProperty(date);

        obj.intField = number;
        obj.setIntProperty(number);

        obj.stringField = string;
        obj.setStringProperty(string);

        obj.dateMethod(date);
        return obj;
    }

    public void testBasObject() throws UnknownKeyException {
        ElEngine oel = ElEngine.getInstance();
        TestObject obj = createObj();

        Date date = obj.dateField;
        int number = obj.intField;
        String string = obj.stringField;

        assertEquals(date, oel.el(obj, "dateProperty"));
        assertEquals(date.getTime(), oel.<Long>el(obj, "dateProperty.time").longValue());
        assertEquals(date, oel.el(obj, "dateField"));
        assertEquals(date.getTime(), oel.<Long>el(obj, "dateField.time").longValue());
        assertEquals(date, oel.el(obj, "dateMethod()"));
        assertEquals(date.getTime(), oel.<Long>el(obj, "dateMethod().time").longValue());

        assertEquals(number, oel.<Integer>el(obj, "intField").intValue());
        assertEquals(number, oel.<Integer>el(obj, "intProperty").intValue());

        assertEquals(string, oel.el(obj, "stringField"));
        assertEquals(string, oel.el(obj, "stringProperty"));
    }

    public void testObj() throws UnknownKeyException {
        ElEngine oel = ElEngine.getInstance();
        TestObject obj = createObj();

        Date date = obj.dateField;
        int number = obj.intField;
        String string = obj.stringField;
        obj.objRef = date;

        assertEquals(date.getTime(), oel.<Long>el(obj, "objRef.time").longValue());


        assertEquals(date.getTime(), oel.<Long>el(obj, "objRef.time").longValue());
    }

    public void testTime() throws UnknownKeyException, InterruptedException {
        TestObject obj = createObj();
        obj.obj = obj;
        ElEngine oel = ElEngine.getInstance();
        TimeSpan ts = new TimeSpan();

        Object o = oel.el(obj, "obj.obj.obj.obj.obj.dateMethod().time");
        System.gc();

        ts.start();
        System.out.println("---------------------");
        //Object longElObj = oel.compile(obj, "obj.obj.obj.obj.obj.dateMethod().time");
        LongExpression el = oel.<Long, LongExpression>compile(obj.getClass(), "obj.obj.obj.obj.obj.dateMethod().time");
        for (int i = 0; i < NUMS; i++) {
            Long l = oel.<Long>el(obj, "obj.obj.obj.obj.obj.dateMethod().time");
            //long l = el.longEl(obj);
            //Long l = el.el(obj);
        }
        System.out.println(ts.end());
        ts.start();

        for (int i = 0; i < NUMS; i++) {
            Long l = obj.obj.obj.obj.obj.dateMethod().getTime();
        }
        System.out.println(ts.end());
    }
}
