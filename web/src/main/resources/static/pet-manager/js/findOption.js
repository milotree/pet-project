var numbers = $(pet_id).find("option"); //获取select下拉框的所有值
for (var j = 0; j < numbers.length; j++) {
    if ($(numbers[j]).val() == pet_attr) {
        $(numbers[j]).porp("selected", "selected");
    }
}