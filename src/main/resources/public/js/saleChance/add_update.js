layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单事件
     */
    form.on("submit(addOrUpdateSaleChance)",function(obj){
        /*加载层*/
        //var index=layer.msg("数据正在提交中，请稍等",{icon: 16,time:false,shade:0.8 });

        console.log(obj.field+"<<");

        //判断是添加还是修改，id==null,添加，id!=null 修改

        var url=ctx+"/sale_chance/save";

        //判断当前页面的隐藏域有没有id,有id做修改，否则添加操作
        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/updata"
        }

        /*发送ajax*/
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (obj){
                if(obj.code==200){
                    //提示一下
                    layer.msg(obj.msg,{icon: 6 });
                    //关闭加载层
                   // layer.close(index);
                    //关闭iframe
                    layer.closeAll("iframe");
                    //刷新页面
                    window.parent.location.reload();
                }else{
                    layer.msg(obj.msg,{icon : 5 });
                    // layer.msg("!!!!!!!!!!!!!!!!!!!");
                }
            }
        });
        //取消跳转
        return false;
    });


    //    取消关闭弹窗
    $("#closeBtn").click(function (){
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

    /*添加下拉框*/
    var assignMan=$("input[name='man']").val();
    $.ajax({
        type:"post",
        url:ctx+"/user/sales",
        dataType:"json",
        success:function(data){
            //console.log(data[0].userName);

            //遍历
            for (var x in data) {
                if(data[x].id==assignMan){
                    $("#assignMan").append("<option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }else{
                    $("#assignMan").append("<option  value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }

            }
            //重新渲染
            layui.form.render("select");
        }
    });


});