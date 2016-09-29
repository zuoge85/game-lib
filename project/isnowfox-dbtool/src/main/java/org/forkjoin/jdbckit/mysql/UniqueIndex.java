package org.forkjoin.jdbckit.mysql;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.TreeSet;

/**
 * @author zuoge85 on 15/5/12.
 */
public class UniqueIndex {
    private String indexName;
    private TreeSet<Field> fields = new TreeSet<>();

    public UniqueIndex(String indexName) {
        this.indexName = indexName;
    }

    public void addField(String fieldName, int ordinalPosition) {
        Field field = new Field();
        field.fieldName = fieldName;
        field.ordinalPosition = ordinalPosition;
        fields.add(field);
    }

    public String getConstantName() {
        return indexName.toUpperCase();
    }


    public String toFieldsArgs() {
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (sb.length() > 0) {
                sb.append(" ,");
            }
            sb.append('"');
            sb.append(StringEscapeUtils.escapeJava(field.fieldName));
            sb.append('"');
        }
        return sb.toString();
    }

    public String getIndexName() {
        return indexName;
    }

    public TreeSet<Field> getFields() {
        return fields;
    }

    public static class Field implements Comparable<Field> {
        private String fieldName;
        private int ordinalPosition;

        public String getFieldName() {
            return fieldName;
        }

        public int getOrdinalPosition() {
            return ordinalPosition;
        }

        @Override
        public int compareTo(Field o) {
            return Integer.compare(ordinalPosition, o.ordinalPosition);
        }

        @Override
        public String toString() {
            return "Field{" +
                    "fieldName='" + fieldName + '\'' +
                    ", ordinalPosition=" + ordinalPosition +
                    '}';
        }
    }
}
