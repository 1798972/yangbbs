//1.清除用户名
function removeUserName() {
    $("#login_username").val('');
}
//2.清除验证码
function removeVerificationCode() {
    $("#login_verificationcode").val('');
}

//3.密码显示与隐藏
function userPasswordEye(name, type) {
    var id = "#" + name;
    var typeValue = '';
    if(type === 'onmousedown') {
        $(id).siblings(".password-eye-span").css("color", "#00FEBF");
        typeValue = 'text'; // 明文显示
    } else {
        $(id).siblings(".password-eye-span").css("color", "#ccc");
        typeValue = 'password'; // 密文显示
    }
    $(id).attr("type", typeValue);
}

//4.改变验证码
function changeImg() {
    function changeImg() {
        document.getElementById("codeImg").src = "/getImgCode?num=" + Math.random();
    }
}