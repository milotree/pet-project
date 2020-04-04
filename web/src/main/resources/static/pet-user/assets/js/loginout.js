//检测是否已有账号登录
$(function () {


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

}