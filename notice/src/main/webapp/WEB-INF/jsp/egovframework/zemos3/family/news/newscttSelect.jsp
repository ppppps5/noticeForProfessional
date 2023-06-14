<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosTop.jsp"%>

<!-- 데이터 개행 변환 (사용시 주석해제) -->
<%
      pageContext.setAttribute("cn", "\n"); //Space, Enter
      pageContext.setAttribute("br", "<br/>"); //br 태그
%>
<!DOCTYPE html>
<html>
<head>

<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosHeader.jsp"%>

<script type="text/javascript">


			$(document).ready(function () {
				
				// 이미지 최대 크기 조정
				$("#dataBbscttContents img").css("max-width","100%");
				$("#dataBbscttContents img").css("height","auto");
				
				// 이미지 상대경로 절대경로로 변경
				$("#dataBbscttContents img").each(function() {
					var vSrc = $(this).attr("src");
					vSrc = vSrc.replace("\\\"", "");
					vSrc = vSrc.replace("../../", "http://www.zeniel.net/");
					vSrc = vSrc.replace("\\\"", "");
					$(this).attr("src", vSrc);
				});
				
				// 링크 상대경로 절대경로로 변경
				$("#dataBbscttContents a").each(function() {
					var vHref = $(this).attr("href");
					if(vHref.startsWith("/")) {
						vHref = "http://www.zeniel.net"+vHref;
					}
					$(this).attr("href", "javascript:void(0);");
					$(this).click(function () {
						fn_mobile_download(vHref);
						return false;
					});
				});
			});
		
		// 전역변수
		var ntceSe 		= "${resultMap.BBSCTT_SE}";
		var ntceSeNm 	= "${params.ntceSeNm[0]}";
		var zemosIdx 	= "${params.zemosIdx[0]}";
		var zemosNm 	= "${params.zemosNm[0]}";
		var wrkplcIdx 	= "${resultMap.WRKPLC_IDX}";
		var news 		= "${news}";
		
	
		$(document).ready(function () {
			
			// 팝업 조회 박스 크기
			$("#bbscttReadListPopupListDiv").height(fn_get_window_height()-200);
			
			// 댓글 창 스크롤 맨밑
			$("#dataAnswerDiv").animate({ scrollTop: $('#dataAnswerDiv').prop("scrollHeight")}, 0);
		});
		
		// 조회
		function fn_select(pIdx) {
			
			if (pIdx == null || pIdx == "" || pIdx == "undefined") {

				fn_alert("게시글이 존재하지 않습니다.");
				return;
			}

			var vUrl = "${pageContext.request.contextPath}/zemos3/family/news/newscttSelect.do";
			vUrl += "?idx=" 		+ pIdx;
			vUrl += "&news=" 		+ news;
			vUrl += "&zemosIdx=" 	+ zemosIdx;
			vUrl += "&zemosNm=" 	+ encodeURIComponent(zemosNm);
			
			fn_location_href(vUrl);
		}

		// 수정
		function fn_update() {
			
			var vUrl = "${pageContext.request.contextPath}/zemos3/family/news/newscttSave.do";
			vUrl += "?idx=${resultMap.IDX}";
			vUrl += "&news=" 		+ news;
			vUrl += "&zemosIdx=" 	+ zemosIdx;
			vUrl += "&zemosNm=" 	+ encodeURIComponent(zemosNm);
			
			fn_location_href(vUrl);
		}

		// 삭제
		function fn_delete() {
			
			if (!fn_confirm("삭제하시겠습니까?")) {
				return;
			}

			if ($("#gvSelectFlag").val() != "stop") {
				return;
			}
			
			$("#gvSelectFlag").val("start");
			fn_loading_show();

			var params = {};
			params.gubun 	= "delete";
			params.idx 		= "${resultMap.IDX}";
			params.useYn 	= 'N';
			params.deleteYn = 'Y';

			// ajax 시작
			$.ajax({
				
				url : "${pageContext.request.contextPath}/zemos3/family/news/newscttSaveSave.do",
				type : 'post',
				dataType : 'json',
				data : params
			}).always(function(data) { // 전 처리 (완료 실패 모든 상황에서 실행됨)
			
				fn_loading_hide();
				$("#gvSelectFlag").val("stop");
			}).done(function(data) { // 완료
				
				var result = Number(data.result);
				var resultMsg = data.resultMsg;
				if (result > 0) {
					fn_back();
				} else {
					fn_alert('savesave걸림');
					fn_alert(resultMsg);
				}
			}).fail(function(data) { // 실패
				fn_location_href("${pageContext.request.contextPath}/");
			}).always(function(data) { // 후 처리 (완료 실패 모든 상황에서 실행됨)
				
			});
			// ajax 끝
		}
	</script>
</head>
<body>
	<!--상단시작-->
	<%@include file="/WEB-INF/jsp/egovframework/zemos3/main/mainTop2.jsp"%>
	<!--상단끝-->
	<!--타이틀시작-->
	<div class="zemos_title1">
		<span class="zemos_title1_middle"
			onclick="javascript:fn_location_href();return false;">전문직 공지사항</span>
		<span class="zemos_title1_left"><a href="#"
			onclick="javascript:fn_back();return false;"><img
				src="${pageContext.request.contextPath}/images/egovframework/zemos3/icon_pre_w.png"
				alt="이전" /></a></span> <span class="zemos_title1_right" style="display: none;">
			<a href="#"
			onclick="javascript:fn_location_href('${pageContext.request.contextPath}/zemos3/zemos/menu/menuManager2.do?zemosIdx=${params.zemosIdx[0]}&zemosNm='+encodeURIComponent('${params.zemosNm[0]}')); return false;">
				<img
				src="${pageContext.request.contextPath}/images/egovframework/zemos3/menu01${menu01}.png"
				alt="홈">
		</a>
		</span>
	</div>
	<!--타이틀끝-->
	<!--변수선언-->
	<input type="hidden" id="dataIdx" value="${resultMap.IDX}">

	<!--변수선언끝-->
	<!--메뉴리스트시작-->
	<div class="zemos_form_header1">
		<p class="zemos_form_header1_left"></p>
		<p class="zemos_form_header1_right"></p>
	</div>
	<!--메뉴리스트끝-->
	<!--서브메뉴시작-->
	<div class="zemos_form2">
		<table class="zemos_table2">
			<colgroup>
				<col width="30%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>제목</th>
					<td>${resultMap.BBSCTT_TITLE}</td>
				</tr>
				<tr>
                    <th>태그</th>
                    <td>
                    	<c:forEach items="${bbscttTagList}" var="item" varStatus="status"><c:if test="${status.count > 1}">,</c:if>#${item.TAG_NM}</c:forEach>
                    </td>
                </tr>
				<tr>
					<th>일자</th>
					<td>
						${fn:substring(resultMap.REG_DT,0,4)}.${fn:substring(resultMap.REG_DT,5,7)}.${fn:substring(resultMap.REG_DT,8,10)}
					</td>
				</tr>
				<tr>
					<td id="dataBbscttContents" colspan="2" 
						style="width: 100%; padding: 20px; overflow: auto;">
						${resultMap.BBSCTT_CONTENTS}
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!--서브메뉴끝-->
	<!--메뉴리스트시작-->
	<div class="zemos_form_header1">
		<p class="zemos_form_header1_left">첨부</p>
		<p class="zemos_form_header1_right"></p>
	</div>
	<form id="dataFileForm" method="post" enctype="multipart/form-data"
		action="" style="display: none;">
		<input type="file" id="dataFile" multiple="multiple">
	</form>
	<div id="dataFileList">
		<c:forEach items="${fileList}" var="item" varStatus="status">
			<input type="hidden" name="dataFileList" id="dataFile_${item.IDX}"
				value="${item.IDX}">
		</c:forEach>
	</div>
	<!--메뉴리스트끝-->
	<!--서브메뉴시작-->
	<div class="zemos_form2">
		<table class="zemos_table2" id="dataFileTable"
			style="margin-bottom: 20px;">
			<colgroup>
				<col width="100%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<c:choose>
					<c:when test="${fn:length(fileList) > 0}">
						<c:forEach items="${fileList}" var="item" varStatus="status">
							<tr id="dataFile_${item.IDX}_tr">
								<td style="border-left-width: 0px; cursor: pointer;"
									onclick="javascript:fn_mobile_download('${common.fullContext}/com/file/download.do?idxVal=${item.IDX_VAL}');">
									<c:choose>
										<c:when test="${item.FILE_SE == 'img'}">
											<img
												src="${pageContext.request.contextPath}/com/file/download.do?idxVal=${item.IDX_VAL}"
												style="max-width: 100%;">
										</c:when>
										<c:otherwise>
											${fn:substring(item.FILE_NM, 0, 25)}
											<c:if test="${fn:length(item.FILE_NM) > 25}">...</c:if>
										</c:otherwise>
									</c:choose>
								</td>
								<td style="padding: 5px 1px; border-left-width: 0px;"><a
									href="#"
									onclick="javascript:fn_mobile_download('${common.fullContext}/com/file/download.do?idxVal=${item.IDX_VAL}'); return false;"
									class="zemos_btn_small1_blue" style="width: 50px;">다운</a></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="2" style="border-left-width: 0px;">첨부 파일이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
	<!--서브메뉴끝-->
	<!--서브메뉴시작-->
	<div class="zemos_btn_mid1">
	
		<p class="zemos_btn_mid1_grey1">
			<a href="#" onclick="javascript:fn_back(); return false;">목록</a>
		</p>
		
		<c:if test="${loginVO.id == 'E210745'|| loginVO.zemosOwnerYn == 'Y'}">
		<p class="zemos_btn_mid1_red2" style="margin-top:10px;">
		   	<a href="#" onclick="javascript:fn_update(); return false;">수정</a>
		</p>
		<p class="zemos_btn_mid1_grey2"style="margin-top:10px;">
		   	<a href="#" onclick="javascript:fn_delete(); return false;">삭제</a>
		</p>
		</c:if>
		
	</div>
	
	<c:if test="${loginVO.id == 'Z210005'|| loginVO.zemosOwnerYn == 'Y'}">
	</c:if>
	
	<c:choose>
		<c:when test="${(loginVO.zemosMainMngrYn == 'Y' || loginVO.zemosMngrYn == 'Y' || loginVO.zemosAuth == '21000' || resultMap.REG_IDX == loginVO.idx) && resultMap.DELETE_YN == 'N'}">
		    <div class="zemos_btn_mid1">
		    	<p class="zemos_btn_mid1_red2">
		        	<a href="#" onclick="javascript:fn_update(); return false;">수정</a>
		        </p>
		        <p class="zemos_btn_mid1_grey2">
		        	<a href="#" onclick="javascript:fn_delete(); return false;">삭제</a>
		        </p>
		    </div>
		</c:when>
		<c:otherwise>
			
		</c:otherwise>
		
	</c:choose>
	
	<!--서브메뉴끝-->
</body>
</html>