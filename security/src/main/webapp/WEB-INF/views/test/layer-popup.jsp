<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>레이어 팝업 예제</title>
		
		<style type="text/css">
			* {
				margin: 0;
				padding: 0;
			}

			body {
				margin: 100px;
			}

			.pop-layer .pop-container {
				padding: 20px 25px;
			}

			.pop-layer p.ctxt {
				color: #666;
				line-height: 25px;
			}

			.pop-layer .btn-r {
				width: 100%;
				margin: 10px 0 20px;
				padding-top: 10px;
				border-top: 1px solid #DDD;
				text-align: right;
			}

			.pop-layer {
				display: none;
				position: absolute;
				top: 50%;
				left: 50%;
				width: 410px;
				height: auto;
				background-color: #fff;
				border: 5px solid #3571B5;
				z-index: 10;
			}

			.dim-layer {
				display: none;
				position: fixed;
				_position: absolute;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				z-index: 100;
			}

			.dim-layer .dimBg {
				position: absolute;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: #000;
				opacity: .5;
				filter: alpha(opacity=50);
			}

			.dim-layer .pop-layer {
				display: block;
			}

			a.btn-layerClose {
				display: inline-block;
				height: 25px;
				padding: 0 14px 0;
				border: 1px solid #304a8a;
				background-color: #3f5a9d;
				font-size: 13px;
				color: #fff;
				line-height: 25px;
			}

			a.btn-layerClose:hover {
				border: 1px solid #091940;
				background-color: #1f326a;
				color: #fff;
			}
		</style>
		
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	</head>
	<body>
		<div style="height: 300px;"></div>
		<a href="#layer1" class="btn-example">일반 팝업레이어</a>
		<div id="layer1" class="pop-layer">
			<div class="pop-container">
				<div class="pop-conts">
					<!--content //-->
					<p class="ctxt mb20">Thank you.<br>
						Your registration was submitted successfully.<br>
						Selected invitees will be notified by e-mail on JANUARY 24th.<br><br>
						Hope to see you soon!
					</p>

					<div class="btn-r">
						<a href="#" class="btn-layerClose">Close</a>
					</div>
					<!--// content-->
				</div>
			</div>
		</div>
		<br/><br/>
		<a href="#layer2" class="btn-example">딤처리 팝업레이어 1</a>
		<div class="dim-layer">
			<div class="dimBg"></div>
			<div id="layer2" class="pop-layer">
				<div class="pop-container">
					<div class="pop-conts">
						<!--content //-->
						<p class="ctxt mb20">Thank you.<br>
							Your registration was submitted successfully.<br>
							Selected invitees will be notified by e-mail on JANUARY 24th.<br><br>
							Hope to see you soon!
						</p>

						<div class="btn-r">
							<a href="#" class="btn-layerClose">Close</a>
						</div>
						<!--// content-->
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function(){
				$('.btn-example').click(function(){
					let href = $(this).attr('href');
					layer_popup(href);
				});
				
				$("#layer1").draggable();	// JQuery UI의 드래그 기능 사용
				$("#layer2").draggable();
			});
			
			function layer_popup(el){
				let $el = $(el);        //레이어의 id로 팝업 DOM을 찾아 $el 변수에 저장
				let isDim = $el.prev().hasClass('dimBg');   //dimmed 레이어를 감지하기 위한 boolean 변수

				isDim ? $('.dim-layer').fadeIn() : $el.fadeIn(); // 팝업 서서히 나타남

				// doubl not bitwise 연산자
				// Math.floor()로 대체.
				// 값을 정수로 변환 -> 소수점 제거
				let elWidth = Math.floor($el.outerWidth());	// 팝업창의 border, padding을 포함한 너비값 산출
				let elHeight = Math.floor($el.outerHeight());	// 팝업창의 border, padding을 포함한 높이값 산출
				let docWidth = $(document).width();		// 브라우저의 padding 안쪽 너비(padding 값 제외)
				let docHeight = $(document).height();		// 브라우저의 padding 안쪽 높이(padding 값 제외)

				// 화면의 중앙에 레이어를 띄운다.
				if (elHeight < docHeight || elWidth < docWidth) {
					$el.css({
						marginTop: -elHeight /2,
						marginLeft: -elWidth/2
					})
				} else {
					$el.css({top: 0, left: 0});
				}

				$el.find('a.btn-layerClose').click(function(){
					isDim ? $('.dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
					return false;
				});

				$('.dim-layer .dimBg').click(function(){
					$('.dim-layer').fadeOut();
					return false;
				});
			}
		</script>
	</body>
</html>