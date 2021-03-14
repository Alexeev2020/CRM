<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme()+"://" +request.getServerName()+
":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){


		//=================为创建模态窗口的按钮addBtn绑定事件
        
		$("#addBtn").click(function () {

			//bootstrap插件的引入
			$(".time").datetimepicker({
				minView:"month",
				language:'zh-CN',
				format:'yyyy-mm-dd',
				autoclose:true,
				todayBtn:true,
				pickerPosition:"bottom-left"
			});


			/*后台查询出用户信息列表，为所有者下拉框铺值*/
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type: "GET",
				dataType:"json",
				success:function (data) {

					//用each循环查询所得的user对象的信息为所有者下拉选框赋值
					var html = "<option></option>";
					$.each(data,function (i,n) {
						html+= "<option value='"+n.id+"'>"+n.name+"</option>"
					})

					//所有者下拉框处理完毕后，展现模态窗口
					$("#create-owner").html(html);

					//将当前登录的用户设置为下拉框默认的选项
					//注意，在js中使用EL表达式一定要套用在“”中
					var id = "${user.id}"
					$("#create-owner").val(id)

					/*操作模态窗口的方式*/
					/*操作模态窗口的jquery对象，调用model方法，为该方法传递参数 show：展示模态窗口，hide：关闭模态窗口*/
					$("#createActivityModal").modal("show");
				}
			})
		})

        //=================为创建模态窗口的按钮editBtn绑定事件

        $("#editBtn").click(function () {

            var $xz = $("input[name=xz]:checked")
            if ($xz.length==0){
                alert("请选择一条需要修改的数据")
            }else if ($xz.length>1){
                alert("每次只能对一条数据进行修改")
            //只选择了一条数据
            }else{
                //
                var id = $xz.val();
                //bootstrap插件的引入
                $(".time").datetimepicker({
                    minView:"month",
                    language:'zh-CN',
                    format:'yyyy-mm-dd',
                    autoclose:true,
                    todayBtn:true,
                    pickerPosition:"bottom-left"
                });

                /*后台查询出用户信息列表，铺值*/
                $.ajax({
                    url:"workbench/activity/getUAA.do",
                    type: "GET",
                    data:{"id":id},
                    dataType:"json",
                    success:function (data) {

                        //用each循环查询所得的user对象的信息为所有者下拉选框赋值
                        var html = "<option></option>";
                        $.each(data.ulist,function (i,n) {
                            html+= "<option value='"+n.id+"'>"+n.name+"</option>"
                        })

                        //所有者下拉框处理完毕后
                        $("#edit-owner").html(html);

                        //处理单条ActivityActivity
                        $("#edit-id").val(data.activity.id);
                        $("#edit-owner").val(data.activity.owner);
                        $("#edit-name").val(data.activity.name);
                        $("#edit-startDate").val(data.activity.startDate);
                        $("#edit-endDate").val(data.activity.endDate);
                        $("#edit-cost").val(data.activity.cost);
                        $("#edit-description").val(data.activity.description);

                        /*操作模态窗口的方式*/
                        /*操作模态窗口的jquery对象，调用model方法，为该方法传递参数 show：展示模态窗口，hide：关闭模态窗口*/
                        $("#editActivityModal").modal("show");
                    }
                })

            }

        })

        //=================为更新按钮绑定事件

        $("#updateBtn").click(function () {
            $.ajax({
                url: "workbench/activity/updateActivity.do",
                data: {
                    "id":$.trim($("#edit-id").val()),
                    "owner": $.trim($("#edit-owner").val()),
                    "name": $.trim($("#edit-name").val()),
                    "startDate": $.trim($("#edit-startDate").val()),
                    "endDate": $.trim($("#edit-endDate").val()),
                    "cost": $.trim($("#edit-cost").val()),
                    "description": $.trim($("#edit-description").val())
                },
                type: "POST",
                dataType: "json",
                success: function (data) {
                    if (data){
                        $("#editActivityModal").modal("hide");
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                    }else{
                        alert("修改失败")
                    }

                }
            })
        })

        //=================为查询按钮绑定事件
        
        $("#searchBtn").click(function () {
            //点击查询按钮之后，应当将搜索框中的信息保存起来，保存到隐藏域中、
            $("#hidden-name").val($.trim($("#query-name").val()));
            $("#hidden-owner").val($.trim($("#query-owner").val()));
            $("#hidden-startDate").val($.trim($("#query-startDate").val()));
            $("#hidden-endDate").val($.trim($("#query-endDate").val()));
            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
        })


        //=================为全选的复选框绑定事件，触发全选操作
        
        $("#qx").click(function (){
            $("input[name=xz]").prop("checked",this.checked);
        })
        //为当前页面的信息列表复选框 动态元素 绑定事件
        //动态生成的元素,我们要以on方法的形式来触发事件
        //语法：
        //      $(需要绑定的元素的有限外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)
        $("#activityBody").on("click",$("input[name=xz]"),function () {
            //通过判断此时复选框数量是否与已经checked的复选框的数量一致，一致则给全选的复选框赋值为true，既checked，不一致则赋值为false，全选框不选
            $("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
        })


        
        //================为删除按钮绑定事件，执行市场活动的删除操作
        $("#deleteBtn").click(function () {
           //找到复选框中所有checked的jquery对象
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
               alert("请选择要删除的数据记录");
            }else{
                if (confirm("确定删除所选中的数据记录吗？")){

                    //用户已经选择了需要删除的记录，可能是一条也可能是多条;json传递同样key值比较麻烦，使用拼接参数方式传参
                    var param = "";
                    //将$xz中的每一个dom对象遍历出来，取其value值，就是相当与取得了需要删除的记录的id
                    for(var i=0;i<$xz.length;i++){
                        param += "id="+$($xz[i]).val();

                        //如果不是最后一元素，需要在后面追加一个& 连接符
                        if (i<$xz.length-1){
                            param += "&";
                        }
                    }
                    //alert(param);
                    $.ajax({
                        url:"workbench/activity/delete.do",
                        data:param,
                        type: "POST",
                        dataType:"json",
                        success:function (data3) {
                            if (data3){
                                //alert("删除数据成功")
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("删除数据失败");
                            }
                        }
                    })
                }
            }
        })


		//=================为保存按钮绑定事件，执行添加操作，完成后清空并关闭页面
        
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner": $.trim($("#create-owner").val()),
					"name": $.trim($("#create-name").val()),
					"startDate": $.trim($("#create-startDate").val()),
					"endDate": $.trim($("#create-endDate").val()),
					"cost": $.trim($("#create-cost").val()),
					"description": $.trim($("#create-description").val())
				},
				type: "POST",
				dataType:"json",
				success:function (data) {
					if ("1"==data.nums){
						//jquery对象无法调用reset()方法，必须转为原生js（Dom）对象才可以调用
						//复习jquery-->Dom的操作:通过id选择器获取到Dom数组对象后取数组第一个元素，即下标为0的元素
						//      Dom-->jquery的操作:$(dom)
						$("#createFormId")[0].reset();
						$("#createActivityModal").modal('hide');
						/*
						* $("#activityPage").bs_pagination('getOption', 'currentPage')
						* 操作后停留在当前页
				        * $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
				        * 操作后维持已经设置好的每页展现记录数
						* */
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                    }else{
						alert("添加市场活动失败");
					}
				}
			})
		})
		//===========页面加载完成后，执行下列查询方法，提供当前页中的市场活动的信息列表
        
		pageList(1,3);
	});

	//=================pageList函数的具体定义
    
    //=================函数定义
	/*什么时候需要调用pageList分页方法
	  1.点击左侧菜单中的 市场活动 时，需要刷新市场活动列表，调用pageList方法
	  2.点击查询按钮时
	  3.执行 添加，修改，删除，三项操作且成功之后
	  4.点击分页插件时
	  */
	function pageList(pageNo,pageSize) {
	    //在每次分页之前把全选框设置为未选中状态
        $("#qx").prop("checked",false);
        //分页查询之前，将隐藏域中的信息取出，重新赋值给搜索框中
        $("#query-name").val($.trim($("#hidden-name").val()));
        $("#query-owner").val($.trim($("#hidden-owner").val()));
        $("#query-startDate").val($.trim($("#hidden-startDate").val()));
        $("#query-endDate").val($.trim($("#hidden-endDate").val()));
        $.ajax({
			url:"workbench/activity/query.do",
			data:{
				"pageNo": pageNo,
				"pageSize": pageSize,
				"name": $.trim($("#query-name").val()),
				"owner": $.trim($("#query-owner").val()),
				"startDate": $.trim($("#query-startDate").val()),
				"endDate": $.trim($("#query-endDate").val())
			},
			type: "get",
			dataType:"json",
			success:function (data) {
				/*所需data分析
				* 1.市场活动信息列表，activityList List集合
				* 2.信息总条数，total int 用以分页插件显示分页
				* 3.状态信息， State String 用于表示程序执行情况
				* */
				var addHtml = "";
				//注意方法里面的owner要使用多表联查 查询真实的用户对应的姓名
				$.each(data.dataList,function (i,n) {
					addHtml += '<tr class="active">';
					addHtml += '<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
					addHtml += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					addHtml += '<td>'+n.owner+'</td>';
					addHtml += '<td>'+n.startDate+'</td>';
					addHtml += '<td>'+n.endDate+'</td>';
					addHtml += '</tr>';
				})
				$("#activityBody").html(addHtml);

				//数据处理完毕后，结合分页插件，向前端传递
                //计算总页数
                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                $("#activityPage").bs_pagination({
                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //该回调函数在点击分页组件时触发
                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });
            }
		})
	}
	
</script>
</head>
<body>

<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="createFormId">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">

                            </select>
                        </div>
                        <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate" readonly>
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <%--创建隐藏域对象，作用是把修改前的id值保存，并且在更新时，提供原id值--%>
            <input type="hidden" id="edit-id" />
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" >
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <%--
                                关于文本域，要以标签对呈现，正常情况下要紧紧挨着，因为空格和换行也算文本域内容
                                textarea虽然是以标签对的形式来呈现的，并且它没有value属性，但是它的赋值要使用val()方法，不能使用html()方法
                             --%>
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>




<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input id="query-name" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input id="query-owner" class="form-control" type="text">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input id="query-startDate" class="form-control" type="text"  >
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input id="query-endDate" class="form-control" type="text" >
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
                <button type="button" class="btn btn-danger"  id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--<tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage"></div>
        </div>

    </div>

</div>
</body>
</html>