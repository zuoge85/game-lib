package com.isnowfox.oel;

import java.util.Date;

public class TestObject {
    public int intField;
    public String stringField;
    public Date dateField;
    private int intProperty;
    private String StringProperty;
    private Date dateProperty;
    private Date dateMethod;
    public TestObject obj;
    public Object objRef;

    public int getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }

    public String getStringProperty() {
        return StringProperty;
    }

    public void setStringProperty(String stringProperty) {
        StringProperty = stringProperty;
    }

    public Date getDateProperty() {
        return dateProperty;
    }

    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }

    public Date dateMethod() {
        return dateMethod;
    }

    public void dateMethod(Date dateMethod) {
        this.dateMethod = dateMethod;
    }

    @Override
    public String toString() {
        return "TestObject [intField=" + intField + ", StringField="
                + stringField + ", dataField=" + dateField + ", intProperty="
                + intProperty + ", StringProperty=" + StringProperty
                + ", dataProperty=" + dateProperty + "]";
    }
}
