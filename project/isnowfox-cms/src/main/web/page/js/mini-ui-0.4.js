/**
 * ui库
 * 修改成支持jq1.9
 * 放弃对ie678系列的直接支持,意思部分效果在ie下可用,但是体验差
 * 犹豫ie9 10的css3和html支持有限,所以也只是部分支持!
 * @author zuoge85@gmail.com
 * @version 0.4
 **/
;window.mini || (function ($) {
    var doc = $(document),
        win = $(window), body = $(document.body),
        w = mini = {
            transparentImg: "http://dev.openg3.com/images/s.gif",
            version: "0.4",
            isDoc: function (o) {
                return o.length == 1 ? o[0] == document : false;
            },
            isBody: function (o) {
                return o.length == 1 ? o[0] == document.body : false;
            },
            /**
             * 是不是ie6
             **/
            ie6: function () {
                return w.browser.msie && w.browser.version < 7;
            },
            /**
             * 获取元素的高，要处理document
             **/
            height: function (o) {
                if (this.isDoc(o) && this.ie6()) {
                    var scrollHeight = Math.max(
                        document.documentElement.scrollHeight,
                        document.body.scrollHeight
                    ), offsetHeight = Math.max(
                        document.documentElement.offsetHeight,
                        document.body.offsetHeight
                    );
                    if (scrollHeight < offsetHeight) {
                        return win.height();
                    } else {
                        return scrollHeight;
                    }
                } else if (this.isDoc(o)) {
                    return o.height();
                } else {
                    return o.outerHeight(true);
                }
            },
            /**
             * 获取元素的高，要处理document
             **/
            width: function (o) {
                if (this.isDoc(o) && this.ie6()) {
                    var scrollWidth = Math.max(
                        document.documentElement.scrollWidth,
                        document.body.scrollWidth
                    ), offsetWidth = Math.max(
                        document.documentElement.offsetWidth,
                        document.body.offsetWidth
                    );
                    if (scrollWidth < offsetWidth) {
                        return win.width();
                    } else {
                        return scrollWidth;
                    }
                } else if (w.isDoc(o)) {
                    return o.width();
                } else {
                    return o.outerWidth(true);
                }
            },
            animate: {
                show: function (o, t, c) {
                    o.show();
                    if (c)c();
                },
                fadeIn: function (o, t, c) {
                    o.fadeIn(t, c);
                },
                /**
                 * 暂时发现一个ff的渲染错误貌似把overflow:hidden把东西不能完全隐藏起来！
                 * 是否考虑ff不使用这个动画效果
                 **/
                resizeIn: function (o, t, c) {
                    var top = parseInt(o.css("top")), left = parseInt(o.css("left")), w = o.width(), h = o.height();
                    o.height(0).width(0).css({top: top + h / 2, left: left + w / 2});
                    o.show();
                    o.animate({left: left, width: w}, t / 2, function () {
                        o.animate({top: top, height: h}, t, c);
                    });
                }
            }
        };
    /**
     * 显示一个基于覆盖层的loading
     * cfg包含一个参数msg,显示一个消息
     * @param c 配置，这个配置会传递给覆盖层函数
     */
    w.showLoading = function (cfg) {
        var el = (this == $ || this == mini) ? doc : $(this);
        var o = el.overlay(cfg);
        o.overlay.addClass("ui-loading");
        return o;
    }
    function getZIndex(el, def) {
        var zIndex = def;
        for (var p = el; p && (!w.isDoc(p)); p = p.parent()) {
            var z = p.css("zIndex");
            if (z != "auto") {
                zIndex = parseInt(z) + 1;
                break;
            }
        }
        return zIndex;
    }

    /**
     * overlay 覆盖层,配置的单击事件click可以获得一个覆盖层的参数和覆盖方法返回的是一样的对象
     * 返回对象的el表示当前对象，overlay 表示覆盖层对象。
     * @param c 配置 {zIndex:10000,backgroundColor:'#666666',opacity:.2,click:fn,cls:覆盖层class,animate:bool 是否动画,默认是有动画的}
     * @return 覆盖层 {resize:fn,hide:fn,el:jQuery 表示当前对象,overlay:jQuery 表示覆盖层对象}
     **/
    var maxZIndex = 10000;
    w.overlay = (function () {
        var d = {//默认配置
            opacity: .2, backgroundColor: '#666666'
        };
        return function (cfg, cls) {
            var el = (this == $ || this == mini) ? doc : $(this), c = cfg ? cfg : d,
                o = $('<div/>')
                    .hide()
                    .addClass("ui-overlay").addClass(cls),
                zIndex;//=getZIndex(el)
            if (w.isDoc(el)) {
                o.appendTo(document.body)
            } else {
                o.appendTo(el)
            }
            if (cfg && cfg.zIndex) {
                zIndex = cfg.zIndex;
            } else {
                zIndex = Math.max(maxZIndex + 10, getZIndex(el, 0));
                maxZIndex = zIndex;
            }
            /*if(mini.ie6()){
             o.append('<iframe src="javascript:false" style="position:absolute;opacity:0;visibility:inherit; top:0px;left:0px;width:100%;height:200%;z-index:-1;filter=\'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)\';"></iframe>');
             }*/
            o.css({
                position: 'absolute',
                overflow: 'hidden',
                zIndex: zIndex,
                backgroundColor: c.backgroundColor ? c.backgroundColor : d.backgroundColor,
                opacity: 0
            });
            function resize() {
                var doc = w.isDoc(el), top = doc ? 0 : el.position().top, left = doc ? 0 : el.position().left;
                o.css({
                    top: top,
                    left: left,
                    width: w.width(el),
                    height: w.height(el)
                });
            }

            function hide() {
                //释放事件
                resize();
                win.unbind('resize', resize);
                win.unbind('ready', resize);
                if (false === c.animate) {
                    o.hide();
                } else {
                    o.fadeOut(400);
                }
            }

            function del() {
                if (false === c.animate) {
                    o.hide();
                    o.remove();
                } else {
                    o.fadeOut(400, function () {
                        o.remove();
                    });
                }
            }

            function show() {
                resize();
                o.show();
                if (false === c.animate) {
                    o.css({opacity: c.opacity ? c.opacity : d.opacity});
                } else {
                    o.animate({opacity: c.opacity ? c.opacity : d.opacity}, 400);
                }
            }

            resize();
            if (c.cls) {
                o.addClass(c.lcs);
            }
            //
            win.bind("resize.overlay", resize).bind("ready.overlay", resize).bind("load.overlay", resize);
            //覆盖层单击事件
            var obj = {resize: resize, hide: hide, el: el, overlay: o, show: show, del: del, zIndex: zIndex};
            show();
            if (c.click) {
                o.click(function () {
                    c.click(obj);
                });
            }
            return obj;
        }
    })();
    /**
     * 弹出框框，默认样式的框很简单
     * 弹出框
     * 参数 cfg {title:'消息',opacity:.5,width:100,height:80,borderWidth:10,zIndex:11000
	 *		,show:bool 是否直接显示窗口 默认是,overlay:是否显示覆盖层 默认现实,overlayCfg:覆盖层配置,drag:bool是否可以拖动
	 * ,inAnimate: in动画名称,默认是 resize}
     **/
    w.dialog = (function () {
        var d = {//默认配置
            title: '消息', width: 100, height: 120, borderWidth: 6
            , zIndex: 11000, content: '消息', inAnimate: 'resizeIn', inAnimateTime: 200
        };
        return function (cfg) {
            //b是边框对象用于显示边框,d是框正文
            d.zIndex += 100;
            var c = cfg ? cfg : d, el = (this == $ || this == mini) ? null : this, bw = c.borderWidth ? c.borderWidth : d.borderWidth//边框的宽
                , title = (cfg && cfg.title) ? c.title : (el ? el.attr('title') : d.title)
                , content = (cfg && cfg.content) ? c.content : (el ? el : d.content)
                , bc = {
                position: "absolute",
                display: "none", visibility: 'hidden',
                opacity: .3, top: 0, left: 0
            }
                , dc = {position: "absolute", margin: bw, visibility: 'hidden', top: 0, left: 0}
                , b = $('<div class="ui-dialog-border"></div>').appendTo(document.body).css(bc)
                , div = $('<div class="ui-dialog"><div class="ui-dialog-inner">' +
                '<div class="ui-dialog-titlebar"><a class="ui-dialog-titlebar-close" href="javascript:;"><span class="ui-icon-closethick">×</span></a> <span class="ui-dialog-title">'
                + title + '</span>  </div>' +
                '<div  class="ui-dialog-content">' +
                '</div>' + '<div class="ui-dialog-buttons"></div>' +
                '</div></div>').appendTo(document.body).css(dc)
            //正文,和宽高
                , tb = div.find(".ui-dialog-titlebar")
                , tt = div.find(".ui-dialog-title")
                , db = div.find(".ui-dialog-buttons")
                , ct = div.find(".ui-dialog-content")
                , close = div.find(".ui-dialog-titlebar-close")
                , overlay, dci = div.find(".ui-dialog-inner")
                , fn = mini.animate[c.inAnimate ? c.inAnimate : d.inAnimate]
                , oh = tb.outerHeight(true) + ct.outerHeight(true) - ct.height()//其他的高，除了正文部分的高;
                , ow = ct.outerWidth(true) - ct.width();//其他的高，除了正文部分的高;
            //show和定位，先定位外框
            //添加按钮栏
            if (c.buttons) {
                $.each(c.buttons, function (i, sb) {
                    var sb = c.buttons[i], bb = $('<input value="' + sb.text + '" type="button" />');
                    db.append(bb);
                    bb.button();
                    bb.click(function () {
                        if (sb.callback)
                            sb.callback();
                    });
                });
                oh += db.outerHeight();
            } else {
                db.hide();
            }
            /**
             * 宽和高计算错误，重新计算哈
             * iw ih ,内容框的宽和高
             * w  h 正文的宽和高
             **/
            var w = ((c && c.width && c != d) ? c.width : (el ? el.outerWidth(true) + ow : d.width))
                , h = ((c && c.height && c != d) ? c.height : (el ? el.outerHeight(true) + oh : d.height))
                , iw = w - ow
                , ih = h - oh, o = div.add(b);

            function scroll() {
                var left = (win.width() - w) / 2 + doc.scrollLeft()
                    , top = (win.height() - h) * .4 + doc.scrollTop()
                    , left = left < 10 ? 10 : left, top = top < 10 ? 10 : top;
                o.css({width: w, height: h, top: top, left: left});
            }

            ct.append(content);
            //显示方法
            function show() {
                //是否显示doc覆盖层
                //确定位置
                ct.css({width: iw, height: ih});
                if (c.overlay !== false) {//显示覆盖层
                    overlay = mini.overlay(c.overlayCfg);
                    b.css("zIndex", overlay.zIndex + 1);
                    div.css("zIndex", overlay.zIndex + 2);
                }
                var left = (win.width() - w) / 2, top = (win.height() - h) * .4
                    , left = left < 10 ? 10 : left, top = top < 10 ? 10 : top;
                o.css({width: w, height: h, position: 'fixed', top: top, left: left});

                if (c.drag !== false) mini.drag(o, tb);
                o.css({display: 'none', visibility: 'visible'});
                //下面是动画
                if (fn)fn(o, c.inAnimateTime ? c.inAnimateTime : d.inAnimateTime);
                //正文内容
                //ct.empty().append(content);
                if (content) {
                    $(content).show();
                }
            }

            function setTitle(t) {
                tt.html(t);
            }

            function setContent(cid) {
                var c = $(cid);
                var w = ((cfg && cfg.width) ? cfg.width : (c ? c.outerWidth(true) + ow : d.width))
                    , h = ((cfg && cfg.height) ? cfg.height : (c ? c.outerHeight(true) + oh : d.height))
                    , iw = w - ow
                    , ih = h - oh;
                ct.css({width: iw, height: ih});
                o.css({width: w, height: h});
                el = c.clone();
                ct.empty().append(el);
                ct.find("*").show();
                return el;
            }

            function setHtml(h) {
                ct.html(h);
            }

            function hide(bol) {
                //o.hide();//.remove();
                //b.slideUp();
                o.fadeOut(300);
                if ((bol !== false) && c.close) c.close();
                if (overlay) overlay.hide();
                win.unbind('resize', scroll);
                win.unbind('scroll', scroll);

            }

            if (c.close === false) {
                close.hide();
            } else {
                close.click(hide);
            }
            if (false !== c.show)
                show();

            //返回对象，对框的很多操作依靠这个对象
            var obj = {
                el: content,
                content: ct,
                border: b,
                level: div,
                overlay: overlay,
                show: show,
                hide: hide,
                setContent: setContent,
                setTitle: setTitle,
                setHtml: setHtml
            };
            return obj;
        }
    })();
    /**
     * 模拟alert
     * @param msg 消息
     * @param callback 回调
     **/
    w.alert = function (msg, callback) {
        window.focus();
        var d = w.dialog({
            content: msg, width: 300, close: callback
            , buttons: [{
                text: '确定', callback: function () {
                    d.hide()
                }
            }]
        });
    }
    w.confirm = function (msg, callback, cancel) {
        window.focus();
        var d = w.dialog({
            content: msg, width: 250, close: cancel
            , buttons: [{
                text: '确定', callback: function () {
                    d.hide(false);
                    if (callback)callback()
                }
            }
                , {
                    text: '取消', callback: function () {
                        d.hide()
                    }
                }]
        });
    }
    /**
     * 拖动层，增加层的拖动能力,修正边框等造成的定位不很准确,修改拖出可见区域
     * @param obj:jQuery拖动对象，如果不是就尝试使用this
     * @param start:开始拖动使用的那个对象，监听这个事件的事件开始拖动，如果不声明就使用obj
     **/
    w.drag = function (obj, start) {
        var o = obj ? obj : this, st = start ? start : o, s//是否开始
            , mw, mh, zx = o.css("zIndex");
        doc.mousemove(function (e) {
            if (s) {
                //开始拖动
                var fx = "fixed" == o.css("position"), left = (fx ? e.clientX : e.pageX) - mw, top = (fx ? e.clientY : e.pageY) - mh;
                left = left < 0 ? 0 : left;
                top = top < 0 ? 0 : top;
                o.css("left", left).css("top", top);
            }
        });
        st.mousedown(function (e) {
            $(document.body).css("-moz-user-select", "none");
            o.css("-moz-user-select", "none")
                .each(function (i, o) {
                    o.onselectstart = function () {
                        return false
                    };
                });
            var os = o.offset(), fx = "fixed" == o.css("position");
            //修正边框造成的定位不很准确
            mh = (e.pageY) - os.top, mw = (e.pageX) - os.left
                , s = true, o.css("zIndex", 10000 + zx);//超高的层级别了。。。
        });
        doc.mouseup(function (e) {
            o.css("zIndex", zx), s = false, $(document.body).css("-moz-user-select", "auto");
            o.css("-moz-user-select", "auto")
        });
    };
    /**
     * 检查处理png,如果不是ie6就返回
     * @param obj:jQuery拖动对象，如果不是就尝试使用this
     */
    /*
     w.checkPng=function(obj){
     var o=obj?obj:this;
     function img(i){
     i.each(function(){
     var t=$(this),src=t.attr("src");
     t.attr("src",w.transparentImg).css("filter",'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="'+src+'", sizingMethod="scale")');
     });
     }
     if("IMG"==o.attr("tagName").toUpperCase())
     img(o);
     else{
     img(o.find("img"));
     }
     }
     */
    /*
     w.appendImg=function(src,pro){
     var t=$(this);
     var i=$('<img src="'+src+'"/>');
     i.appendTo(t).attr(pro);
     if(w.ie6()){
     var img=new Image();
     img.onload=function(){
     i.height(img.height);i.width(img.width);
     }
     img.src=src;
     }
     return i;
     }*/
    w.button = function (cls, obj) {
        var o = obj ? obj : this, b = $('<span/>'), i = $('<span/>').addClass("text"), cls = (cls == null ? "button" : cls), hCls = cls + "Hover";
        o.after(b).appendTo(i);
        i.appendTo(b);
        b.hover(function () {
            b.addClass(hCls)
        }, function () {
            b.removeClass(hCls)
        }).addClass(cls).addClass("formField");
        return b;
    }
    w.input = function (cls, obj) {
        var o = obj ? obj : this, b = $('<span/>'), i = $('<span/>').addClass("text"), cls = (cls == null ? "input" : cls), hCls = cls + "Focus";
        o.after(b).appendTo(i).addClass('inputText');
        i.appendTo(b);
        o.focus(function () {
            b.addClass(hCls)
        }).blur(function () {
            b.removeClass(hCls)
        });
        b.addClass(cls).addClass("formField");
        return b;
    }
    w.inputFile = function (cls, obj) {
        var o = obj ? obj : this, p = $("<span/>"), ospan = $("<span/>").addClass("inputFileInputSpan"), b = $('<span/>')
            , i = $('<span/>').addClass("text"), cls = (cls == null ? "inputFile" : cls), hCls = cls + "Focus"
            , tCls = cls + "Text", but = $('<input name="selectButton" id="selectButton" value="浏览..." type="button" />');
        o.after(p).addClass('inputFileInput').appendTo(ospan);
        p.append(ospan).append(b).addClass(cls).append(but);
        i.appendTo(b);
        setTimeout(function () {
            b.addClass(tCls).addClass("formField");
            var of = b.offset();
            o.focus(function () {
                p.addClass(hCls)
            }).blur(function () {
                p.removeClass(hCls)
            });
            o.change(function () {
                i.text($(this).val());
            });
            //ospan.css({top:of.top,left:of.left,opacity:1,width:p.outerWidth(true),height:p.outerHeight(true)});
            var button = w.button(null, but);
            //button.html(button.html());
            o.hover(function () {
                button.addClass("buttonHover")
            }, function () {
                button.removeClass("buttonHover")
            });
            ospan.css({top: of.top, left: of.left, opacity: 0, width: p.outerWidth(true), height: p.outerHeight(true)});
            o.hover();
        }, 0);
    }
    w.selectInput = function (cls, obj) {
        var o = obj ? obj : this, cls = (cls == null ? "selectInput" : cls), d = $("<span/>")
            , si = $('<input type="text" style="height:0px; width:0px;"/>').attr("name", o.attr("name")).attr("id", o.attr("id")).val(o.val())
            , text = o.find(":selected").text(), text = $('<span class="text">' + (text == "" ? "请选择" : text) + '</span>')
            , ul = $("<ul/>"), sf = "selectInputFocus";
        o.find("option").each(function (i, o) {
            var op = $(o), li = $("<li/>").text(op.text()).data("value", op.val()).appendTo(ul);
        });
        li = ul.find("li");
        o.after(d).remove();
        d.append(si).append(text).append(ul);
        var zIndex = getZIndex(ul, 1000);
        ul.addClass("selectInputUl").appendTo(doc.find("body")).css("zIndex", zIndex);
        function hide() {
            d.removeClass(sf);
            ul.hide();
            doc.unbind("mousedown", hide);
        }

        function show() {
            setTimeout(function () {
                doc.mousedown(hide);
            }, 1);
            d.addClass(sf);
            var w = d.width(), w2 = ul.width(), of = d.offset();
            if (w2 < w) {
                ul.width(d.outerWidth() - 2);
            }
            ul.css({top: of.top + 22, left: of.left});
            ul.show();
        }

        si.focus(show);
        si.blur(hide);
        text.click(show);
        ul.add(li).mousemove(function () {
            si.unbind("blur");
        });
        d.add(ul).add(li).mouseout(function () {
            si.blur(hide);
        });
        li.mousedown(function () {
            var t = $(this);
            text.text(t.text());
            si.val(t.data("value"));
            hide();
        }).hover(function () {
            $(this).addClass("liHover");
        }, function () {
            $(this).removeClass("liHover");
        });
        d.addClass(cls).addClass("formField");
    }
    /**
     * isAuto是否自动提交
     * fromCfg{isAuto:是否自动提交,showLoadEl,显示load的jq对象,默认是表单自己}
     */
    w.form = function (fromCfg, cfg, cls, obj) {
        var o = obj ? obj : $(this), isAuto = fromCfg && fromCfg.isAuto === false ? false : true,
            cls = (cls == null ? "form" : cls), cfg = (cfg == null) ? {} : cfg,
            showLoadEl = fromCfg && fromCfg.showLoadEl ? fromCfg.showLoadEl : o,
            field = function (o) {
                var la = o.parent(), txt = la[0].firstChild.nodeValue, fi = $("<div/>").addClass("field"), label = $("<div/>").addClass("label").text(txt);
                la.after(fi).remove();
                return fi.append(label).append(o).append("<div class='clear'/>");
            };
        o.addClass("form");
        o.find("input").each(function (i, o) {
            var io = $(o), t = io.attr("type"), id = io.attr("id"), c = cfg[id], c = c == null ? {} : c;
            if (t == "text" || t == "password") {
                var vali = function () {
                    var lable = $(this).parent().parent().parent(), m = lable.find(".message");
                    if (!o.validate()) {
                        m.show();
                        var of = io.position();
                        m.width(m.width());
                        var top = Math.ceil((io.outerHeight(true) - m.outerHeight(true)) / 2.0 + of.top);
                        m.css({top: top, left: of.left + io.parent().outerWidth(true) + 2});
                    } else {
                        m.hide();
                    }
                }
                var fi = field(io);
                io.input(c.cls);
                if (c.validate) {
                    o.validate = c.validate;
                    var lable = io.parent().parent().parent(), m = lable.find(".message");
                    if (m.length == 0) {
                        m = $("<span><img class='ui-dialog-icon-error' src='" + mini.transparentImg + "' />" + (c.validateText ? c.validateText : "输入错误！") + "</span>");
                        m.hide();
                        m.addClass("message");//.find("img").checkPng();
                        lable.find(".clear").before(m);
                        m.css("opacity", 0.5);
                    }
                    io.focus(vali).blur(vali).keyup(vali);
                }
            } else if (t == "button" || t == "submit") {
                io.button(c.cls);
            } else if (t == "file") {
                io.inputFile(c.cls);
            }
        });
        o.find("select").each(function (i, o) {
            var io = $(o), t = io.attr("type"), id = io.attr("id"), c = cfg[id], c = c == null ? {} : c;
            field(io);
            $(o).selectInput(c.cls);
        });
        o.find("textarea").each(function (i, o) {
            var io = $(o), t = io.attr("type"), id = io.attr("id"), c = cfg[id], c = c == null ? {} : c;
            field(io);
            //$(o).selectInput(c.cls);
            var vali = function () {
                var lable = $(this).parent().parent().parent(), m = lable.find(".message");
                if (!o.validate()) {
                    if (c.show) {
                        c.show(lable);
                    } else {
                        m.show(lable);
                        var of = io.position();
                        m.width(m.width());
                        var top = (io.outerHeight() - m.outerHeight(true)) / 2 + of.top;
                        m.css({top: top, left: of.left + io.parent().outerWidth(true) + 2});
                    }
                } else {
                    if (c.hide) {
                        c.hide(lable);
                    } else {
                        m.hide();
                    }

                }
            }
            var fi = field(io);
            if (c.validate) {
                o.validate = c.validate;
                var lable = io.parent().parent().parent(), m = lable.find(".message");
                if (m.length == 0) {
                    m = $("<span><img class='ui-dialog-icon-error' src='" + mini.transparentImg + "' width='12px' height='12px'/><span class='text'>" + (c.validateText ? c.validateText : "输入错误！") + "</span></span>");
                    m.hide();
                    m.addClass("message").find("img").attr("align", "absmiddle");
                    lable.find(".clear").before(m);
                    m.css("opacity", 0.5);
                }
                io.focus(vali).blur(vali).keyup(vali);
            }
        });
        o.find(".inputPanel").each(function (i, o) {
            var io = $(o), t = io.attr("type"), id = io.attr("id"), c = cfg[id], c = c == null ? {} : c;
            field(io);
        });
        o.submit(function () {
            if (isAuto) {
                if (o.validate()) {
                    var load;
                    if ($.isFunction(showLoadEl)) {
                        load = showLoadEl().showLoading();
                    } else {
                        load = showLoadEl.showLoading();
                    }
                    setTimeout(function () {
                        var pd = $.post(o.attr("action"), o.serialize(), function (data) {
                            if (load) {
                                load.del();
                                delete load;
                            }
                            if (fromCfg.success) {
                                fromCfg.success(data);
                            }
                        });
                    }, 1)
                }
                return false;
            } else {
                return o.validate();
            }
        });
        return o;
    }
    w.table = function (cls, obj) {
        var o = obj ? obj : this, cls = (cls == null ? "table" : cls), d = $("<div/>").addClass(cls);
        o.after(d).appendTo(d);
        o.find("tr:nth-child(2n+1)").addClass("interleaving");
        /*
         if($.browser.msie)
         o.find("caption").corner("top 5px");
         if(w.ie6()){
         o.find("tr").hover(function(){
         $(this).addClass("hover");
         },function(){
         $(this).removeClass("hover");
         });
         }*/
    }
    w.validate = function (obj) {
        var o = obj ? obj : this, bool = true;
        o.find(":input").each(function (i, o) {
            if (o.validate && !o.validate()) {
                o.focus();
                bool = false;
                return false;
            }
        });
        return bool;
    }
    w.showDebugDiv = function (x, y, w, h) {
        var div = $("<div/>").appendTo(document.body);
        div.css({
            position: "absolute", zIndex: 100000,
            background: "#333333",
            boder: "boder 1px sold #000",
            width: w, height: h, top: y, left: x
        });
    }
    w.containsPoint = function (x1, y1) {
        var t = $(this), of = t.offset(), x = of.left, y = of.top, w = t.outerWidth(), h = t.outerHeight();
        //mini.showDebugDiv(x,y,w,h);
        if ((w | h) < 0) {
            return false;
        }
        if (x1 < x || y1 < y) {
            return false;
        }
        w += x;
        h += y;
        return ((w < x || w > x1) &&
        (h < y || h > y1));
    }
    w.addFavorite = function (name, url) {
        if (w.browser.msie) {
            window.external.addFavorite(url, name);
        } else if (window.sidebar) {
            window.sidebar.addPanel(name, url, "");
        }
    }
    w.addHome = function (url) {
        if (w.browser.msie) {
            document.body.style.behavior = 'url(#default#homepage)';
            document.body.setHomePage(url);
        } else if (window.sidebar) {
            if (window.netscape) {
                try {
                    netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                } catch (e) {
                    alert("加入首页失败，请在地址栏内输入 about:config,然后将项 signed.applets.codebase_principal_support 值该为true");
                }
            }
            var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
            prefs.setCharPref('browser.startup.homepage', url);
        }
    }
    /**
     *
     */
    w.getParam = (function () {
        var qs = window.location.search;
        var params = {};
        var paramPattern = /([^=^&^?]*)=([^&]*)/g
        if (qs && qs.length > 1) {
            var arr;
            while ((arr = paramPattern.exec(qs)) != null) {
                var ls = params[arr[1]];
                if (!(ls)) {
                    ls = []
                    params[arr[1]] = ls;
                }
                ls.push(decodeURIComponent(arr[2]));
            }
            return function (key, df) {
                var p = params[key];
                if (p && p.length > 0) {
                    return p[0];
                }
                return df;
            }
        }
        return function (key, df) {
            return df;
        }
    })();
    /**
     * 编辑一个元素的内容
     * 忽略前后空格
     * 注意,只能编辑值,而不是其他html
     * cfg:{el}
     */
    w.editField = function (c) {
        var n = "ui-edit-field"
        var el = c.el ? $(c.el) : ((this == $ || this == mini) ? null : $(this));
        el.addClass(n);
        var v = el.text();
        var end = 10;//结束位置附加宽
        var ve = $("<span/>").addClass(n + "-value").text(v);
        var ipe = $("<input/>").addClass(n + "-input").val($.trim(v)).attr("type", "text").hide();
        var edit = false;
        var linke;
        if ("password" == c.type) {
            ipe.val("").attr("type", "password");
        }
        function startEdit() {
            if (!edit) {
                ipe.css("display", "inline-block").width(ve.width() + end).focus().select();
                ve.hide();
                linke.text("保存");
                edit = true;
            }
        }

        function action() {
            if (edit) {
                var isok;
                if (c.callback) {
                    isok = c.callback($.trim(ipe.val()));
                }
                if (isok !== false) {
                    ipe.hide();
                    ve.show();
                    linke.text("修改");
                    edit = false;
                } else {
                    ipe.focus().select();
                }
            } else {
                startEdit();
            }
        }

        linke = $("<a/>").text("修改").attr("href", "javascript:;").click(action);
        if (c.max) {
            ipe.attr("maxlength", c.max);
        }
        el.empty().append(ve).append(ipe).append(linke);
        //保存
        ve.click(startEdit);
        ipe.keyup(function (e) {
            if ("password" == c.type) {
                ve.text(ipe.val().replace(/./g, "●"));
            } else {
                ve.text($.trim(ipe.val()));
            }
            ipe.width(ve.width() + end);
            if (13 == e.keyCode) {
                action();
            }
        });
        ve.hover(function () {
            ve.addClass("hover")
        }, function () {
            ve.removeClass("hover")
        })
    }
    /**
     * 必选  page,pageCount
     * 可选 key,url,goCallback,anchor,showMax,
     * url和 goCallback必选一个
     * goCallback=function(page){
	 *     alert(page)
	 * }
     * 选择url必须选择key
     * key表示页码参数名称
     * cfg:{elpage,pageCount,key,url,goCallback,anchor}
     **/
    //"index.do?${pageContext.request.queryString}"
    w.pageTool = function (cfg) {
        var el = cfg.el ? $(cfg.el) : ((this == $ || this == mini) ? null : $(this));
        if (el == null) return;
        var page = cfg.page;
        var pageCount = cfg.pageCount;
        var showMax = cfg.showMax ? cfg.showMax : 10;
        var key = cfg.key;
        var anchor = cfg.anchor;
        var url = cfg.url;
        var exp = new RegExp("(.*)(" + key + "=[^&^#^=]*)(.*)", "i")//i

        var _make;

        function _url(n) {
            if ((!(el.goCallback)) && url) {
                var arr = url.match(exp);
                if (arr) {
                    return arr[1] + key + "=" + n + arr[3] + "#" + anchor;
                } else {
                    var start = ""
                    if (url.indexOf('?') > -1) {
                        if (url.charAt(url.length - 1) == "?".charAt(0)) {
                            start = url + key + "=";
                        } else {
                            start = url + "&" + key + "=";
                        }
                    } else {
                        start = url + "?" + key + "=";
                    }
                    return start + n + "#" + anchor;
                }
            }
            return "javascript:;";
        }

        //<a href="index.do?page=1#main" class="cur">1</a>

        if (page < 1) {
            page = 1;
        }
        var nums = pageCount;
        var j = page - showMax / 2;

        function _next() {
            _make();
            return false;
        }

        function _prev() {
            j = j - showMax * 2;
            _make();
            return false;
        }

        if (j < 1) {
            j = 1;
        }
        function _page(sb, i) {
            sb.push("<a page='");
            sb.push(i);
            sb.push("' href=\"");
            sb.push(_url(i));
            sb.push("\"");
            if (i == page) {
                sb.push(" class=\"cur\"");
            }
            sb.push(">");
            sb.push(i);
            sb.push("</a>");
        }

        _make = function () {
            var sb = [];
            if (j > 1) {
                _page(sb, 1);
            } else {
                j = 1;
            }
            if (j > 2) {
                sb.push("<a class='prev' href='javascript:;'>&lt;&lt;</a>");
            }
            for (var i = 0; i < showMax && j <= nums; i++) {
                _page(sb, j);
                j++;
            }
            if (j < nums) {
                sb.push("<a class='next' href='javascript:;'>&gt;&gt;</a>");

            }
            if ((j <= nums) && ((page + 1) < nums)) {
                _page(sb, nums);
            }
            //var p=$(".page");
            el.html(sb.join(""));
            el.find(".next").click(_next);
            el.find(".prev").click(_prev);
            if (cfg.goCallback) {
                el.find("a").not(".next,.prev").click(function () {
                    cfg.goCallback($(this).attr("page"));
                    return false;
                })
            }
        }
        _make();
    }
    function uaMatch(ua) {
        ua = ua.toLowerCase();

        var match = /(chrome)[ \/]([\w.]+)/.exec(ua) ||
            /(webkit)[ \/]([\w.]+)/.exec(ua) ||
            /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
            /(msie) ([\w.]+)/.exec(ua) ||
            ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
            [];

        return {
            browser: match[1] || "",
            version: match[2] || "0"
        };
    };

    var matched = uaMatch(navigator.userAgent);
    w.browser = {};
    if (matched.browser) {
        w.browser[matched.browser] = true;
        w.browser.version = matched.version;
    }
    /**
     *注册到 jq
     **/
    jQuery.fn.extend({
        overlay: w.overlay, dialog: w.dialog, alert: w.alert,
        confirm: w.confirm,
        drag: w.drag,
        button: w.button, input: w.input, table: w.table,
        selectInput: w.selectInput, form: w.form, validate: w.validate,
        inputFile: w.inputFile, containsPoint: w.containsPoint,
        showLoading: w.showLoading, pageTool: w.pageTool,
        editField: w.editField
    });
    /**
     *注册到 jq的静态函数
     **/
    $.extend({
        overlay: w.overlay, dialog: w.dialog, alert: w.alert,
        confirm: w.confirm, drag: w.drag, checkPng: w.checkPng,
        addFavorite: w.addFavorite, addHome: w.addHome,
        getParam: w.getParam, pageTool: w.pageTool,
        editField: w.editField
    });
    doc.ajaxError(function (ev, rrr, set, err) {
        if (rrr.status == 200) return true;
        /*
         if(rrr&&rrr.responseText){
         var w=window.open();
         if(w){
         w.document.write(rrr.responseText);
         w.document.close();
         $.alert("请求错误(错误消息页面已经打开)，<br />请联系管理员！状态码："+rrr.status+",url:"+set.url);
         return ;
         }
         }*/
        $.alert("请求错误，请联系管理员！<br />状态码：" + rrr.status + ",url:" + set.url);
    });
    //$.ajaxSettings.cache=false;
})(jQuery);