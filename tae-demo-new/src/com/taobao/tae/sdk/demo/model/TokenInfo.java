package com.taobao.tae.sdk.demo.model;

public class TokenInfo {
    public Long    timestamp;
    public String  isvAccountId;
    public Long    openAccountId;
    public Integer loginStateExpireIn;
    public String  uuid;

    public String toJsonString(){
        StringBuilder str = new StringBuilder().append('{');
        appendKV(str, "timestamp", timestamp);
        appendKV(str, "isvAccountId", isvAccountId);
        appendKV(str, "openAccountId", openAccountId);
        appendKV(str, "loginStateExpireIn", loginStateExpireIn);
        appendKV(str, "uuid", uuid);
        str.append('}');
        return str.toString();
    }
    private void appendKV(StringBuilder str, String k, Object v){
        if(v != null){
            if(str.length() > 1){
                str.append(',');
            }
            str.append(k).append(':');
            if(v instanceof CharSequence){
                str.append('\'');
            }
            str.append(v);
            if(v instanceof CharSequence){
                str.append('\'');
            }
        }
    }
}
