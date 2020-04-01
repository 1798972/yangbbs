//未点赞 fa-thumbs-o-up //点赞 fa-thumbs-up

//进入时body异步加载点赞数
function selectThumbCount() {
    //加载当前用户的点赞状态
    getThumbStatu();
    var qid = $("#deleteId").text();
    $.ajax({
        url: "/question/thumbCount",
        data: {
            "qid": qid
        },
        type: "post",
        datatype: "text",
        success: function (data) {
            if (data !== -1) {
                $("#thumbNum").text(data + "人点赞");
            } else {
                layer.msg('加载点赞数失败,稍后再试试吧...', {
                    icon: 0,
                    time: 3000
                });
                $("#thumbNum").text("我加载出错了");
            }
        },
        error: function () {
        }
    });
    //加载评论
    app.getInfo();
}

//要加载用户的点赞状态
//用户不同 不能简单的改变样式啊

//加载该问题下该用户的点赞状态
function getThumbStatu() {
    var userThumbFlag = false;
    var thumbTag = $("#thumb");
    //问题id和用户id
    var qid = $("#deleteId").text();
    var uid = $("#sessionUserId").text();

    $.ajax({
        url: "/question/getThumbStatu",
        data: {
            "qid": qid,
            "uid": uid
        },
        type: "post",
        datatype: "text",
        success: function (data) {
            if (data === true) {
                //点赞样式
                thumbTag.removeClass("fa-thumbs-o-up");
                thumbTag.addClass("fa-thumbs-up");
                userThumbFlag = true;
                return userThumbFlag;
            } else {
                //未点赞样式
                thumbTag.removeClass("fa-thumbs-up");
                thumbTag.addClass("fa-thumbs-o-up");
                userThumbFlag = false;
                return userThumbFlag;
            }
        },
        error: function () {
        }
    });
}


//未点赞 fa-thumbs-o-up //点赞 fa-thumbs-up
//点赞/取消点赞
function giveOrCancelThumb() {

    var thumbTag = $("#thumb");
    //问题id和用户id
    var qid = $("#deleteId").text();
    var uid = $("#sessionUserId").text();

    if (uid == null || uid === "") {
        //提示登录
        layer.confirm('还未登录,现在不能进行点赞操作...', {
            btn: ['去登录', '不点了'] //按钮
        }, function () {
            //登录操作
            layer.open({
                type: 2,
                area: ['800px', '600px'],
                fixed: false, //不固定
                maxmin: false,
                content: '/login',
                success: function () {
                }
            });
        });
        return;
    }
    //
    // console(getThumbStatu()+"aaa");
    if (thumbTag.hasClass("fa-thumbs-o-up")) {
        $.ajax({
            url: "/question/giveThumb",
            data: {
                "qid": qid,
                "uid": uid
            },
            type: "post",
            datatype: "text",
            success: function (data) {
                if (data !== -1) {
                    //点赞操作
                    thumbTag.removeClass("fa-thumbs-o-up");
                    thumbTag.addClass("fa-thumbs-up");
                    $("#thumbNum").text(data + "人点赞");
                } else {
                    layer.msg('点赞失败了,稍后再试试吧...', {
                        icon: 0,
                        time: 3000
                    });
                }
            },
            error: function () {
            }
        });


    } else {
        $.ajax({
            url: "/question/cancelThumb",
            data: {
                "qid": qid,
                "uid": uid
            },
            type: "post",
            datatype: "text",
            success: function (data) {
                if (data !== -1) {
                    //取消点赞操作
                    thumbTag.removeClass("fa-thumbs-up");
                    thumbTag.addClass("fa-thumbs-o-up");
                    $("#thumbNum").text(data + "人点赞");
                } else {
                    layer.msg('取消赞失败了,这是天意啊...', {
                        icon: 0,
                        time: 3000
                    });
                }
            },
            error: function () {
            }
        });

    }
}

//点击评论一个问题
function commentOneQuestion() {
    var commentQuestionContent = $("#commentQuestionContent").val();
    var parentId = $("#deleteId").text();
    var commentUserId = $("#sessionUserId").text();

    //本地校验

    if (commentUserId == null || commentUserId === "") {
        layer.confirm('用户未登录,不能发表评论...', {
            btn: ['去登录', '我就随便看看'] //按钮
        }, function () {
            //登录操作
            layer.open({
                type: 2,
                area: ['800px', '600px'],
                fixed: false, //不固定
                maxmin: false,
                content: '/login',
                success: function () {
                }
            });
        });
        return;
    }

    if (commentQuestionContent == null || commentQuestionContent === "") {
        // layer.msg("还没有输入内容呢...", {
        //     icon: 2,
        //     time: 2000
        // });
        layer.msg('还没有输入内容呢...');
        return;
    }

    $.ajax({
        url: "/question/comment",
        data: {
            "parentId": parentId,
            "userId": commentUserId,
            "content": commentQuestionContent,
            "type": 0
        },
        type: "post",
        datatype: "text",
        success: function (data) {
            if (data.state === "success") {
                //评论成功
                layer.open({
                    title: '提示框'
                    , content: '发表成功',
                    time: 1000
                });
                //1s后刷新评论数
                setTimeout(function () {
                    $.ajax({
                        url: "/question/getQuestionCommentCount",
                        data: {
                            "qid": parentId
                        },
                        type: "post",
                        datatype: "text",
                        success: function (data) {
                            //刷新回复数量
                            $("#commentCount").text(data);
                            //刷新评论
                            rows = 5;
                            app.getInfo();
                            //关闭所有的评论详情
                            var contentTag = $("#questionComment").children('div').children(".layui-colla-item").children(".layui-colla-content");
                            if (contentTag.hasClass("layui-show")) {
                                contentTag.removeClass("layui-show");
                            }
                            //刷新子评论数量
                            var countTag = $("#questionComment").children('div:first-child').children('.yang').text("0");
                            // console.log(countTag);
                            //回复框清空
                            $("#commentQuestionContent").val("");

                            //刷新导航栏未读数目
                            $.ajax({
                                url: "/session/getUnReadNumber",
                                data: {
                                },
                                type: "post",
                                datatype: "text",
                                success: function (data) {
                                    $("#unReadNum1").text(data);
                                    $("#unReadNum2").text(data);
                                },
                                error: function () {
                                }
                            });

                        },
                        error: function () {
                        }
                    });

                }, 1);
            } else {
                //评论失败
                layer.msg(data.des, {
                    icon: 2,
                    time: 2000
                });
            }
        },
        error: function () {
        }
    });

}

//加载更多区域的显示样式
function checkLoadTag() {
    var commentCount = $("#commentCount").text();
    var count = $("#questionComment").children('div').length;
    // console.log('A'+commentCount);
    // console.log('B'+count);
    if (count >= commentCount) {
        $("#loadMore").text("没有更多内容了");
    } else {
        $("#loadMore").text("点击加载更多...");
    }
}

//加载更多
function loadMore() {
    //先计算页面上的和总条数是否一样
    var commentCount = $("#commentCount").text();
    // var count = $("#nowCount").text();
    var count = $("#questionComment").children('div').length;
    // console.log('a'+commentCount);
    // console.log('b'+count);

    if (count >= commentCount) {
        $("#loadMore").text("没有更多内容了");
    } else {
        $("#loadMore").text("点击加载更多...");
        app.getInfo();
    }
}

/*二级评论部分*/
function commentOneComment(e) {

    var commentCommentContent = $(e).parent().children('textarea').val();
    var parentId = $(e).parent().children('p').text();
    var commentUserId = $("#sessionUserId").text();

    //本地校验

    if (commentUserId == null || commentUserId === "") {
        layer.confirm('用户未登录,不能发表评论...', {
            btn: ['去登录', '我就随便看看'] //按钮
        }, function () {
            //登录操作
            layer.open({
                type: 2,
                area: ['800px', '600px'],
                fixed: false, //不固定
                maxmin: false,
                content: '/login',
                success: function () {
                }
            });
        });
        return;
    }

    if (commentCommentContent == null || commentCommentContent === "") {
        layer.msg('还没有输入内容呢...');
        return;
    }

    //评论评论
    $.ajax({
        url: "/question/comment",
        data: {
            "parentId": parentId,
            "userId": commentUserId,
            "content": commentCommentContent,
            "type": 1
        },
        type: "post",
        datatype: "text",
        success: function (data) {
            if (data.state === "success") {
                //评论成功
                layer.open({
                    title: '提示框'
                    , content: '发表评论成功',
                    time: 1000
                });
                //1s后刷新评论数
                setTimeout(function () {
                    $.ajax({
                        url: "/question/getCommentCommentCount",
                        data: {
                            "cid": parentId
                        },
                        type: "post",
                        datatype: "text",
                        success: function (data) {
                            $(e).parent().parent().parent().children('.yang')
                                .html(' <i class="fa fa-commenting-o" aria-hidden="true">' + data+ '</i>');
                            //刷新评论
                            secondRows = 5;
                            app.getCommentInfo(e,1);
                            $(e).parent().children('textarea').val('');
                            //刷新导航栏未读数目
                            $.ajax({
                                url: "/session/getUnReadNumber",
                                data: {
                                },
                                type: "post",
                                datatype: "text",
                                success: function (data) {
                                    $("#unReadNum1").text(data);
                                    $("#unReadNum2").text(data);
                                },
                                error: function () {
                                }
                            });

                        },
                        error: function () {
                        }
                    });

                }, 1);
            } else {
                //评论失败
                layer.msg(data.des, {
                    icon: 2,
                    time: 2000
                });
            }
        },
        error: function () {
        }
    });
}

//点击数量的span 打开回复框
function clickResponse(e) {
    //itag是评论展示标签
    var iTag = $(e).next().children(".layui-colla-content");

    if (iTag.hasClass("layui-show")) {
        iTag.removeClass("layui-show");
    } else {
        //加载二级评论
        iTag.addClass("layui-show");
    }
}

//检测加载更多样式
function checkLoadCommentTag(e) {
    //总评论数
    var commentCount = $(e).text();
   //当前显示的评论数
    var nowcount = $(e).next().children("div").children('div').length;
    //加载更多
    var btn = $(e).next().children('div').children(".layui-btn-primary");
    if (nowcount >= commentCount) {
        $(btn).text("没有更多内容了");
    } else {
        $(btn).text("点击加载更多...");
        // app.getCommentInfo(e,2);
    }
}

//点击加载更多评论
function loadCommentMore(e) {
    var commentCount = $(e).parent().parent().prev().text();
    var nowcount = $(e).parent().children('div').length;
    //
    // console.log("1  "+commentCount);
    // console.log("2  "+nowcount);
    if (nowcount >= commentCount) {
        $(e).text("没有更多内容了");
    } else {
        $(e).text("点击加载更多...");
        app.getCommentInfo(e,2);
    }
}

//获得当前评论区域
function getNowComment(cid) {
    console.log("a"+cid);
    //整个评论区域
    var questionComment = $("#questionComment");
    var id2 = questionComment.children('div').children('div').children('div').children('p').text();
    if (cid === id2){

    }
}

