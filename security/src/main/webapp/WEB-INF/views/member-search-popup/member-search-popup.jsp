<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<c:url value='/resources/css/ui/member-search-popup/member-search-popup.css'/>" />
<script charset="utf-8" src="<c:url value='/resources/scripts/ui/member-search-popup/member-search-popup.js'/>"></script>

<div id="layer-mem-search" class="pop-layer">
	<div class="pop-container">
		<div class="data_box">
			<header class="title">
				<span class="title_font">계정 조회</span>
			</header>
			
			<section>
				<article>
					<form id="memberSearchForm">
						<label for="">· 아이디</label><input type="text" id="memId" class="" name="memId" value="" />
						<label for="">· 닉네임</label><input type="text" id="memNicknm" class="" name="memNicknm" value="" />
						<button id="memberSearchButton">검색</button>
					</form>
				</article>
				<article id="data_member" class="data_body">
				
				</article>
				<article>
					<p>
						<span>원하는 계정을 더블 클릭 하세요.</span>
					</p>
					<button class="btn-layer-close">닫기</button>
				</article>
			</section>
		</div>
	</div>
</div>