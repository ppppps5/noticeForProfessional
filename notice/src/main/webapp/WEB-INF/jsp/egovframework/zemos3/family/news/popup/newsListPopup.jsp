<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

	<div id="newsListPopup" class="zemos_modal">
    	<div class="zemos_modal_content">
	    	<div class="zemos_title1">
				<span class="zemos_title1_middle">게시판 선택</span>
				<span class="zemos_title1_left"><a href="#" onclick="javascript:$('.zemos_modal').hide(); return false;"><img src="${pageContext.request.contextPath}/images/egovframework/zemos3/icon_pre_w.png" alt="이전"/></a></span>
			</div>
			<div class="zemos_label_search1">
				<span class="fl mb5" style="width:100%; padding-right: 40px;">
					<input id="newsListPopupSelectText" type="text" class="zemos_input1" style="float: right; width:100%;" value="${params.selectText[0]}" placeholder="검색어 입력" onKeyPress="javascript:if(event.keyCode == 13) fn_bbsListPopup_select('select');" onfocus="$(this).select();">
				</span>
				<span class="fr mb5" style="position: absolute; margin-top: 1px; right: 20px;">
					<a href="#" onclick="javascript:fn_newsListPopup_select('select'); return false;" class="zemos_label_search1_search_image"></a>
				</span>
			</div>
			<input id="newsListPopupListCnt" type="hidden" value="${fn:length(newsAuthList)}">
			<div id="newsListPopupListDiv" style="width: 100%; overflow: auto;">
				<div id="newsListPopupList">
					<c:choose>
						<c:when test="${fn:length(newsAuthList) > 0}">
							<%-- 시스템 관리자(systemMgrYn) --%>
							<c:if test="${adminAuth != null && adminAuth != '' && adminAuth == 'systemMgr'}">
								<div class="zemos_naming1" style="border-bottom: 1px solid #e9e9e9; cursor: pointer;" onclick="javascript:fn_reload('bbs','',''); return false;">
									<p style="margin: 5px 0px;">전체 공지</p>
								</div>
							</c:if>
							<c:forEach items="${newsAuthList}" var="item" varStatus="status">
								<div class="zemos_naming1" style="border-bottom: 1px solid #e9e9e9; cursor: pointer;" onclick="javascript:fn_reload('bbs','${item.BBS_IDX}','${item.BBS_SE_IDX}'); return false;">
									<p style="margin: 5px 0px;"><c:if test="${item.ZEMOS_NM != null && item.ZEMOS_NM != ''}">${item.ZEMOS_NM}&nbsp;/&nbsp;</c:if>${item.BBS_NM}</p>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="zemos_naming1" style="border-bottom: 1px solid #e9e9e9; background-image: none;">
								<p style="margin: 5px 0px;">조회 결과가 없습니다.</p>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>