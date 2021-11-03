layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //角色列表展示
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function(){
        tableIns.reload({
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });


    /*头部工具栏绑定*/
    //头工具栏事件
    table.on('toolbar(roles)', function(obj){
        //删除获取id
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                layer.msg("添加");
                openAddOrUpdateUserPage();
                break;
            case 'grant':
                layer.msg("授权");
                toRoleGrant(checkStatus.data);
                break;
        };
    });
    /*授权*/
    function toRoleGrant(datas){
        if(datas.length>1){
            layer.msg("不支持多人同时授权~",{icon:5});
            return;
        }
        //判断
        if(datas.length<1){
            layer.msg("请选择授权目标",{icon:5});
            return;
        }

        var title="<h2>角色模块---授权</h2>";
        var url=ctx+"/role/toRoleGrant?roleId="+datas[0].id;
        //弹出层
        layer.open({
            title:title,
            content:url,
            type:2,//iframe
            area:["650px","400px"],
            maxmin:true,
        })
    }



    /*添加或修改*/
    function openAddOrUpdateUserPage(roleId){
        console.log(roleId+'-----------------')
        var title="<h2>角色模块---添加</h2>";
        var url=ctx+"/role/addOrUpdatePage";

        //判断是否修改还是添加
        if(roleId){
            title="<h2>角色模块---更新</h2>";
            url=url+"?id="+roleId;
            console.log(url);
        }
        //弹出层
        layer.open({
            title:title,
            content:url,
            type:2,//iframe
            area:["650px","400px"],
            maxmin:true,
        })
    }


    /*行内工具栏的绑定*/
    //监听行工具事件
    table.on('tool(roles)', function(obj){
        var data = obj.data;
        console.log(data.id);
        if(obj.event === 'del'){
            layer.confirm("主人，你确定狠心删除数据吗?",{
                btn:["确认","取消"]
            },function(index){
                //关闭
                layer.close(index);
                //发送ajax删除数据
                $.post(ctx+"/role/delete",{id:data.id},function(result){
                    if(result.code==200){
                        //重新加载数据
                        tableIns.reload();
                    }else{
                        //提示一下
                        layer.msg(result.msg,{icon:5 });
                    }
                },"json");
            });
        } else if(obj.event === 'edit'){
            console.log(data.id);
            openAddOrUpdateUserPage(data.id);
        }
    });


});