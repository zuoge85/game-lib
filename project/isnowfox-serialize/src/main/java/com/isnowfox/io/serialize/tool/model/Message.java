package com.isnowfox.io.serialize.tool.model;

import java.util.ArrayList;

public final class Message {
    private ArrayList<String> asImports = new ArrayList<>();
    private ArrayList<String> javaImports = new ArrayList<>();
    private String comment;
    private String packageName;
    private String name;
    private int type;
    private int id;
    private Package pack;
    private boolean isClientHandler;
    private boolean isServerHandler;

    public Message() {
        //this.pack = pack;
    }

    private ArrayList<Attribute> attributes = new ArrayList<>();

    public void add(String type, String name, String comment, boolean isArray, int arrayNums) {
        Attribute attr = new Attribute(this);
        AttributeType t = AttributeType.to(type);
        if (t == AttributeType.OTHER) {
            attr.setTypeName(type);
        }
        attr.setType(t);
        attr.setName(name);
        attr.setComment(comment);
        attr.setArray(isArray);
        attr.setArrayNums(arrayNums);
        attributes.add(attr);
    }

    public String getJavaToString() {
//		return "TestAbc [testBool=" + testBool + ", testInt=" + testInt
//		+ ", testDouble=" + testDouble + ", testString=" + testString
//		+ "]";
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(name);
        sb.append(" [");
        for (Attribute attr : attributes) {
            sb.append(attr.getName());
            sb.append("=\" + ");
            sb.append(attr.getName());
            sb.append(" + \"");
            sb.append(',');
        }
        sb.append(" ]\"");
        return sb.toString();
    }

    public String getJavaConstructorString(Utils utils) {
        StringBuilder sb = new StringBuilder();
        for (Attribute attr : attributes) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(utils.getJavaType(attr));
            sb.append(" ");
            sb.append(attr.getName());
        }
        return sb.toString();
    }

    public String getAsConstructorString(Utils utils) {
        StringBuilder sb = new StringBuilder();
        for (Attribute attr : attributes) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(attr.getName());
            sb.append(":");
            sb.append(utils.getAsTypeName(attr));
        }
        return sb.toString();
    }

    public String getLayaConstructorString(Utils utils) {
        StringBuilder sb = new StringBuilder();
        for (Attribute attr : attributes) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(attr.getName());
            sb.append(":");
            sb.append(utils.getLayaTypeName(attr));
        }
        return sb.toString();
    }

    public String getAsToString() {
//		return "TestAbc [testBool=" + testBool + ", testInt=" + testInt
//		+ ", testDouble=" + testDouble + ", testString=" + testString
//		+ "]";
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(name);
        sb.append(" [");
        for (Attribute attr : attributes) {
            sb.append(attr.getName());
            sb.append("=\" + ");
            if (attr.getType() == AttributeType.BYTES) {
                sb.append("ByteArrayUtils.toHexString(");
                sb.append(attr.getAsFieldName());
                sb.append(", 10)");
            } else {
                sb.append(attr.getAsFieldName());
            }
            sb.append(" + \"");
            sb.append(',');
        }
        sb.append(" ]\"");
        return sb.toString();
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void setName(String name) {
        this.name = name;
    }

    protected void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getComment() {
        return comment;
    }


    public String getName() {
        return name;
    }

    public String getFieldName() {
        return Utils.toFieldName(name);
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Package getPack() {
        return pack;
    }

    public void setPack(Package pack) {
        this.pack = pack;
    }

    public ArrayList<String> getAsImports() {
        return asImports;
    }

    public void addAsImport(String item) {
        asImports.add(item);
    }

    public ArrayList<String> getJavaImports() {
        return javaImports;
    }

    public void addJavaImport(String item) {
        javaImports.add(item);
    }

    public final String getPackageName() {
        return packageName;
    }

    public final void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isClientHandler() {
        return isClientHandler;
    }

    public boolean isServerHandler() {
        return isServerHandler;
    }

    public void setServerHandler(boolean isServerHandler) {
        this.isServerHandler = isServerHandler;
    }

    public void setClientHandler(boolean isClientHandler) {
        this.isClientHandler = isClientHandler;
    }

    @Override
    public String toString() {
        return "Message [asImports=" + asImports + ", comment=" + comment
                + ", packageName=" + packageName + ", name=" + name + ", type="
                + type + ", id=" + id + ", attributes=" + attributes + "]";
    }
}
