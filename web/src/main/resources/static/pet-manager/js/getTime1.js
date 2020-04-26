function timeDate(time) {
    var thismonth = new Date(time ).getMonth() + 1;
    if (thismonth < 10) {
        var month = ("0" + thismonth).slice(-2);
    }else {
        var month = thismonth;
    }
    var thisday = new Date(time).getDate();
    if (thisday < 10) {
        var day = ("0" + thisday).slice(-2);
    }else {
        var day = thisday;
    }
   var thisHours= new Date(time).getHours();
    var thisMinutes = new Date(time).getMinutes();
    var thisSeconds = new Date(time).getSeconds();
    return new Date(time).getFullYear() + "-" + (month) + "-" + (day)+ " " + (thisHours)+ ":" +(thisMinutes)+ ":" +(thisSeconds);
}