<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="format-detection" content="telephone=no">
		<title>我的订单</title>
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/frozen.css">
		<script type="text/javascript" src="js/jquery.js"></script>
		<style type="text/css">
			.mbtn {
				width: 100%;
				height: 20px;
				background-color: #fff;
				border: #D43530 0px solid;
				color: #D43530;
			}
		</style>

	</head>

	<body>
		<!--商品信息-->
		<ul class="ui-list ui-list-text" id="ordersList"></ul>
		
		<div id="listNull"></div>

	</body>

	<script type="text/javascript">
		//网页加载时调用
		$(document).ready(function() {
			getClientOrders();
		});

		//获取客户订单
		function getClientOrders() {
			var url = 'getClientOrders.action';
			var openId = '${openId}';
			localStorage.openId = openId;

			//get client orders
			$.ajax({
				type: "post",
				url: url,
				dataType: "json",
				data: {
					openId: openId
				},

				success: function(data) {
					console.log('data', data);
					if(data.resultCode == 'SUCCESS') {

						var orders = data.orders;
						//如果列表为空 加载
						if(orders.length < 1) {
							var icon = ['<font style="color: #8f8f8f"><p style="text-align:center; margin-top: 120px;" id="express">',
								'			<img src="img/dui.png" />',
								'		</p></font>		',
								'		<font style="color: #8f8f8f"><p style="text-align:center;" id="express">对不起，您没有订单~</p></font>'
							].join("");
							$('#listNull').append(icon);
							return;
						}

						//列表有数据 加载订单列表
						for(var d = 0; d < orders.length; d++) {
							//console.log("order:" + d + ",no:" + orders[d].order_id);

							var orderStatus;
							var btn;
							//判断订单状态
							if(data.orders[d].order_status == 1) {
								orderStatus = "未付款";
								btn = "付款";
							} else if(data.orders[d].order_status == 2) {
								orderStatus = "已付款";
								btn = "查看物流";
							} else if(data.orders[d].order_status == 3) {
								orderStatus = "已发货";
								btn = "查看物流";
							} else if(data.orders[d].order_status == 0) {
								orderStatus = "已取消";
								btn = "查看物流";
							} else {
								orderStatus = "交易成功";
								btn = "查看物流";
							}

							var list = ['		<section style="background: #E7E7E7;padding: 3px;"></section>',
								'			<li class="ui-border-t">',
								'				<div class="ui-list-info">',
								'					<h4><font style="color: #8f8f8f">订单号：' + data.orders[d].order_id + ' </font></h4>',
								'				</div>',
								'				<div class="ui-list-action"><font style="color: #F54C2A">' + orderStatus + '</font></div>',
								'			</li>',
								'			<div id="productList' + d + '" onclick="clickItem(' + data.orders[d].order_status + ',' + data.orders[d].order_id + ')">',
								'			</div>',
								'			<li class="ui-border-t" ><h6>共' + data.orders[d].total_quantity + '件商品 合计：¥ ' + data.orders[d].pay_amount + '（含运费¥ ' + data.orders[d].deliver_fee + '）</h6></li>',
								'			<li class="ui-border-t">',
								'				<div class="ui-list-info"></div>',
								' 				<div class="ui-list-action">',
								'        			<button class="ui-btn" style="border: #FF0101 1px solid; border-radius: 15px; margin-top: 3px;" onclick="clickBtn1(' + data.orders[d].order_status + ',' + data.orders[d].order_id + ')" id="btn' + d + '">取消订单</button>',
								'        			<button class="ui-btn" style="border: #FF0101 1px solid; border-radius: 15px; margin-top: 3px;" onclick="clickBtn2(' + data.orders[d].order_status + ',' + data.orders[d].order_id + ')" id="btwl'+ d + '">' + btn + '</button>',
								'				</div>',
								'			</li>'
							].join("");
							$('#ordersList').append(list);

							//如果列表状态不为“未付款” 隐藏掉“取消”按钮
							if(data.orders[d].order_status != 1) {
								$('#btn' + d).hide();
							}
							//已取消订单，隐藏查看物流信息
							if(data.orders[d].order_status == 0){
								$('#btwl' + d).hide();
							}

							//console.log("2");
							//加载每条订单的商品信息
							var orderDetails = data.orders[d].order_details;
							for(var li = 0; li < orderDetails.length; li++) {
								var div = ['		<li style="background: #FAFAFA;">',
									'					<div class="ui-row-flex ui-whitespace">',
									'						<div class="ui-list-img" style="">',
									'							<span style="background-image:url(img/hud_icon.png)"></span>',
									'						</div>',
									'						<div>',
									'							<h4 style="position: absolute;">' + data.orders[d].order_details[li].product_name + '</h4>',
									'							<h4 style="position: absolute; right: 0px;">¥ ' + data.orders[d].order_details[li].unit_price + '</h4>',
									'						</div>',
									'						<div style="margin-top: 20px;">',
									'							<h4 style="position: absolute; "></h4>',
									'							<h4 style="position: absolute; right: 0px;"><del><font style="color: #6B6B6B">¥ 1499.00</font></del></h4>',
									'						</div>',
									'						<div style="margin-top: 40px;">',
									'							<h5 style="position: absolute; "><font style="color: #CDCDCD">颜色：深灰</font></h5>',
									'							<h5 style="position: absolute; right: 0px;"><font style="color: #CDCDCD">x ' + data.orders[d].order_details[li].quantity + '</font></h5>',
									'						</div>',
									'					</div>',
									'				</li>'

								].join("");
								$('#productList' + d).append(div);
							}
						}

					} else {
						console.log('get data failed');
						alert("get product failed");
					}
				}
			});
		}

		//点击某个订单
		function clickItem(orderStatus, orderId) {
			if(window.localStorage) {
				//把订单号用localStorage存储
				localStorage.orderNo = orderId;
				localStorage.orderStatus = orderStatus;
				window.location.replace("Order_details.html");
			} else {
				//浏览器不支持 不存储
			}
		}

		//点击某个订单 取消按钮(只存在于“未付款”状态)
		function clickBtn1(orderStatus, orderId) {
			//ToDO
			cancelOrder(orderId);
		}

		//点击某个订单 右边按钮(付款或查看物流)
		function clickBtn2(orderStatus, orderId) {
			if(orderStatus == 1) {
				localStorage.orderNo = orderId;
				localStorage.orderStatus = orderStatus;
				window.location.replace("Order_details.html");
			} else {
				//跳转到物流详情页面
				//ToDO
			}
		}

		
		//获取客户订单
		function cancelOrder(orderNo) {
			var url = 'cancelOrder.action';
			console.log("cancel order",orderNo);
			//get client orders
			$.ajax({
				type: "post",
				url: url,
				dataType: "json",
				data: {
					orderNo: orderNo
				},
				success: function(data) {
					console.log('data', data);
					if(data.resultCode == 'SUCCESS') {
						console.log("cancel order successfully");
					}else{
						console.log("cancel order failed",data.resultMessage);
					}
				}
			});
		}

		pushHistory();
		window.addEventListener("popstate", function(e) {
			window.location = 'product_detail.html';
		}, false);

		function pushHistory() {
			var state = {
				title: "title",
				url: "#"
			};
			window.history.pushState(state, "title", "#");
		}

	</script>

</html>