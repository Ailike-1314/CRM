layui.use(['form', 'layer',"formSelects"], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
         formSelects= layui.formSelects;

    /**
     * 添加或更新用户
     */
    form.on("submit(addOrUpdateUser)",function(data){
        var index=top.layer.msg("数据正在加载中...",{time:false,shade:0.8,icon:16});
        //判断添加还是修改
        var url=ctx+"/user/save";
        //判断是否做修改操作
        if($("input[name='id']").val()){
            url=ctx+"/user/update";
        }
        /*发送ajax添加*/
        $.post(url,data.field,function(result){
            if(result.code==200){
                //定时执行，定时器
                setTimeout(function(){
                    //关闭加载层
                    top.layer.close("index");
                    //提示消息
                    top.layer.msg(result.msg,{icon : 6 });
                    //关闭所有的iframe;
                    layer.closeAll("iframe");
                    //刷新
                    parent.location.reload();
                },500);
            }else{
                layer.msg(result.msg,{icon : 5 });
            }
        },"json");
        //取消默认跳转
        return false;
    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /**
     * 加载角色信息
     */
    formSelects.config('selectId', {
        type: 'post',                //请求方式: post, get, put, delete...
        searchUrl: ctx+'/role/findRoles?userId='+$("input[name=id]").val(),              //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
        keyName: 'roleName',            //自定义返回数据中name的key, 默认 name
        keyVal: 'id',            //自定义返回数据中value的key, 默认 value
        //当有搜索内容时, 点击选项是否清空搜索内容, 默认不清空
    }, true);

});