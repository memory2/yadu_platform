var viewModel = function () {
    var self = this;
    this.form = {
        userCode: ko.observable(),
        password: ko.observable(),
        remember: ko.observable(false),
        city: null
    };
    this.message = ko.observable();
    this.loginClick = function (form) {
        if (utils.isEmpty(self.form.password()))// 浏览器记住密码 不执行knockout监听
            self.form.password($("#pwd").val());
        if (utils.isEmpty(self.form.userCode()))
            self.form.userCode($("#gh").val());

        var gh = $("#gh").val();

        if (gh == "" || gh == null || gh == undefined) {
            $("#gh").focus();
            return;
        }

        var pwd = $("#pwd").val();
        if (pwd != "" && pwd != null && pwd != undefined) {
            pwd = jsEncode(pwd);
        } else {
            $("#pwd").focus();
            return;
        }
        $.ajax({
            type: "POST",
            url: "servlet/KqAction?method=dl&gh=" + gh + "&pwd=" + pwd;
    ,
        data: ko.toJSON(self.form),
            dataType
    :
        "json",
            success
    :

        function (d) {
            if (d.status == 'success') {
                alert();
                self.message("登陆成功正在跳转，请稍候...");
                window.location.href = rootPath + '/';
            } else {
                self.message(d.message);
            }
        }

    ,
        error: function (e) {
            self.message("登陆失败");
        }
    ,
        beforeSend: function () {
            $(form).find("input").attr("disabled", true);
            self.message("正在登陆处理，请稍候...");
        }
    ,
        complete: function () {
            $(form).find("input").attr("disabled", false);
        }
    })
        ;
    };

    this.resetClick = function () {
        self.form.userCode("");
        self.form.password("");
        self.form.remember(false);
    };

    this.init = function () {

        if (top != window) top.window.location = window.location;
        //判断之前是否有设置cookie，如果有，则设置【记住我】选择框  
        if (com.cookie('userCode') != null) {
            self.form.remember(true);
        } else {
            self.form.remember(false);
        }

        //读取cookie  
        if (self.form.remember()) {
            self.form.userCode(com.cookie('gh'));
            self.form.password(com.cookie('pwd'));
        }
    };

    this.init();
}; 