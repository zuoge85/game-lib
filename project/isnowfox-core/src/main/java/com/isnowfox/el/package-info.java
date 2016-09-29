/**
 * oel object expression language
 * 目的在于实现一个快速简单的oel
 * evaluation
 * 关于实现的思考
 * 1. 单个对象单个属性代理模式
 * 每个对象的每个属性生成一个代理类
 * 然后分解执行
 * 这样的缺点是多次查表,效率降低
 * <p>
 * 2.对每个不同el生成一个代理class,
 * 这样执行快,但是随到el表达式的增加,代理类增加太多!
 * <p>
 * 现在只实现一个模式,就是单个el代理模式
 * <p>
 * 表达式不支持复杂的带参数的函数调用
 *
 * @author zuoge85
 */

package com.isnowfox.el;
