function checkErrorInfo() {
    var infoTag = $("#errorInfo");
    if (infoTag.text() === "No message available"){
        infoTag.text("服务器出错了,检查下地址栏,或者稍后再来试试吧。") ;
    }
}