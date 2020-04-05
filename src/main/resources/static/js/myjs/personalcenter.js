//更改个人资料 点击事件
function changeInformation() {

    var userId = $("#userIdSpan").text();
    //检查用户类型 QQ/GitHub用户头像实时更新 无需更改
    $.ajax({
        url: "/getUserType",
        data: {
            id: userId
        },
        type: "post",
        datatype: "text",
        success: function (data) {
            if (data === 0) {

                //页面操作
                $("#changeInformationBtn").hide();
                var preDiv = $("#preDiv");
                var nextDiv = $("#nextDiv");
                preDiv.removeClass('col-lg-12');
                preDiv.addClass('col-lg-6');
                nextDiv.show();

            } else {

                //无需更改资料
               /* layer.open({
                    type: 1,
                    shade: false,
                    title: false, //不显示标题
                    content: $('.layer_notice'), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
                    //取消后的操作
                    // cancel: function(){
                    //     layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', {time: 5000, icon:6});
                    // }
                });*/
                layer.msg('您采用第三方登录,无需修改信息,信息会自动同步.');
            }
        },
        error: function () {
        }
    });
}

//右侧的取消事件
function cancelChange() {
    $("#changeInformationBtn").show();
    var preDiv = $("#preDiv");
    var nextDiv = $("#nextDiv");
    preDiv.removeClass('col-lg-6');
    preDiv.addClass('col-lg-12');
    nextDiv.hide();
}

//用户名称离焦事件
function checkNewName() {
    var newNickname = $("#newNickname").val();
    //昵称格式检验
    if (newNickname === null || newNickname === "") {
        layer.msg('昵称不能为空!', function () {
        });
        return false;
    }
    if (newNickname.length > 8) {
        layer.msg('昵称格式不正确!', function () {
        });
        return false;
    }
    // layer.msg('这个昵称很棒!');
    return true;
}

//更改用户信息 提交事件
function submitChangeInfo() {
    var newAvaUrl = $("#newAvatarUrl").text();
    var newNickname = $("#newNickname").val();
    var userId = $("#userIdSpan").text();

    if (checkNewName()){
        //更改用户信息
        $.ajax({
            url: "/changeUserInfo",
            data: {
                id : userId,
                newNickname : newNickname,
                newAvaUrl : newAvaUrl
            },
            type: "post",
            datatype: "text",
            success: function (data) {
                if (data === "ok"){
                    //更新成功
                    layer.open({
                        title: '提示框'
                        , content: '用户信息更新成功',
                        time: 1000
                    });
                    //1s后刷新评论数
                    setTimeout(function () {
                        location.reload();
                    },1000);
                }else {
                    // 更新失败
                    layer.msg('更新信息失败了...', {
                        icon: 2,
                        time: 2000
                    });
                }
            },
            error: function () {
            }
        });
    }

}