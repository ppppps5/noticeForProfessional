<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <div id="newscttReadListPopup" class="zemos_modal">
    	<div class="zemos_modal_content">
	    	<div class="zemos_title1">
				<span class="zemos_title1_middle">조회인원</span>
				<span class="zemos_title1_left"><a href="#" onclick="javascript:$('.zemos_modal').hide(); return false;"><img src="${pageContext.request.contextPath}/images/egovframework/zemos3/icon_pre_w.png" alt="이전"/></a></span>
			</div>
			<div id="newscttReadListPopupListDiv" style="width: 100%; overflow: auto;">
				<table class="zemos_table1" style="margin-top: 0px; border-top-width: 0px;">
			        <colgroup>
				        <col width="10%" />
				        <col width="20%" />
				        <col width="20%" />
				        <col width="30%" />
				        <col width="20%" />
			        </colgroup>
			        <thead>
			        	<tr>
			        		<th>번호</th>
			        		<th>법인</th>
			        		<th>거래처</th>
			        		<th>현장</th>
			        		<th>이름</th>
			        		<th>조회일시</th>
			        	</tr>
			        </thead>
			        <tbody>
			        	<c:choose>
							<c:when test="${fn:length(newscttReadList) > 0}">
								<c:forEach items="${newscttReadList}" var="item" varStatus="status">
									<tr>
						                <td>${status.count}</td>
						                <td>${item.BU_NAME}</td>
						                <td>${item.CUST_NAME}</td>
						                <td>${item.CUSTWO_NAME}</td>
						                <td>${item.NAME}</td>
						                <td>${fn:replace(item.REG_DT,'.0','')}</td>
						            </tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="3">조회 결과가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
			        </tbody>
			    </table>
			</div>
		</div>
	</div>