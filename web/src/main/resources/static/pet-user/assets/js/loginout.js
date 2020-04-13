//检测是否已有账号登录
$(function () {
   if (cookie.getCookie("uname")){
       var msg = "<li>欢迎回来</li>" + cookie.getCookie("uname");
       $("#loginOnline").html(msg + "&nbsp;&nbsp;<a onclick='quit()' style='cursor: pointer; font-size: 14px;'>退出</a>");
       cartReflash();
   }
});

function quit() {
    cookie.removeCookie("uname");
    cookie.removeCookie("utel");
    cookie.removeCookie("uemail");
    cookie.removeCookie("uid");
    cartDelAll();
    location.href = "index.html";

}

function cartReflash() {
    $.post("/cart/showCart",{},function (data) {
        var str = "";
        var img;
        var moneySum =0;
        if (data!=null){
            for (var i = 0 ;i<data.length;i++){
                img = data[i].goodsImg.substring(data[i].goodsImg.length - 36);
                str+='                                    <li class="single-shopping-cart">\n' +
                    '                                        <div class="shopping-cart-img">\n' +
                    '                                            <img alt="" src="/petImage/' + img + '">\n' +
                    '                                        </div>\n' +
                    '                                        <div class="shopping-cart-title">\n' +
                    '                                            <h4><a href="#"><font style="vertical-align: inherit;"><font\n' +
                    '                                                    style="vertical-align: inherit;">'+data[i].goodsName+' </font></font></a></h4>\n' +
                    '                                            <h6><font style="vertical-align: inherit;"><font\n' +
                    '                                                    style="vertical-align: inherit;">数量：'+data[i].goodsNum+'</font></font></h6>\n' +
                    '                                            <span>￥'+data[i].goodsPrice+'</span>\n' +
                    '                                        </div>\n' +
                    '                                        <div class="shopping-cart-delete">\n' +
                    '                                            <a onclick="delCart(\''+data[i].goodsId+'\')"><i class="ti-close"></i></a>\n' +
                    '                                        </div>\n' +
                    '                                    </li>\n';
                moneySum+=parseInt(data[i].goodsPrice);
            }
            var size = data.length;
        }else {
            var size = 0;
        }
        $("#moneySum").html(moneySum);
        $("#goodsNumber").html(size);
        $("#cartShow").html(str);
    },"json");
}
function cartDelAll() {
    $.post("/cart/DelAllCart",{},function (data) {
        cartReflash();

    });
}