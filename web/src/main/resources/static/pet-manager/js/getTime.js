function timeDate(time) {
    var thismonth = new Date(time * 1000).getMonth() + 1;
    if (thismonth < 10) {
        var month = ("0" + thismonth).slice(-2);
    }else {
        var month = thismonth;
    }
    var thisday = new Date(time * 1000).getDate();
    if (thisday < 10) {
        var day = ("0" + thisday).slice(-2);
    }else {
        var day = thisday;
    }
    return new Date(time * 1000).getFullYear() + "-" + (month) + "-" + (day);
}