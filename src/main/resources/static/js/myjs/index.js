function checkIframe() {
    //加载时判断自己在不在iframe中
    //不在iframe中
    if (self !== top) {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.location.reload();//刷新父页面，注意一定要在关闭当前iframe层之前执行刷新
        parent.layer.close(index); //再执行关闭
    }
}

