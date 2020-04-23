function addCard(uid,id, pname, pprice, pnum, pimg) {
    if (cookie.getCookie("uname")) {
        $.post("/cart/addCart", {uid:uid,pid: id, pname: pname, pprice: pprice, pnum: pnum, pimg: pimg}, function (data) {
            tablefill(data, data.length);
        }, "json");
    } else {
        if (window.confirm("需登录才能进行操作，是否登录？")) {
            location.href = "login-register.html";
        } else {
            return false; 
        }
    }
    window.scrollTo(0, 300);
}

function checkMyaccount() {
    if (cookie.getCookie("uname")){
        var uid = cookie.getCookie("uid");
        location.href="my-account.html?uid="+uid;
}else {
        if (window.confirm("需登录才能进行操作，是否登录？")) {
            location.href = "login-register.html";
        } else {
            return false;
        }
    }
}


function delCart(id,uid) {
    // alert(id+"++++++++"+uid);
    $.post("/cart/delCart", {pid:id,uid:uid}, function (data) {
        tablefill(data, data.length);

    }, "json");
}

function tablefill(data, length) {
    var str = "";
    var img;
    var moneySum = 0;
    for (var i = 0; i < length; i++) {
        img = data[i].goodsImg.substring(data[i].goodsImg.length - 36);
        str += '                                    <li class="single-shopping-cart">\n' +
            '                                        <div class="shopping-cart-img">\n' +
            '                                            <img alt="" src="/petImage/' + img + '">\n' +
            '                                        </div>\n' +
            '                                        <div class="shopping-cart-title">\n' +
            '                                            <h4><a href="#"><font style="vertical-align: inherit;"><font\n' +
            '                                                    style="vertical-align: inherit;">' + data[i].goodsName + ' </font></font></a></h4>\n' +
            '                                            <h6><font style="vertical-align: inherit;"><font\n' +
            '                                                    style="vertical-align: inherit;">数量：' + data[i].goodsNum + '</font></font></h6>\n' +
            '                                            <span>￥' + data[i].goodsPrice + '</span>\n' +
            '                                        </div>\n' +
            '                                        <div class="shopping-cart-delete">\n' +
            '                                            <a onclick="delCart(\'' + data[i].goodsId + '\',\'' + cookie.getCookie("uid") + '\')"><i class="ti-close"></i></a>\n' +
            '                                        </div>\n' +
            '                                    </li>\n';
        moneySum += parseInt(data[i].goodsPrice);
    }
    $("#moneySum").html(moneySum);
    $("#goodsNumber").html(length);
    $("#cartShow").html(str);
}

/**
 * 发送一个信号给后端，让后台将redis数据库中的内容传递至MySQL数据库中
 */
/*function emptyCar() {
    $.post("/cart/emptyCar",{},function (data) {

    },"json");


}*/

//将购物车的值放入订单
function addOrder() {
    var uid = cookie.getCookie("uid");
    var timestamp=new Date().getTime();
    $.post("/cart/addOrder",{uid:uid,time:timestamp},function (data) {
        if (data.msg=="提交订单成功"){
            location.href="checkout.html";
        }else {
            alert(data.msg);
        }
    },"json")
}


