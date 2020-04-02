/*注册部分*/

//全局变量 是否能提交
var canPost = false;
//返回值0错误 1正确

//1.输入框失焦时 异步校验注册用户名
function ajaxCheckRegisterName() {
    //ajax里面的return只是结束本方法 需要外面定义一个变量做返回值
    var flag = false;
    var regisName = $("#registerName").val();
    //每次先将 存在 / 不存在 隐藏
    var infoTag = $("#checkNameInfo");
    var infoTag2 = $("#checkNameInfo2");
    infoTag.hide();
    infoTag2.hide();
    //为空检查
    if (regisName === "" || regisName === undefined || regisName === null) {
        layer.tips('用户名不能为空!', '#registerName',{
            tipsMore: true
        });
        return flag;
    }
    //正则验证格式
    eval("var reg = /^[a-zA-Z0-9_-]{4,16}$/;");
    var resFlag = RegExp(reg).test(regisName);
    if (!resFlag){
        layer.tips('用户名格式不正确!', '#registerName',{
            tipsMore: true
        });
        return flag;
    }
    $.ajax({
        //1.请求的地址
        url: "/register/checkname",
        //2.请求的数据
        data: {
            regisName: regisName
        },
        //3.请求方式
        type: "get",
        //4.数据的类型
        datatype: "text",
        async: false,
        //5.成功后返回data  做什么处理
        success: function (data) {
            if (data === 0) {
                infoTag.show();
                flag = true;
            } else {
                infoTag2.show();
                flag = false;
            }
        },
        error: function () {
            alert("error!");
            flag = false;
        }
    });

    return flag;
}

//2.输入框失焦点时 检查用户密码
function checkRegisterPassword() {
    //密码
    password = $("#registerPassword").val();
    //提示按钮
    var infoTag = $("#checkPasswordInfo");
    infoTag.hide();

    //为空判断
    if (password === '' || password === undefined || password === null) {
        //密码为空
        layer.tips('密码不能为空!', '#registerPassword',{
            tipsMore: true
        });
        return false;
    }
    //格式检查
    eval("var reg = /^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]).{6,16}$/;");
    var resFlag = RegExp(reg).test(password);
    if (!resFlag){
        layer.tips('密码格式不正确!', '#registerPassword',{
            tipsMore: true
        });
        return false;
    }
    infoTag.show();
    return true;
}


//3.生成更换图片验证码
function changeImg() {
    document.getElementById("codeImg").src = "/register/getImgCode?num=" + Math.random();
}

//第一次加载先在body上查询一次
//3.检查图形验证码
function getCodeNow() {
    $.ajax({
        url: "/register/get4Code",
        type: "post",
        datatype: "text",
        success: function (data) {
            $("#receivedImgCode").html(data);
        }
    });
}

function checkRegisterImgCode() {
    //满足格式 比较真假 取不到？
    var backCode = $("#receivedImgCode").text();
    //输入的值
    var codeInput = $("#registerImgCheckCode").val();

    //提示按钮
    var infoTag = $("#checkImgCode");
    var infoTag2 = $("#checkImgCode2");
    infoTag.hide();
    infoTag2.hide();

    //1.先判断是否输入了验证码
    if (codeInput === '' || codeInput === undefined || codeInput === null) {
        //密码为空
        layer.tips('图形验证码不能为空!', '#registerImgCheckCode',{
            tipsMore: true,
            tips: [1, '#0FA6D8']
        });
        return false;
    } else {
        //验证格式
        eval("var reg = /^[a-zA-Z0-9]{4}$/;");
        var flag = RegExp(reg).test(codeInput);
        if (flag) {
            if (codeInput.toLocaleUpperCase() === backCode.toLocaleUpperCase()) {//忽略大小写
                //正确
                infoTag.show();
                return true;
            } else {
                infoTag2.show();
                return false;
            }
        } else {
            layer.tips('图形验证码格式不正确!', '#registerImgCheckCode',{
                tipsMore: true,
                tips: [1, '#0FA6D8']
            });
            return false;
        }
    }
}

//4.检查 手机号  检查被注册过吗
function checkRegisterTel() {
    var tempflag = false;
    var telNumber = $("#registerTel").val();

    var infoTag = $("#checkTel");
    infoTag.hide();
    //格式匹配
    eval("var reg = /^1[34578]\\d{9}$/;");
    var flag = RegExp(reg).test(telNumber);
    //格式正确
    if (flag === true) {
        //检查手机号用过没
        $.ajax({
            async: false,
            url: "/register/chickTel",
            data: {
                "telNumber":telNumber
            },
            type: "post",
            datatype: "text",
            success: function (data) {
                if (data === "hasUsed"){
                    //手机被注册过了
                    infoTag.show();
                    tempflag = false;
                }else {
                    tempflag = true;
                }
            }
        });
        return tempflag;
    } else {
        layer.tips('手机号格式错误!', '#registerTel',{
            tipsMore: true,
            tips: [1, '#0FA6D8']
        });
        return tempflag;
    }
}

//发送按钮的js事件 验证码倒计时
var countdown = 60;
var infoNum = 0;

function clickSendTelCode() {
    //发送验证码前 先进行人机验证
    if (checkRegisterImgCode() !== true) {
        var infoTag = $("#checkTel");
        layer.tips('请先校对图形验证码!', '#sendCodeBtn',{
            tipsMore: true,
        });
        return false;
    }
    var sendCodeBtn = $("#sendCodeBtn");

    //发送前先确定手机号是否正确
    var flag = checkRegisterTel();
    if (flag) {
        var telNum = $("#registerTel").val();
        var telNum4Last = telNum.substr(7, 4);
        //设置全局变量最后四位 每次点击后都赋予新值
        infoNum = parseInt(telNum4Last);
        //样式改变
        settime(sendCodeBtn, telNum4Last);
        //满足条件才能执行发短信的操作
        send6TelCode(telNum);
        //添加被点过的标记
        sendCodeBtn.attr("clickF", "hasClicked");
    }
}

//根据时间设置样式
function settime(obj) { //发送验证码倒计时
    if (countdown === 0) {
        //再次发送
        obj.removeClass("btn-warning");
        obj.addClass("btn-info");
        obj.attr('disabled', false);
        obj.html("发送验证码");
        //重新赋值
        countdown = 60;
        return;
    } else {
        //点击发送后 显示重新发送时间
        obj.removeClass("btn-info");
        obj.addClass("btn-warning");
        obj.attr('disabled', true);
        obj.html("尾号<b style='color: black'>" + infoNum + "</b>,重新发送(" + countdown + ").");
        countdown--;
    }

    setTimeout(function () {
        settime(obj)
    }, 1000)
}

//异步 发送手机验证码
function send6TelCode(telNum) {
    //点击事件设置了手机号验证 不用重复验证了

    //异步请求 发送手机号码 回传验证码
    $.ajax({
        //1.请求的地址
        url: "/register/send6code",
        //2.请求的数据
        data: {
            telNum: telNum
        },
        //3.请求方式
        type: "post",
        //4.数据的类型
        datatype: "text",
        //5.成功后返回data
        // {
        //     "status": "error",
        //     "backCode": 9001,
        //     "des": "手机号不是11位数吗？"
        // }
        //做什么处理
        success: function (data) {
            if (data.status === "success") {
                //成功发送验证码
                $("#receivedCode").html(data.backCode);
                //检测验证码
            } else {
                var errorCode = data.backCode;
                if (errorCode === 1024) {
                    layer.alert('请不要频繁获取验证码~一个小时后再来试试吧...', {
                        icon: 3,
                        skin: 'layer-ext-moon'
                    });
                } else if (errorCode === 1025) {
                    layer.alert('你今天已经获取太多次验证码了~明天再来试试吧...', {
                        icon: 1,
                        skin: 'layer-ext-moon'
                    });
                } else {
                    layer.alert('短信发送失败!'+ data.des, {
                        icon: 2,
                        skin: 'layer-ext-moon'
                    });
                }
            }
        },
        error: function () {
            //请求失败
        }
    });

}

//6.检查 手机验证码
function checkRegisterTelCode() {

    var numCode = $("#registerTelCheckCode").val();

    //提示信息的标签
    var infoTag = $("#checkTelBackCode");
    var infoTag2 = $("#checkTelBackCode2");
    infoTag.hide();
    infoTag2.hide();

    //1.检测输入的验证码格式
    // eval("var reg = /^1[34578]\\d{9}$/;");
    eval("var reg = /^\\d{6}$/;");
    var flag = RegExp(reg).test(numCode);
    if (flag === true) {
        //2.检测有没有发送验证码操做
        var sendCodeBtn = $("#sendCodeBtn");
        if (sendCodeBtn.attr("clickF") === undefined) {
            //未点击过
            layer.tips('请先获取验证码(若已经发送请稍等)', '#registerTelCheckCode',{
                tipsMore: true,
            });
            return false;
        } else {
            var code = $("#receivedCode").text();
            if (numCode === code) {
                infoTag.show();
                return true;
            } else {
                infoTag2.show();
                return false;
            }
        }
    } else {
        layer.tips('验证码应为6位数字!', '#registerTelCheckCode',{
            tipsMore: true,
        });
        return false;
    }
}

//9.异步注册请求
//不触发表单的submit事件即可
function registerUserByBtn() {
    //先检测
    canPost = ajaxCheckRegisterName() && checkRegisterPassword() && checkRegisterImgCode() && checkRegisterTel() && checkRegisterTelCode();
    // console.log(canPost);
    // canPost = ajaxCheckRegisterName() && checkRegisterPassword();
    if (canPost === false) {
        layer.msg('请先正确填写全部注册信息！', function(){
        });
        return false;
    } else {
        var resUserame = $('#registerName').val();
        var resPassword = $('#registerPassword').val();
        var resTelNumber = $('#registerTel').val();
        // var resgisterUser = {
        //     "name": resUserame,
        //     "password": resPassword,
        //     "tel": resTelNumber
        // };
        $.ajax({
            url: "/register",
            data: {
                "username": resUserame,
                "password": resPassword,
                "tel": resTelNumber
            },
            type: "post",
            //返回类型
            datatype: "text",
            success: function (data) {
                var regisState = data.state;
                if (regisState === "success"){
                    layer.msg('注册成功!即将跳转至登录界面...', {
                        icon: 1,
                        time: 2000
                    });
                    setTimeout(function () {
                        window.location.reload();
                    }, 2000);
                    //注册成功!跳转至登录
                    //显示selectDiv1 隐藏2
                    $("#user_register").hide();
                    $("#user_login").show();
                    $("#li_login").addClass("active");
                    $("#li_register").removeClass("active");
                }else {
                    //显示错误信息
                    layer.msg('错误码'+data.code+':'+ data.des, {icon: 5});
                }
            },
            error: function () {
            }
        });
    }
}

