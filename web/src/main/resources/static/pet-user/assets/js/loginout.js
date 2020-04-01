//检测是否已有账号登录
$(function () {
   /* $.post("/user/findOne", {}, function (data) {
        var obj = JSON.parse(data); //由JSON字符串转换为JSON对象
        var msg = "<li>欢迎回来</li>" + obj.uname;
        $("#loginOnline").html(msg + "&nbsp;&nbsp;<a onclick='quit()' style='cursor: pointer; font-size: 14px;'>退出</a>");
    });*/

   if (cookie.getCookie("uname")){
       var msg = "<li>欢迎回来</li>" + cookie.getCookie("uname");
       $("#loginOnline").html(msg + "&nbsp;&nbsp;<a onclick='quit()' style='cursor: pointer; font-size: 14px;'>退出</a>");
   }
});

function quit() {
    cookie.removeCookie("uname");
    cookie.removeCookie("utel");
    cookie.removeCookie("uemail");
    cookie.removeCookie("uid");
    location.href = "index.html";
    /*$.post("/user/logOut", {}, function (data) {
        location.href = "index.html";
    })*/

}