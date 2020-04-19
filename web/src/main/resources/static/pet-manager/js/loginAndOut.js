$(function () {
    if (cookie.getCookie("mname1")){
        $("#mname_title").html(cookie.getCookie("mname1")+"管理员");
    }
});
function logout(){
    cookie.removeCookie("mname1");
    $.post("/manager/logout",{},function (data) {
        // var res=JSON.parse(data);
        if (data.msg=="yes"){
            location.href="login-Manager.html";
        }else {
            alert("退出失败");
        }
    },"json")
    return false;

}