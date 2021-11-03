layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    //监听提交
    form.on('submit(saveBtn)', function(data){
        //layer.msg(JSON.stringify(data.field));
        var fieldData=data.field;
        // console.log('password:'+fieldData.old_password);
        // alert("测试.....")
        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                "oldPwd":fieldData.old_password,
                "newPwd":fieldData.new_password,
                "againPwd":fieldData.again_password
            },
            dataType:"json",
            success:function (data){

                if(data.code==200){
                    layer.msg("修改密码成功了，系统三秒钟后退出",function (){
                        //清空Cookie
                        $.removeCookie("userId",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转页面
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    //登录失败的提示
                    layer.msg(data.msg);
                }
            }
        });


        //取消默认行为
        return false;
    });

});