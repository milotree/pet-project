function timeDate(time) {
    var thismonth = new Date(time * 1000).getMonth() + 1;
    if (thismonth < 10) {
        var month = ("0" + thismonth).slice(-2);
    }
    var thisday = new Date(time * 1000).getDate();
    if (thisday < 10) {
        var day = ("0" + thisday).slice(-2);
    }
    return new Date(time*1000)+thismonth+thisday;
}