<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosTop.jsp"%>

<!DOCTYPE html>
<html>
<head>

<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosHeader.jsp"%>

<script type="text/javascript">
	
		// 전역변수
		var pageNo 		= "${pageNo}";
		var numOfRows 	= "${numOfRows}";
		var bbsIdx 		= "${bbsIdx}";
		var selectGubun = "${params.selectGubun[0]}";
		var selectText 	= "${params.selectText[0]}";
		
		$(document).ready(function () {
			
		});
		
		// 화면 새로고침 함수
		function fn_reload(type, value1, value2) {
			
			if (type == "pageNo") {
				pageNo = value1;
			}
			
			if (type == "select") {
				
				selectGubun = $("#selectGubun").val();
				selectText = $("#selectText").val();
				pageNo = 1;
			}
			
			if (type == "bbs") {
				
				bbsIdx 		= value1;
				selectGubun = "";
				selectText 	= "";
				pageNo 		= 1;
			}
			
			var url = "${pageContext.request.contextPath}${common.requestURI}";
			url += "?pageNo="		+ pageNo;
			url += "&numOfRows="	+ numOfRows;
			url += "&bbsIdx="		+ bbsIdx;
			url += "&selectGubun="	+ selectGubun;
			url += "&selectText="	+ encodeURIComponent(selectText);
			
			fn_location_href(url); 
		}
		
		function fn_insert() {
			fn_location_href("${pageContext.request.contextPath}/zemos3/family/news/newscttSave.do");
		}
		
		function fn_select(pIdx) {
			
			if (pIdx == null || pIdx == "" || pIdx == "undefined") {
				fn_alert("게시글이 존재하지 않습니다.");
				return;
			}
			
			var vUrl = "${pageContext.request.contextPath}/zemos3/family/news/newscttSelect.do";
			vUrl += "?idx="	+ pIdx;
			
			fn_location_href(vUrl);
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
				alt="이전" /></a></span>
	</div>
	<!--타이틀끝-->
	
	<!--조회조건시작-->
	<div class="zemos_label_search1">
		<span class="fl mb5" style="width: 100%; padding-right: 40px;">
			<select id="selectGubun" class="zemos_select1" style="float: left; width: 30%;">
				<option value="ALL" <c:if test="${params.selectGubun[0] == 'ALL'}">selected</c:if> >전체</option>
				<option value="TITLE" <c:if test="${params.selectGubun[0] == 'TITLE'}">selected</c:if> >제목</option>
				<option value="CONTENTS" <c:if test="${params.selectGubun[0] == 'CONTENTS'}">selected</c:if> >내용</option>
				<option value="TAG" <c:if test="${params.selectGubun[0] == 'TAG'}">selected</c:if> >태그</option>
			</select>
			<input id="selectText" type="text" class="zemos_input1"	style="float: right; width: 69%;" 
			value="${params.selectText[0]}" placeholder="검색어 입력" 
			onKeyPress="javascript:if(event.keyCode == 13) fn_reload('select');"
			onfocus="$(this).select();">
		</span> 
		<span class="fr mb5" style="position: absolute; margin-top: 1px; right: 20px;"> 
			<a href="#" onclick="javascript:fn_reload('select'); return false;" class="zemos_label_search1_search_image"></a>
		</span>
	</div>
	<!--조회조건끝-->
	
	<!--조회된자료시작-->

	<div class="zemos_label_search2">
		<p class="fl" style="padding: 10px 0px;">
			조회<span class="zemos_label_data_grey2">${resultListCnt}</span>건
		</p>


		<c:choose>
			<%-- 파트장(zemosMainMngrYn) /  잡컨설턴트(zemosMngrYn) / 대행 잡컨설턴트(zemosAuth:21000) / 임원(zemosOwnerYn) --%>
			<c:when
				test="${ loginVO.zemosAuth == '21000' || loginVO.zemosOwnerYn == 'Y' || loginVO.zemosMngrYn == 'Y'}">
				<p class="fr" style="padding: 7px 0px;">
					<a href="#" onclick="javascript:fn_insert(); return false;"
						class="zemos_form_span_btn_blue">등록</a>
				</p>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="zemos_form1">
		<table class="zemos_table1">
			<colgroup>
				<col width="*" />
				<col width="30%" />
			</colgroup>
			<thead>
				<tr>
					<th style="padding: 10px 5px;">제목</th>
					<th style="padding: 10px 5px;">일자</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${fn:length(resultList) > 0}">
						<c:forEach items="${resultList}" var="item" varStatus="status">
							<tr style="cursor: pointer;" onclick="javascript:fn_select('${item.IDX}');">
								<td class="tl" style="padding: 10px 5px;">
									${item.BBSCTT_TITLE}</td>
								<td class="tc" style="padding: 10px 5px;">
									<%-- ${item.REG_DT} --%>
									${fn:substring(item.REG_DT,0,4)}.${fn:substring(item.REG_DT,5,7)}.${fn:substring(item.REG_DT,8,10)}
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="2">조회 결과가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<!-- 페이징 처리 -->
		<%@include 
			file="/WEB-INF/jsp/egovframework/zemos3/com/zemosPaging.jsp"%>
		<!-- 페이징 처리 -->
	</div>

</body>
</html>