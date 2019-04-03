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
		<title>确认订单</title>
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/frozen.css">
		<script type="text/javascript" src="js/jquery.js"></script>
		<style type="text/css">
			.mbtn {
				height: 30px;
				width: 30px;
				border-radius: 5px;
			}
			
			.mbtn-white {
				background-color: #E5E5E5;
			}
			
			.mbtn-red {
				background-color: #D43530;
				color: #fff;
			}
			
			.p-l {
				margin-right: 10px;
			}
			
			.p-r {
				margin-left: 10px;
				margin-right: -25px;
			}
		</style>
	</head>

	<body>

		<!--收货地址-->
		<ul class="ui-list ui-list-pure ui-border-tb">
			<li class="ui-border-t" id="setaddress">
				<div class="ui-row-flex ui-whitespace">
					<div style="margin-right: 20px;">
						<i class="ui-icon-pin" style="font-size: 50px;color: #858586; margin-left: -20px; margin-right: -20px;"></i>
					</div>
					<div>
						<h5 style="position: absolute;">收货人：<label id="name"></h5>
						<h5 style="position: absolute; right: 10px;"></label><label id="phone"></label></h5>
					</div>
					<div style="margin-top: 20px;">
						<h4>收货地址：<label id="address"></label></h4>
					</div>
				</div>

			</li>
		</ul>

		<!--快递线-->
		<section style="background-image: url(img/line.png); height: 4px;"></section>

		<!--间隔-->
		<section style="background: #E7E7E7;padding: 5px;"></section>

		<!--商品信息-->
		<ul class="ui-list ui-list-text">
			<li class="ui-border-t">
				<div class="ui-list-info">
					<h4>酷驾导航</h4>
				</div>
			</li>
			<div>
				<li style="background: #888;">
					<div class="ui-row-flex ui-whitespace">
						<div class="ui-list-img">
							<span style="background-image:url(img/hud_icon.png)"></span>
						</div>
						<div>
							<h4 style="position: absolute;"><label  id="productName"></label></h4>
							<h4 style="position: absolute; right: 0px;"></h4>
						</div>
						<div style="margin-top: 20px;">
							<h5 style="position: absolute; "><font style="color: #CDCDCD">颜色：深灰</font></h5>
							<h5 style="position: absolute; right: 0px;"></h5>
						</div>
						<div style="margin-top: 40px;">
							<h4 style="position: absolute; "><font color="red">¥ <label id="productPrice"></label></font></h4>
							<h4 style="position: absolute; right: 0px;">x <label id="icQuantity"></label></font></h4>
						</div>
					</div>
				</li>
			</div>
		</ul>

		<ul class="ui-list ui-list-text ui-border-tb">
			<!--购买数量-->
			<li class="ui-border-t">
				<div class="ui-list-info">
					<h4>购买数量</h4>
				</div>
				<div class="ui-list-action">
					<div class="ui-btn-wrap">
						<div class="ui-row-flex ui-whitespace">
							<div class="ui-col p-l"><button class="mbtn mbtn-white" type="button" id="min"><font size="3">—</font></button></div>
							<input id="quantity" type="text" value="1" style="height: 30px; width: 30px; text-align:center" />
							<div class="ui-col p-r"><button class="mbtn mbtn-red" type="button" id="add"><font size="4">+</font></button></div>
						</div>
					</div>
				</div>
			</li>
			<!--配送方式-->
			<li class="ui-border-t">
				<div class="ui-list-info">
					<h4>配送方式</h4>
				</div>
				<div class="ui-list-action">
					<select name="" id="company" onchange="selectFee()">

					</select>
					快递：¥ <label id="deliverFee"></label>
				</div>
			</li>
			<!--优惠活动-->
			<!--<li class="ui-border-t">
					<div class="ui-list-info">
						<h4>店家优惠</h4>
					</div>
					<div class="ui-list-action">新客户：- ¥<label id="discounts"></label></div>
				</li>-->
			<!--合计-->
			<li class="ui-border-t">
				<div class="ui-list-info">
					<h4>合计</h4>
				</div>
				<div class="ui-list-action">
					<font color="red">¥ <label id="total"></label></font>
				</div>
			</li>
			<!--卖家留言-->
			<!--				<li class="ui-border-t">
					<div class="ui-list-info">
						<h4>买家留言</h4>
					</div>
				</li>
				<div class="ui-input ui-border-radius">
					<input type="text" name="" value="" placeholder="选填：对本次的交易说明" id="askFor">
				</div>-->
		</ul>

		<!--浮动按钮-->
		<div class="ui-footer ui-footer-stable ui-btn-group ui-border-t">
			<button class="ui-btn-lg ui-btn-danger" id="pay">微信支付</button>
		</div>

		<script type="text/javascript">
			var prepay_id;
			var paySign;
			var appId;
			var timeStamp;
			var nonceStr;
			var packageStr;
			var signType;
			var out_trade_no;

			function pay() {
				//wechat pay:
				var url = 'http://wx.cooldrivenavi.com/WxPay/wechatPay.action';
				//var url = 'http://localhost:8080/WxPay/wechatPay.action';

				var products = [{
					"product_id": 1,
					"quantity": quantity,
				}];

				console.log('url', url);
				console.log('companyId', companyId);
				console.log('products', products);

				$.ajax({
					type: "post",
					url: url,
					dataType: "json",
					data: {
						openId: '${openId}', //用户微信 openID  //
						products: JSON.stringify(products), //订单产品明细
						order_channel: 1, //订单渠道： 1-微信， 2-支付宝， 3- 其他
						deliver_user: name, //收货人姓名
						deliver_address: address, //收货地址
						deliver_phone: phone, //收货人电话
						deliver_company: parseInt(companyId), //快递公司 ， 1- 顺丰， 2-其他
					},

					success: function(data) {
						console.log('data', data);
						if(data.resultCode == 'SUCCESS') {
							console.log('get data success');
							appId = data.appId;
							paySign = data.paySign;
							timeStamp = data.timeStamp;
							nonceStr = data.nonceStr;
							packageStr = data.packageStr;
							signType = data.signType;
							out_trade_no = data.out_trade_no;

							if(window.localStorage) {
								//把订单号用localStorage存储
								localStorage.orderNo = out_trade_no;
								var openId = '${openId}';
								localStorage.openId = openId;
							} else {
								//浏览器不支持 不存储
							}

							callpay();
						} else {
							console.log('get data failed');
							alert("统一下单失败");
						}
					}
				});
			}

			function callpay() {
				if(typeof WeixinJSBridge == "undefined") {
					console.log('weixinjsbridge undefined');
					if(document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
					} else if(document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
						document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
					}
				} else {
					console.log('onbridgeready');
					onBridgeReady();
				}
			}

			function onBridgeReady() {
				console.log('invoke weixin pay');
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": appId, //公众号名称，由商户传入
						"paySign": paySign, //微信签名
						"timeStamp": timeStamp, //时间戳，自1970年以来的秒数
						"nonceStr": nonceStr, //随机串
						"package": packageStr, //预支付交易会话标识
						"signType": signType //微信签名方式
					},
					function(res) {
						if(res.err_msg == "get_brand_wcpay_request:ok") {
							window.location.replace("pay_success.html");
							alert('支付成功');

						} else if(res.err_msg == "get_brand_wcpay_request:cancel") {
							alert('支付取消');
							window.location.replace("pay_failed.html");
						} else if(res.err_msg == "get_brand_wcpay_request:fail") {
							alert('支付失败');
							window.location.replace("pay_failed.html");
						} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
					}
				);
			}

			//网页加载时调用
			$(document).ready(function() {
				setAddress();
				setProdutDetail();
				getDeliverFee();

				console.log('openId', '${openId}');
			});

			//点击设置跳转到set_address页面
			$("#setaddress").click(function() {
				window.location.replace("set_address.html");
			})

			var name;
			var phone;
			var address;
			//设置地址
			function setAddress() {
				//取出localStorage存储的值 并填入
				name = localStorage["name"];
				phone = localStorage["phone"];
				address = localStorage["address"];

				if(name != undefined && phone != undefined && address != undefined) {
					$("#name").text(name);
					$("#phone").text(phone);
					$("#address").text(address);
				} else {
					//直接从服务器获取
				}
			}

			//设置产品信息
			function setProdutDetail() {
				var productName = localStorage["productName"];
				var productPrice = localStorage["productPrice"];
				$("#icQuantity").text(1);
				if(productName != undefined && productPrice != undefined) {
					$("#productName").text(productName);
					$("#productPrice").text(productPrice);
				} else {
					//直接从服务器获取
				}
			}

			var quantity = parseInt($("#quantity").val())
			//点击更改数量
			$("#add").click(function() {
				quantity++;
				$("#quantity").val(quantity);
				$("#icQuantity").text(quantity);
				$("#min").removeAttr("disabled"); //当按加1时，解除$("#min")不可读状态  
				setTotal();
			})
			$("#min").click(function() {
				if(quantity > 1) { //判断数量值大于1时才可以减少  
					quantity--;
					$("#quantity").val(quantity);
					$("#icQuantity").text(quantity);
				} else {
					$("#min").attr("disabled", "disabled") //当$("#min")为1时，$("#min")不可读状态  
				}
				setTotal();
			})

			var companyId;
			var deliverFee;
			var deliverCompanies;
			//获取快递价格
			function getDeliverFee() {
				var url = 'http://wx.cooldrivenavi.com/WxPay/getDeliveryCompanyList.action';
				$.ajax({
					type: "post",
					url: url,
					dataType: "json",
					data: {},

					success: function(data) {
						console.log('data', data);
						if(data.resultCode == 'SUCCESS') {
							deliverCompanies = data.deliverCompanies;
							for(var d = 0, len = deliverCompanies.length; d < len; d++) {
								var opt = ['<option value="' + data.deliverCompanies[d].company_id + '">' + data.deliverCompanies[d].company_name + '</option>', ].join("");
								$('#company').append(opt);
							}
							companyId = deliverCompanies[0].company_id;
							deliverFee = deliverCompanies[0].deliver_fee;
							$("#deliverFee").text(deliverFee);
							setTotal();
						} else {
							console.log('get deliverFee failed');
						}
					}
				});
			}

			//选择快递价格 
			function selectFee() {
				companyId = document.getElementById("company").value;
				for(var d = 0; d < deliverCompanies.length; d++) {
					if(data.deliverCompanies[d].company_id = companyId) {
						deliverFee = deliverCompanies[d].deliver_fee;
					}
				}
				$("#deliverFee").text(deliverFee);
				setTotal();
			}

			//计算总价
			function setTotal() {
				var productName = localStorage["productName"];
				var unitPrice = localStorage["unitPrice"];

				if(productName != "" && productName != undefined && unitPrice != "" && unitPrice != undefined) {
					$("#productName").text(productName);
					$("#productPrice").text(unitPrice);
					//					$("#discounts").text("100.00");
					console.log("total", quantity + "," + unitPrice + "," + deliverFee);
					$("#total").text((quantity * parseFloat(unitPrice) + parseFloat(deliverFee)).toFixed(2));
				}

			}

			//获取留言
			function method() {
				var ask = document.getElementById("askFor").value;
				console.log(ask);
			}

			//点击支付
			$("#pay").click(function() {
				console.log("微信支付");
				if(name != "" && name != undefined && phone != "" && phone != undefined && address != "" && address != undefined) {
					pay();
				} else {
					alert("请填写收货地址！");
				}
			})

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

	</body>

</html>