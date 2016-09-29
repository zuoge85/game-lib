package com.isnowfox.io.serialize.tool.model;

public final class Attribute {
    private AttributeType type;
    private String typeName;
    private String name;
    private String comment;
    private boolean isArray;
    private int arrayNums;
    private Message message;

    public Attribute(Message message) {
        this.message = message;
    }

    public String getJavaTypeString() {
        if (type == AttributeType.BYTES || !isArray) {
            return getJavaTypeStringInner();
        } else if (type == AttributeType.OTHER) {
            return "java.util.ArrayList<" + getJavaWrapTypeStringInner() + ">";
        } else {
            return getJavaTypeStringInner() + "[]";
        }
    }

    public String getAsTypeString() {
        if (type == AttributeType.BYTES || !isArray) {
            return getAsTypeStringInner();
        } else {
            return "Vector.<" + getAsTypeStringInner() + ">";
        }
    }

    public String getLayaTypeString() {
        if (type == AttributeType.BYTES || !isArray) {
            return getLayaTypeStringInner();
        } else {
            return "Array";
        }
    }

    public String getAsFieldName() {
        return "m" + Utils.toClassName(name);
    }

    private String getJavaWrapTypeStringInner() {
        switch (type) {
            case BOOLEAN:
                return "Boolean";
            case FLOAT:
                return "Float";
            case DOUBLE:
                return "Double";
            case OTHER:
                return typeName;
            case INT:
                return "Integer";
            case LONG:
                return "Long";
            case STRING:
                return "String";
            case BYTES:
                return "byte[]";
            case BYTE_BUF:
                return "ByteBuf";
            default:
                return null;
        }
    }

    private String getAsTypeStringInner() {
        switch (type) {
            case BOOLEAN:
                return "Boolean";
            case FLOAT:
                return "Number";
            case DOUBLE:
                return "Number";
            case OTHER:
                return typeName;
            case INT:
                return "int";
            case LONG:
                return "Number";
            case STRING:
                return "String";
            case BYTES:
            case BYTE_BUF:
                return "flash.utils.ByteArray";
            default:
                return null;
        }
    }

    private String getLayaTypeStringInner() {
        switch (type) {
            case BOOLEAN:
                return "Boolean";
            case FLOAT:
                return "Number";
            case DOUBLE:
                return "Number";
            case OTHER:
                return typeName;
            case INT:
                return "int";
            case LONG:
                return "Number";
            case STRING:
                return "String";
            case BYTES:
            case BYTE_BUF:
                return "laya.utils.Byte";
            default:
                return null;
        }
    }

    private String getJavaTypeStringInner() {
        switch (type) {
            case BOOLEAN:
                return "boolean";
            case FLOAT:
                return "float";
            case DOUBLE:
                return "double";
            case OTHER:
                return typeName;
            case INT:
                return "int";
            case LONG:
                return "long";
            case STRING:
                return "String";
            case BYTES:
                return "byte[]";
            case BYTE_BUF:
                return "ByteBuf";
            default:
                return null;
        }
    }

    public String getBasEncodeMethod() {
        String method = getBasEncodeMethodInner();
        if (type == AttributeType.BYTES) {
            return method;
        }
        if (isArray) {
            return method + "Array";
        } else {
            return method;
        }
    }

    private final String getBasEncodeMethodInner() {
        switch (type) {
            case BOOLEAN:
                return "writeBoolean";
            case FLOAT:
                return "writeFloat";
            case DOUBLE:
                return "writeDouble";
            case INT:
                return "writeInt";
            case LONG:
                return "writeLong";
            case STRING:
                return "writeString";
            case BYTES:
                return "writeBytes";
            case BYTE_BUF:
                return "writeByteBuf";
            default:
                return null;
        }
    }

    public String getBasDecodeMethod() {
        String method = getBasDecodeMethodInner();
        if (type == AttributeType.BYTES) {
            return method;
        }
        if (isArray) {
            return method + "Array";
        } else {
            return method;
        }
    }

    private final String getBasDecodeMethodInner() {
        switch (type) {
            case BOOLEAN:
                return "readBoolean";
            case FLOAT:
                return "readFloat";
            case DOUBLE:
                return "readDouble";
            case INT:
                return "readInt";
            case LONG:
                return "readLong";
            case STRING:
                return "readString";
            case BYTES:
                return "readBytes";
            case BYTE_BUF:
                return "readByteBuf";
            default:
                return null;
        }
    }

    public boolean isBasType() {
        return type != AttributeType.OTHER;
    }

    public String getJavaAddName() {
        return "add" + Utils.toClassName(name);
    }

    public String getJavaSetName() {
        return "set" + Utils.toClassName(name);
    }

    public String getJavaGetName() {
        return "get" + Utils.toClassName(name);
    }

    protected void setType(AttributeType type) {
        this.type = type;
    }

    protected void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setComment(String comment) {
        this.comment = comment;
    }

    protected void setArray(boolean isArray) {
        this.isArray = isArray;
    }

    protected void setArrayNums(int arrayNums) {
        this.arrayNums = arrayNums;
    }

    public AttributeType getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isByteBuf() {
        return type == AttributeType.BYTE_BUF;
    }

    public int getArrayNums() {
        return arrayNums;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Attribute [type=" + type + ", typeName=" + typeName + ", name="
                + name + ", comment=" + comment + ", isArray=" + isArray
                + ", arrayNums=" + arrayNums + "]";
    }
}
