<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosTop.jsp"%>

<!DOCTYPE html>
<html>
<head>

	<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosHeader.jsp"%>
	
	<!-- 업로드 -->
	<%@include file="/WEB-INF/jsp/egovframework/zemos3/com/zemosUpload.jsp"%>
	
	<!-- CK Editor (에디터 사용시 주석해제) -->
	<script type='text/javascript' src="${pageContext.request.contextPath}/engine/ckeditor/ckeditor.js"></script>
	
	<!-- MobiScroll (날짜필드 사용시 주석해제) -->
	<script type='text/javascript' src='${pageContext.request.contextPath}/engine/mobiscroll/Scripts/mobiscroll-2.13.2.full.min.js'></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/engine/mobiscroll/Content/mobiscroll-2.13.2.full.min.css"/>
	
	<script type="text/javascript">
	
		// 전역변수
		$(document).ready(function () {
			
			// 시작일
			$('#dataNoticeStart').mobiscroll().date({
			
		        theme		: 'android-holo-light',
		        display		: 'modal',
		        mode		: 'scroller',
		        dateOrder	: 'yy mm dd',
		        dateFormat 	: "yy-mm-dd",
		        maxDate		: new Date(2099, 11, 31), // 2099-12-31 까지
		        
				onSelect : function (valueText, inst) {
					
					var vStartDt 	= $('#dataNoticeStart').val().replace(/-/gi, "");
					var vEndDt 		= $('#dataNoticeEnd').val().replace(/-/gi, "");
					if (vStartDt != "" && vEndDt != "" && Number(vStartDt) > Number(vEndDt)) {
					
						$('#dataNoticeEnd').val($('#dataNoticeStart').val());
						$('#dataNoticeEnd').mobiscroll("setDate", new Date($('#dataNoticeStart').val()));
					}
			    }
		    });
			
			// 종료일
			$('#dataNoticeEnd').mobiscroll().date({
			
		        theme		: 'android-holo-light',
		        display		: 'modal',
		        mode		: 'scroller',
		        dateOrder	: 'yy mm dd',
		        dateFormat 	: "yy-mm-dd",
		        maxDate		: new Date(2099, 11, 31), // 2099-12-31 까지
		        
				onSelect : function (valueText, inst) {
					
					var vStartDt 	= $('#dataNoticeStart').val().replace(/-/gi, "");
					var vEndDt 		= $('#dataNoticeEnd').val().replace(/-/gi, "");
					if (vStartDt != "" && vEndDt != "" && Number(vStartDt) > Number(vEndDt)) {
					
						$('#dataNoticeStart').val($('#dataNoticeEnd').val());
						$('#dataNoticeStart').mobiscroll("setDate", new Date($('#dataNoticeEnd').val()));
					}
			    }
		    });
			
			// 내용
			var imageUploadUrl = fn_get_context_path() + '/com/file/uploadCkTemp.do?fileOwner=${FILE_OWNER_PREFIX}${resultMap.IDX}&fileSe=img';
			
			var parentIdx = "${parentIdx}";
			if (parentIdx != '' && parentIdx != "") {
				imageUploadUrl = fn_get_context_path() + '/com/file/uploadCkTemp.do?fileOwner=${FILE_OWNER_PREFIX}' + parentIdx + '&fileSe=img';
			}
			
			CKEDITOR.config.imageUploadUrl = imageUploadUrl;
			CKEDITOR.replace( 'dataEditor',{
			
				customConfig 	: '${pageContext.request.contextPath}/engine/ckeditor/config.js',
				width			: '100%',
				height			: '350px',
				startupFocus 	: true,
				
				on : {
					
					instanceReady : function ( evt ) {
						
						// 상단바 하단바 display none
						document.getElementById( evt.editor.id+'_top' ).style.display 	 = 'none';
						document.getElementById( evt.editor.id+'_bottom' ).style.display = 'none';
					}
				}
			});
		});
		
		// 파일 삭제
		function fn_file_delete(pIdx) {
			
			if (!fn_confirm("해당 파일을 삭제하시겠습니까?")) {
				return;
			}
			
			$("#dataFile_" + pIdx).remove();
			$("#dataFile_" + pIdx + "_tr").remove();
		}
		
		// 저장
		function fn_save(pGubun) {
			
			if (pGubun == "delete") {
				
				if (!fn_confirm("삭제하시겠습니까?")) {
					return;
				}
			} else {
				
				if ( $("#dataBbscttTitle").val().trim() == "" ) {
					
					fn_alert("제목을 입력해 주세요.");
					$("#dataBbscttTitle").focus();
					return;
				}
				
				var vEditor = CKEDITOR.instances['dataEditor'];
				var vData 	= vEditor.getData();
				if ( vData.trim() == "" ) {
					
					fn_alert("내용을 입력해 주세요.");
					vEditor.focus();
					return;
				}
			}
			
			if ($("#gvSelectFlag").val() != "stop") {
				return;
			}
			
			$("#gvSelectFlag").val("start");
			fn_loading_show();
			
			var params 		= {};
			params.gubun 	= pGubun;
			if (pGubun == "delete") {
				
				params.idx 		= "${resultMap.IDX}";
				params.useYn 	= 'N';
				params.deleteYn = 'Y';
			} else {
				
				params.idx 			= "${resultMap.IDX}";
				params.bbscttTitle 	= $("#dataBbscttTitle").val();
				
				var editor = CKEDITOR.instances['dataEditor'];
				params.bbscttContents 	= editor.getData();
				params.secretYn 		= "N";
				params.noticeYn 		= $("#dataNoticeYn").is(":checked") ? "Y" : "N";
				params.noticeStart 		= $("#dataNoticeStart").val();
				params.noticeEnd 		= $("#dataNoticeEnd").val();
				params.ntcnYn 			= $("#dataNtcnYn").is(":checked") ? "Y" : "N";
				params.enfrcNtcnYn 		= $("#dataEnfrcNtcnYn").is(":checked") ? "Y" : "N";
				params.useYn 			= 'Y';
				params.deleteYn 		= 'N';
				
				var vBbscttTagList = [];
				var vBbscttTag 			= $("#dataBbscttTag").val().trim();
				var vBbscttTagSplit1 	= vBbscttTag.split("#");
				for (var i = 0; i < vBbscttTagSplit1.length; i++) {
					
					var vBbscttTagSplit2 = vBbscttTagSplit1[i].split(",");
					for (var j = 0; j < vBbscttTagSplit2.length; j++) {
						
						var vBbscttTagData = {};
						vBbscttTagData.tagNm = vBbscttTagSplit2[j].trim();
						vBbscttTagList.push(vBbscttTagData);
					}
				}
				
				params.bbscttTagListLength 	= vBbscttTagList.length;
				params.bbscttTagList 		= vBbscttTagList;
				
				var vFileList = [];
				$("#dataFileList input[name='dataFileList']").each(function() {
					
					var vFileData = {};
					vFileData.idx = $(this).val();
					vFileList.push(vFileData);
				});
				
				params.fileListLength 	= vFileList.length;
				params.fileList 		= vFileList;
			}
			
			// ajax 시작
			$.ajax({
				
			    url			: "${pageContext.request.contextPath}/zemos3/family/news/newscttSaveSave.do",
			    type		: 'post',
			    dataType 	: 'json',
			    data		: params
			}).always(function(data) { // 전 처리 (완료 실패 모든 상황에서 실행됨)
			
				fn_loading_hide();
				$("#gvSelectFlag").val("stop");
			}).done(function (data) { // 완료
				
				var result 		= Number(data.result);
				var resultMsg 	= data.resultMsg;
				if (result > 0) {
					fn_back();
				} else {
					fn_alert(resultMsg);
				}
			}).fail(function (data) { // 실패
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
		<span class="zemos_title1_middle" onclick="javascript:fn_location_href();return false;">
			전문직 공지사항
		</span>
		<span class="zemos_title1_left"><a href="#" onclick="javascript:fn_back();return false;"><img src="${pageContext.request.contextPath}/images/egovframework/zemos3/icon_pre_w.png" alt="이전"/></a></span>
		<span class="zemos_title1_right" style="display: none;">
			<a href="#" onclick="javascript:fn_location_href('${pageContext.request.contextPath}/zemos3/zemos/menu/menuManager2.do?zemosIdx=${params.zemosIdx[0]}&zemosNm='+encodeURIComponent('${params.zemosNm[0]}')); return false;">
				<img src="${pageContext.request.contextPath}/images/egovframework/zemos3/menu01${menu01}.png" alt="홈">
			</a>
		</span>
	</div>
    <!--타이틀끝-->
    
    <!--변수선언-->
	<input type="hidden" id="dataIdx" value="${resultMap.IDX}">
	<!--변수선언끝-->
	
    <!--메뉴리스트시작-->
    <div class="zemos_form_header1">
        <p class="zemos_form_header1_left">게시글 입력</p>
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
                    <td>
	                    <input id="dataBbscttTitle" type="text" class="zemos_input1" style="width: 100%;" value="${resultMap.BBSCTT_TITLE}">
                    </td>
                </tr>
                <tr>
                    <th>태그</th>
                    <td>
	                    <input id="dataBbscttTag" type="text" class="zemos_input1" style="width: 100%;" value="<c:forEach items="${bbscttTagList}" var="item" varStatus="status"><c:if test="${status.count > 1}">,</c:if>#${item.TAG_NM}</c:forEach>">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="border-left-width: 0px; border-bottom-width: 0px;">
	                    <textarea id="dataEditor" rows="" cols="" style="">${resultMap.BBSCTT_CONTENTS}</textarea>
	                    <form id="dataEditorImageForm" method="post" enctype="multipart/form-data" action="" style="display:none;">
							<input type="file" id="dataEditorImage" multiple="multiple">
			        	</form>
                    </td>
                </tr>
                <c:choose>
	                <c:when test="${mobileYn == 'Y'}">
		                <tr>
		                    <td colspan="2" style="border-left-width: 0px;">
			                    <div class="fl" style="width: 49%;">
				                    <a href="#" onclick="javascript:fn_editor_image_upload('camera'); return false;" class="zemos_btn_small1_grey" style="width: 100%;">카메라</a>
			                    </div>
			                    <div class="fr"  style="width: 49%;">
				                    <a href="#" onclick="javascript:fn_editor_image_upload('image'); return false;" class="zemos_btn_small1_grey" style="width: 100%;">이미지</a>
			                    </div>
		                    </td>
		                </tr>
	                </c:when>
	                <c:otherwise>
	                	<tr>
	                		<td colspan="2" style="border-left-width: 0px;">
	                			<a href="#" onclick="javascript:fn_editor_image_upload('image'); return false;" class="zemos_btn_small1_grey" style="width: 100%;">이미지</a>
		                    </td>
	                	</tr>
	                </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    <!--서브메뉴끝-->
    
    <!--메뉴리스트시작-->
    <div class="zemos_form_header1">
        <p class="zemos_form_header1_left">첨부</p>
        <p class="zemos_form_header1_right"></p>
    </div>
    
    <form id="dataFileForm" method="post" enctype="multipart/form-data" action="" style="display:none;">
		<input type="file" id="dataFile" multiple="multiple">
	</form>
	
	<div id="dataFileList">
		<c:forEach items="${fileList}" var="item" varStatus="status">
			<input type="hidden" name="dataFileList" id="dataFile_${item.IDX}" value="${item.IDX}">
		</c:forEach>
	</div>
    <!--메뉴리스트끝-->
    
    <!--서브메뉴시작-->
    <div class="zemos_form2">
        <table class="zemos_table2" id="dataFileTable">
            <colgroup>
	            <col width="100%" />
	            <col width="*" />
            </colgroup>
            <tbody>
				<tr>
                    <td colspan="2" style="border-left-width: 0px;">
                    	<div class="fl"  style="width: 100%;">
		                    <a href="#" onclick="javascript:fn_file_upload('file'); return false;" class="zemos_btn_small1_grey" style="width: 100%;">파일선택</a>
	                    </div>
                    </td>
                </tr>
				<c:forEach items="${fileList}" var="item" varStatus="status">
				    <tr id="dataFile_${item.IDX}_tr">
					    <td style="border-left-width: 0px; cursor: pointer;" onclick="javascript:fn_mobile_download('${common.fullContext}/com/file/download.do?idxVal=${item.IDX_VAL}');">
					    	<c:choose>
								<c:when test="${item.FILE_SE == 'img'}">
									<img src="${pageContext.request.contextPath}/com/file/download.do?idxVal=${item.IDX_VAL}" style="max-width: 100%;">
								</c:when>
								<c:otherwise>
									${fn:substring(item.FILE_NM, 0, 25)}
									<c:if test="${fn:length(item.FILE_NM) > 25}">...</c:if>
								</c:otherwise>
							</c:choose>
						</td>
						<td style="padding: 5px 1px; border-left-width: 0px;">
							<a href="#" onclick="javascript:fn_file_delete('${item.IDX}'); return false;" class="zemos_btn_small1_grey" style="width: 50px;">삭제</a>
		                </td>
					</tr>
				</c:forEach>
            </tbody>
        </table>
    </div>
    <!--서브메뉴끝-->
    
    <%-- 파트장(zemosMainMngrYn) /  잡컨설턴트(zemosMngrYn) / 대행 잡컨설턴트(zemosAuth:21000) --%>
	<c:if test="${loginVO.zemosMainMngrYn == 'Y' || loginVO.zemosMngrYn == 'Y' || loginVO.zemosAuth == '21000' }">
	
	    <!--서브메뉴시작-->
	    <div class="zemos_form2">
	        <table class="zemos_table2" id="dataTable1" style="display: none;">
	            <colgroup>
		            <col width="99%" />
		            <col width="*" />
	            </colgroup>
	            <tbody>
	            
		            <c:choose>
	    				<c:when test="${bbsAuthMap.AUTH6_YN == 'Y'}">
				            <tr>
				                <td style="text-align:left; border-left-width: 0px;">
				                	공지
				                </td>
				                <td style="text-align:right; border-left-width: 0px;">
			                		<label class="zemos_switch">
				                		<input type="checkbox" id="dataNoticeYn" <c:if test="${resultMap.NOTICE_YN == 'Y'}">checked="checked"</c:if>>
				                		<span class="zemos_slider zemos_round"></span>
									</label>
				                </td>
				            </tr>
				            <tr>
				                <td style="text-align:left; border-left-width: 0px;" colspan="2">
				                	<span style="float:left; width: 50px; line-height: 30px;">공지기간</span>
									<input id="dataNoticeEnd" type="text" class="zemos_input1 tc" style="float: right; width:90px;" value="${fn:substring(resultMap.NOTICE_END,0,10)}" placeholder="종료일 입력" onKeyPress="javascript:if(event.keyCode == 13) fn_reload('select');" onfocus="$(this).select();">
									<span style="float: right; width: 10px; line-height: 30px; text-align: center;">~</span>
									<input id="dataNoticeStart" type="text" class="zemos_input1 tc" style="float: right; width:90px;" value="${fn:substring(resultMap.NOTICE_START,0,10)}" placeholder="시작일 입력" onKeyPress="javascript:if(event.keyCode == 13) fn_reload('select');" onfocus="$(this).select();">
				                </td>
				            </tr>
						</c:when>
						<c:otherwise>
							<tr style="display: none;">
				                <td style="display: none;" colspan="2">
				                	<input type="checkbox" id="dataNoticeYn" style="display: none;">
				                	<input type="hidden" id="dataNoticeStart" style="display: none;" value="${fn:substring(resultMap.NOTICE_START,0,10)}">
				                	<input type="hidden" id="dataNoticeEnd" style="display: none;" value="${fn:substring(resultMap.NOTICE_END,0,10)}">
				                </td>
				            </tr>
						</c:otherwise>
					</c:choose>
		            <tr>
		                <td style="text-align:left; border-left-width: 0px;">
		                	알림
		                </td>
		                <td style="text-align:right; border-left-width: 0px;">
	                		<label class="zemos_switch">
		                		<input type="checkbox" id="dataNtcnYn" <c:if test="${resultMap.NTCN_YN != 'N'}">checked="checked"</c:if>>
		                		<span class="zemos_slider zemos_round"></span>
							</label>
		                </td>
		            </tr>
		            <c:choose>
		            	<%-- 잡컨설턴트(zemosMngrYn) / 대행 잡컨설턴트(zemosAuth:21000) --%>
	    				<c:when test="${loginVO.zemosMngrYn == 'Y' || loginVO.zemosAuth == '21000'}">
				            <tr>
				                <td style="text-align:left; border-left-width: 0px;">
				                	강제알림
				                </td>
				                <td style="text-align:right; border-left-width: 0px;">
			                		<label class="zemos_switch">
				                		<input type="checkbox" id="dataEnfrcNtcnYn" <c:if test="${resultMap.ENFRC_NTCN_YN == 'Y'}">checked="checked"</c:if>>
				                		<span class="zemos_slider zemos_round"></span>
									</label>
				                </td>
				            </tr>
		            	</c:when>
		            	<c:otherwise>
		            		<tr style="display: none;">
				                <td style="display: none;" colspan="2">
				                	<input type="checkbox" id="dataEnfrcNtcnYn" style="display: none;">
				                </td>
				            </tr>
		            	</c:otherwise>
		            </c:choose>
	            </tbody>
	        </table>
	    </div>
	    <!--서브메뉴끝-->
	    
    </c:if>
    <!--서브메뉴끝-->
    
	<c:choose>
		<%-- 파트장(zemosMainMngrYn) /  잡컨설턴트(zemosMngrYn) / 대행 잡컨설턴트(zemosAuth:21000) --%>
		<c:when test="${(loginVO.zemosMainMngrYn == 'Y' || loginVO.zemosMngrYn == 'Y' || loginVO.zemosAuth == '21000' || resultMap.REG_IDX == loginVO.idx) && resultMap.DELETE_YN != 'N'}">
			<div class="zemos_btn_mid1">
		    	<p class="zemos_btn_mid1_blue1">
		        	<a href="#" onclick="javascript:fn_save('insert'); return false;">등록</a>
		        </p>
		    </div>
		</c:when>
		<%-- 파트장(zemosMainMngrYn) /  잡컨설턴트(zemosMngrYn) / 대행 잡컨설턴트(zemosAuth:21000) --%>
		<c:when test="${(loginVO.zemosMainMngrYn == 'Y' || loginVO.zemosMngrYn == 'Y' || loginVO.zemosAuth == '21000' || resultMap.REG_IDX == loginVO.idx) && resultMap.DELETE_YN == 'N' || loginVO.zemosOwnerYn == 'Y'}">
		    <div class="zemos_btn_mid1">
		    	<p class="zemos_btn_mid1_red2">
		        	<a href="#" onclick="javascript:fn_save('update'); return false;">수정</a>
		        </p>
		        <p class="zemos_btn_mid1_grey2">
		        	<a href="#" onclick="javascript:fn_back(); return false;">취소</a>
		        </p>
		    </div>
		</c:when>
	</c:choose>
	
</body>
</html>