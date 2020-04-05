/*登录部分*/

//检查用户名格式
function checkLoginName() {

   var infoTag = $("#checkLoginNameSuccess");
   var loginName = $("#loginName").val();
   infoTag.hide();
    if (loginName === "" || loginName === undefined || loginName === null) {
        // infoTag.html("<b style='color:red'>不能为空!</b>");
        // infoTag.show();

        layer.tips('用户名不能为空!', '#loginName',{
            tipsMore: true
        });
        return false;
    }
    eval("var reg = /^[a-zA-Z0-9_-]{4,16}$/;");
    var resFlag = RegExp(reg).test(loginName);
    if (!resFlag){
        // infoTag.html("<b style='color:red'>格式错误!</b>");
        // infoTag.show();

        layer.tips('用户名格式错误!', '#loginName',{
            tipsMore: true
        });
        return false;
    }
    infoTag.show();
    return true;
}

//检查密码格式
function checkLoginPassword() {
    var infoTag = $("#checkLoginPwdSuccess");
    var loginPassword = $("#loginPassword").val();
    infoTag.hide();
    if (loginPassword === "" || loginPassword === undefined || loginPassword === null) {
        // infoTag.html("<b style='color:red'>不能为空!</b>");
        // infoTag.show();
        layer.tips('密码不能为空!', '#loginPassword',{
            tipsMore: true
        });
        return false;
    }
    eval("var reg = /^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]).{6,16}$/;");
    var resFlag = RegExp(reg).test(loginPassword);
    if (!resFlag){
        // infoTag.html("<b style='color:red'>格式错误!</b>");
        // infoTag.show();
        layer.tips('密码格式错误!', '#loginPassword',{
            tipsMore: true
        });
        return false;
    }
    infoTag.show();
    return true;
}

//登录事件 异步校验 判断是否登录成功
// function checkLoginInfo() {
//     return checkLoginName() && checkLoginPassword();
// }
function loginUserByBtn(){
     if (checkLoginName() && checkLoginPassword()){
         var loginName = $("#loginName").val();
         var loginPassword = $("#loginPassword").val();
         //异步校验登录账户
         $.ajax({
             url: "/login",
             data: {
                 "loginUsername":loginName,
                 "loginPassword":loginPassword
             },
             type: "post",
             datatype: "text",
             success: function (data) {
                 if (data === "success"){
                     //登录成功跳转至主页
                     $(location).prop('href',"/");
                     //index.html中checkIframe()判断是否需要关闭页面
                 }else {
                     layer.msg('用户名/密码错误,请检查！');
                     return false;
                 }
             }
         });
     }else {
         layer.msg('请先正确填写全部信息！', function(){
         });
         return false;
     }
}

//找回密码
function findPassword() {
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '240px'], //宽高
        content: $("#findPasswordFrame")
    });
}