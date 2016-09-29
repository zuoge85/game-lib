package com.isnowfox.web;

/**
 * 通用缓存类型
 * 这些都是静态的,可以配置的
 * Last-Modified和Etag不包含的原因是不是静态的!
 *
 * @author zuoge85
 */
public enum CacheType {
    EXPIRES,
    /**
     * 如果和EXPIRES同时存在,优先级别比EXPIRES高
     */
    CACHE_CONTROL,
    /**
     * 指示响应可被任何缓存区缓存
     */
    PUBLIC,
    /**
     * 指示对于单个用户的整个或部分响应消息，不能被共享缓存处理。
     * 这允许服务器仅仅描述当用户的部分响应消息，此响应消息对于其他用户的请求无效
     */
    PRIVATE,
    /**
     * 指示请求或响应消息不能缓存（HTTP/1.0用Pragma的no-cache替换）根据什么能被缓存
     */
    NO_CACHE,
    /**
     * 用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存
     */
    NO_STORE,
    /**
     * 指示客户机可以接收生存期不大于指定时间（以秒为单位）的响应。
     */
    MAX_AGE,
    /**
     * 指示客户机可以接收响应时间小于当前时间加上指定时间的响应。
     */
    MIN_FRESH,
    /**
     * 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，
     * 那么客户机可以接收超出超时期指定值之内的响应消息。
     */
    MAX_STALE,
}
