package org.forkjoin.core.dao;

public final class QueryParam {
    public enum OperatorType {
        EQ,        // = = 或 eq
        LIKE,
        LIKE_BOTH,
        LT,        //< 或 lt 			小于   ${ 3 < 5 }或 ${ 3 lt 5 }  true
        GT,        // > 或 gt 			大于   ${ 3 > 5 }或 ${ 3 gt 5 }  false
        LE,        //<= 或 le			小于等于 ${ 3 <= 5 }或 ${ 3 le 5 } true
        GE,        //>= 或 ge     		 大于等于        ${ 3 >= 5 }或 ${ 3 ge 5 } false
        NOT,
        IS_NULL,        //is not null
        IS_NOT_NULL
    }

    //logical operator
////	public static type LogicalOperatorType{
//		AND,OR
////		&& 或 and       交集         ${ A && B } 或 ${ A and B } true / false
////		|| 或 or		并集         ${ A || B } 或 ${ A or B } true / false
////		! 或 not		非         ${ !A } 或 ${ not A }     true / false
//	}
    public QueryParam() {

    }

    public QueryParam(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public QueryParam(String key, Object value, OperatorType opt) {
        this.key = key;
        this.value = value;
        this.opt = opt;
    }

    public QueryParam(String key, Object value, OperatorType opt, boolean and) {
        this.key = key;
        this.value = value;
        this.opt = opt;
        this.and = and;
    }

    public QueryParam(String key, Object value, OperatorType opt, boolean and, boolean not) {
        this.key = key;
        this.value = value;
        this.opt = opt;
        this.and = and;
        this.not = not;
    }

    protected OperatorType opt = OperatorType.EQ;
    //protected LogicalOperatorType logicalOpt=LogicalOperatorType.AND;
    protected boolean and = true;
    protected boolean not = false;
    protected String key;
    protected Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public QueryParam not() {
        not = true;
        return this;
    }

    public OperatorType getOpt() {
        return opt;
    }

    public void setOpt(OperatorType opt) {
        this.opt = opt;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public boolean isAnd() {
        return and;
    }

    public void setAnd(boolean and) {
        this.and = and;
    }

    @Override
    public String toString() {
        return "QueryParam [opt=" + opt + ", and=" + and + ", not=" + not
                + ", key=" + key + ", value=" + value + "]";
    }

}
