//页面加载完成时 执行的函数
//主要在缩小屏幕时 想移除那个自定义的css
// $(function () {
//     alert("aaa");
// });


//选择列表
$(document).ready(function () {
    var element = $("#tag_input");
    $("#tag_select ul li").click(function () {
        var tempInput = element.val();
        var result = $(this).text();
        //若标签框中为空
        if (tempInput.indexOf(result) === -1) {
            if (tempInput === "") {
                element.val(result);
            } else {
                element.val(tempInput + ',' + result);
            }
        }
    })
});

//删除一个
function deleteOneTag() {
    var element = $("#tag_input");
    //输入框内容
    var tags = element.val();
    //若有,
    //起码存在两个tag
    if (tags.indexOf(",") !== -1) {
        //切割出数组
        var arr = tags.split(',');
        if (arr.length === 2) {
            //两个的话直接就是第一个
            element.val(arr[0]);
        } else {
            //三个及以上
            var i = tags.lastIndexOf(",");
            var str = tags.substr(0, tags.lastIndexOf(",", i));
            element.val(str);
        }
    } else {
        element.val('');
    }
}

//全部删除
function deleteAllTags() {
    $("#tag_input").val('');
}

// var x=5; //利用了全局变量来执行
// function go(){
//     x--;
//     if(x>0){
//         $("#successtime").html(x+'S');  //每次设置的x的值都不一样了。
//     }else{
//         location.href='/';
//     }
// }



