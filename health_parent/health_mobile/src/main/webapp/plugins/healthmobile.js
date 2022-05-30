// 获取指定的URL参数值
function getUrlParam(paramName) {
    // 例：url==http://localhost/pages/setmeal_detail.html?id=3&name=jack
    const url = document.location.toString();
    const arrObj = url.split("?");
    if (arrObj.length > 1) {
        const arrPara = arrObj[1].split("&");
        let arr;
        for (let i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");
            if (arr != null && arr[0] === paramName) {
                return arr[1];
            }
        }
    }
    return "";
}

// 获得当前日期，返回字符串
function getToday() {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1;
    const day = today.getDate();
    return (year + "-" + month + "-" + day);
}

// 获得指定日期后指定天数的日期
function getSpecifiedDate(date,days) {
    // 获取指定天之后的日期
    date.setDate(date.getDate() + days);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return (year + "-" + month + "-" + day);
}

/**
 * 手机号校验
 1--以1为开头；
 2--第二位可为3,4,5,7,8,中的任意一位；
 3--最后以0-9的9个整数结尾。
 */
function checkTelephone(telephone) {
    // const reg = /^[1][3,4,5,7,8][0-9]{9}$/;
    // 由于个人用户没有发送短信验证码的权限，所以改为发送邮件验证码。这里也改为“对邮箱格式进行判断”
    const reg = /^\w+@\w+\.\w+$/;
    return reg.test(telephone);
}

/**
 * 身份证号码校验
 * 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
 */
function checkIdCard(idCard){
    const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    return reg.test(idCard);
}

let clock = ''; // 定时器对象，用于页面60秒倒计时效果
let nums = 60;
let validateCodeButton;

// 基于定时器实现60秒倒计时效果
function doLoop() {
    // 将按钮置为不可点击
    validateCodeButton.disabled = true;
    nums--;
    if (nums > 0) {
        validateCodeButton.value = nums + '秒后重新获取';
    } else {
        // 清除js定时器
        clearInterval(clock);
        validateCodeButton.disabled = false;
        validateCodeButton.value = '重新获取验证码';
        // 重置时间
        nums = 60;
    }
}