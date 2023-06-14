package egovframework.zemos3.family.news.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.ZemosCommonConstant;
import egovframework.com.admin.service.AdminOwnerService;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ZemosVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.file.service.FileService;
import egovframework.com.utl.zemosPagingUtil;
import egovframework.com.utl.fcc.service.AES128Util;
import egovframework.com.utl.fcc.service.EgovNumberUtil;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.string.EgovDateUtil;
import egovframework.zemos.zemos.service.ZemosService;
import egovframework.zemos.zemossetup.service.ZemosSetupService;
import egovframework.zemos3.com.bbs.service.Z3BbsService;
import egovframework.zemos3.com.bbs.web.Z3BbsController;
import egovframework.zemos3.family.news.service.Z3NewsService;

@Controller
public class Z3NewsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Z3BbsController.class);

	@Resource(name = "adminOwnerService")
	protected AdminOwnerService adminOwnerService;

	@Resource(name = "fileService")
	private FileService fileService;

	@Resource(name = "z3NewsService")
	private Z3NewsService z3NewsService;

	@Resource(name = "z3BbsService")
	private Z3BbsService z3BbsService;

	@Resource(name = "zemosService")
	protected ZemosService zemosService;

	@Resource(name = "zemosSetupService")
	private ZemosSetupService zemosSetupService;

	private final static String FILE_OWNER_PREFIX = "BBSCTT_";

	// 전문직 공지사항 메인
	@RequestMapping("/zemos3/family/news/news.do")
	public String bbs(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request)
			throws Exception {

		String rtnurl = "egovframework/zemos3/family/news/news";

		/* 페이징(전처리) */
		params = zemosPagingUtil.setPrePagingValue(params);

		// 조회
		params.put("bbsIdx", EgovProperties.getProperty(ZemosCommonConstant.zenielNoticeIdx.toString()));
		int resultListCnt = z3NewsService.newscttListCnt(params);
		List<Map<String, Object>> resultList = z3NewsService.newscttList(params);

		// 리턴
		model.put("resultListCnt", resultListCnt);
		model.put("resultList", resultList);

		/* 페이징(후처리) */
		zemosPagingUtil.setNextPagingValue(params, resultListCnt, model);

		return rtnurl;
	}

	// 전문직 공지사항 권한 목록 조회
	@RequestMapping("/zemos3/family/news/newsAuthList.do")
	public ModelAndView bbsAuthList(ModelMap model, @RequestParam Map<String, Object> params) throws Exception {

		ModelAndView modelAndView = new ModelAndView("jsonView");

		// 로그인 정보
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 조회
		params.put("loginVO", loginVO);
		List<Map<String, Object>> resultList = z3NewsService.newsAuthList(params);

		// 리턴
		modelAndView.addObject("resultList", resultList);

		return modelAndView;
	}

	// 전문직 공지사항 게시글 상세 화면
	@RequestMapping("/zemos3/family/news/newscttSelect.do")

	public String newscttSelect(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request)
			throws Exception {

		String rtnurl = "egovframework/zemos3/family/news/newscttSelect";

		// 로그인 정보
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 파라미터
		String idx = EgovStringUtil.isNullToString(params.get("idx"));

		// 조회
		List<Map<String, Object>> resultList = z3NewsService.newscttSelect(params);

		// 리턴
		if (!EgovStringUtil.isEmptyMapList(resultList)) {

			Map<String, Object> result = resultList.get(0);
			model.put("resultMap", result);

			String userFile = EgovStringUtil.isNullToString(result.get("USER_FILE"));
			if (!EgovStringUtil.isEmpty(userFile)) {

				String userFileRealName = EgovStringUtil.isNullToString(result.get("USER_FILE_REAL_NAME"));
				if (!EgovStringUtil.isEmpty(userFileRealName)) {

					String downUrl = "https://zeniel.net/manager2/bbs/int_bbs/" + userFile;
					model.put("downUrl", downUrl);
				}
			}
		}

		model.put("resultList", resultList);

		params.put("bbscttIdx", idx);
		params.put("userIdx", loginVO.getIdx());

		params.put("regIdx", loginVO.getIdx());
		params.put("editIdx", loginVO.getIdx());
		z3NewsService.newsSelectInsert(params);

		String commentEvent = EgovStringUtil.isNullToString(params.get("commentEvent"));
		if (!EgovStringUtil.isEmpty(commentEvent)) {

			model.put("commentEvent", commentEvent);

			// 댓글 목록
			Map<String, Object> newsAnswerParams = new HashMap<String, Object>();
			newsAnswerParams.put("bbscttIdx", idx);
			List<Map<String, Object>> bbscttEventAnswerList = z3NewsService.newsEventAnswerList(newsAnswerParams);
			model.put("newsEventAnswerList", bbscttEventAnswerList);
		}

		return rtnurl;
	}

	// 전문직 공지사항 등록/수정 화면
	@RequestMapping("/zemos3/family/news/newscttSave.do")
	public String bbscttSave(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request)
			throws Exception {

		LOGGER.debug("############################ newscttSave ####################################################");

		String rtnurl = "egovframework/zemos3/family/news/newscttSave";
		model.put("menu02", "_on");

		// 로그인 정보
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 파라미터
		String idx = EgovStringUtil.isNullToString(params.get("idx"));
		String bbsIdx = EgovStringUtil.isNullToString(params.get("bbsIdx"));
		String notice = EgovStringUtil.isNullToString(params.get("notice"));
		String zemosIdx = EgovStringUtil.isNullToString(params.get("zemosIdx"));

		params.put("notice", notice);
		model.put("notice", notice);

		if (!EgovStringUtil.isEmpty(idx)) {
			model.put("gubun", "update");
		} else {

			// 신규생성
			params.put("regYmd", EgovDateUtil.toString(new Date(), "yyyyMMdd", null));
			params.put("regHms", EgovDateUtil.toString(new Date(), "HHmmss", null));
			params.put("useYn", "N");
			params.put("deleteYn", "Y");
			params.put("regIdx", loginVO.getIdx());
			params.put("editIdx", loginVO.getIdx());

			idx = "" + z3NewsService.newscttInsert(params);
			params.put("idx", idx);
			model.put("gubun", "insert");
		}

		// 조회
		List<Map<String, Object>> resultList = z3NewsService.newscttSelect(params);

		// 리턴
		if (!EgovStringUtil.isEmptyMapList(resultList)) {
			bbsIdx = EgovStringUtil.isNullToString(resultList.get(0).get("BBS_IDX"));
			model.put("resultMap", resultList.get(0));
		}

		// 게시판 권한 정보 조회
		Map<String, Object> bbsAuthParams = new HashMap<String, Object>();
		bbsAuthParams.put("loginVO", loginVO);
		bbsAuthParams.put("bbsIdx", bbsIdx);
		bbsAuthParams.put("notice", notice);

		if (!EgovStringUtil.isEmpty(zemosIdx)) {

			ZemosVO zemosVO = (ZemosVO) request.getSession().getAttribute("zemosVO");
			List<Map<String, Object>> zemosHistory = zemosVO.getZemosHistory();
			if (!EgovStringUtil.isEmptyMapList(zemosHistory)) {

				for (Map<String, Object> zemosHistoryMap : zemosHistory) {

					int zemosHisIdx = (Integer) zemosHistoryMap.get("ZEMOS_IDX");
					if (zemosIdx.equals(String.valueOf(zemosHisIdx))) {

						int regHisIdx = (Integer) zemosHistoryMap.get("REG_IDX");
						bbsAuthParams.put("regHisIdx", regHisIdx);
						break;
					}
				}
			}
		}

		List<Map<String, Object>> bbsAuthSelect = z3NewsService.newsAuthList(bbsAuthParams);
		if (!EgovStringUtil.isEmptyMapList(bbsAuthSelect)) {
			model.put("bbsAuthMap", bbsAuthSelect.get(0));
		}

		String parentIdxStr = null;
		Map<String, Object> bbsParams = new HashMap<String, Object>();
		bbsParams.put("idx", idx);
		List<Map<String, Object>> bbscttSelect = z3NewsService.newscttSelect(bbsParams);
		if (!EgovStringUtil.isEmptyMapList(bbscttSelect)) {

			Map<String, Object> selectMap = bbscttSelect.get(0);
			String parentIdx = EgovStringUtil.isNullToString(selectMap.get("PARENT_IDX"));
			if (!EgovStringUtil.isEmpty(parentIdx)) {

				boolean isNum = EgovNumberUtil.getNumberValidCheck(parentIdx);
				if (isNum) {
					parentIdxStr = parentIdx;
				}
			}
		}

		// 태그 목록
		Map<String, Object> bbscttTagParams = new HashMap<String, Object>();
		bbscttTagParams.put("bbscttIdx", idx);
		if (!EgovStringUtil.isEmpty(parentIdxStr)) {
			bbscttTagParams.put("bbscttIdx", parentIdxStr.trim());
		}

		List<Map<String, Object>> bbscttTagList = z3NewsService.newscttTagList(bbscttTagParams);
		model.put("bbscttTagList", bbscttTagList);

		// 첨부파일
		params.put(ZemosCommonConstant.fileOwner.toString(), FILE_OWNER_PREFIX + idx.trim());
		if (!EgovStringUtil.isEmpty(parentIdxStr)) {
			params.put(ZemosCommonConstant.fileOwner.toString(), FILE_OWNER_PREFIX + parentIdxStr.trim());
		}

		params.put(ZemosCommonConstant.fileSe.toString(), ZemosCommonConstant.file.toString());
		List<Map<String, Object>> fileList = fileService.selectFileList(params);
		for (Map<String, Object> map : fileList) {
			map.put("IDX_VAL", AES128Util.encrypt(EgovStringUtil.isNullToString(map.get("IDX"))));
		}

		if (!EgovStringUtil.isEmpty(parentIdxStr)) {
			model.put("parentIdx", parentIdxStr.trim());
		}

		model.put("fileList", fileList);
		model.put("FILE_OWNER_PREFIX", FILE_OWNER_PREFIX);

		// 제모스 오너 권한인 경우 - 임원(zemosOwnerYn)
		if ("Y".equals(loginVO.getZemosOwnerYn())) {

			// 조회
			Map<String, Object> ownerParams = new HashMap<String, Object>();
			ownerParams.put(ZemosCommonConstant.ownerSe.toString(), "SABUN");
			ownerParams.put(ZemosCommonConstant.ownerKey.toString(), EgovStringUtil.isNullToString(loginVO.getSabun()));
			String adminAuthStr = adminOwnerService.getAdminAuthStr(ownerParams);
			if (!EgovStringUtil.isEmpty(adminAuthStr)) {
				model.put(ZemosCommonConstant.adminAuth.toString(), adminAuthStr);
			}
		}

		return rtnurl;
	}

	// 전문직 공지사항 게시글 저장
	@RequestMapping("/zemos3/family/news/newscttSaveSave.do")
	public ModelAndView bbscttSaveSave(@RequestParam Map<String, Object> params) throws Exception {

		ModelAndView modelAndView = new ModelAndView("jsonView");
		// 결과
		int result = 0;
		String resultMsg = "";

		// 로그인 정보
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 파라미터
		String idx = EgovStringUtil.isNullToString(params.get("idx"));
		String gubun = EgovStringUtil.isNullToString(params.get("gubun"));

		String insertIdxList = EgovStringUtil.isNullToString(params.get(ZemosCommonConstant.insertIdxList.toString()));
		String[] insertIdxArr = null;
		if (!EgovStringUtil.isEmpty(insertIdxList)) {

			insertIdxList = insertIdxList.replace("[", ZemosCommonConstant.blankString.toString());
			insertIdxList = insertIdxList.replace("]", ZemosCommonConstant.blankString.toString());
			insertIdxArr = insertIdxList.split(",");
		}

		// 게시글 저장
		params.put("editIdx", loginVO.getIdx());
		params.put("regIdx", loginVO.getIdx());

		if (insertIdxArr != null && insertIdxArr.length > 0) {

			String displayIdx = insertIdxArr[insertIdxArr.length - 1];
			for (String str : insertIdxArr) {

				params.put("idx", str);
				params.put("parentIdx", displayIdx);
				result = z3NewsService.newscttUpdate(params);
			}
		} else {

			params.put("updateYn", "Y");
			result = z3NewsService.newscttUpdate(params);
		}

		String parentIdxStr = null;
		Map<String, Object> bbsParams = new HashMap<String, Object>();
		bbsParams.put("idx", idx);
		List<Map<String, Object>> bbscttSelect = z3NewsService.newscttSelect(bbsParams);
		if (!EgovStringUtil.isEmptyMapList(bbscttSelect)) {

			Map<String, Object> selectMap = bbscttSelect.get(0);
			String parentIdx = EgovStringUtil.isNullToString(selectMap.get("PARENT_IDX"));
			if (!EgovStringUtil.isEmpty(parentIdx)) {

				boolean isNum = EgovNumberUtil.getNumberValidCheck(parentIdx);
				if (isNum) {
					parentIdxStr = parentIdx;
				}
			}
		}

		// 태그 저장
		if ("insert".equals(gubun) || "update".equals(gubun)) {

			if (insertIdxArr != null && insertIdxArr.length > 0) {

				String displayIdx = insertIdxArr[insertIdxArr.length - 1];
				params.put("bbscttIdx", displayIdx);

				z3NewsService.newscttTagDeleteAll(params);

			} else {

				params.put("bbscttIdx", idx);
				z3NewsService.newscttTagDeleteAll(params);
			}

			int bbscttTagListLength = EgovStringUtil.zeroConvert(params.get("bbscttTagListLength"));
			for (int i = 0; i < bbscttTagListLength; i++) {

				Map<String, Object> temp = new HashMap<String, Object>();
				String tagNm = EgovStringUtil.isNullToString(params.get("bbscttTagList[" + i + "][tagNm]"));
				if (!EgovStringUtil.isEmpty(tagNm)) {

					if (insertIdxArr != null && insertIdxArr.length > 0) {

						String displayIdx = insertIdxArr[insertIdxArr.length - 1];
						temp.put("bbscttIdx", displayIdx);
						temp.put("tagNm", tagNm);
						temp.put("useYn", "Y");
						temp.put("regIdx", loginVO.getIdx());

						z3NewsService.newscttTagInsert(temp);

					} else {

						temp.put("bbscttIdx", idx);
						if (!EgovStringUtil.isEmpty(parentIdxStr)) {
							temp.put("bbscttIdx", parentIdxStr.trim());
						}

						temp.put("tagNm", tagNm);
						temp.put("useYn", "Y");
						temp.put("regIdx", loginVO.getIdx());

						z3NewsService.newscttTagInsert(temp);
					}
				}
			}
		}

		// 첨부파일 저장
		if ("insert".equals(gubun) || "update".equals(gubun)) {

			if (insertIdxArr != null && insertIdxArr.length > 0) {

				String displayIdx = insertIdxArr[insertIdxArr.length - 1];
				// 일괄삭제
				params.put(ZemosCommonConstant.fileOwner.toString(), FILE_OWNER_PREFIX + displayIdx.trim());

				fileService.deleteFileAll(params);

			} else {

				// 일괄삭제
				params.put(ZemosCommonConstant.fileOwner.toString(), FILE_OWNER_PREFIX + idx.trim());
				fileService.deleteFileAll(params);

				if (!EgovStringUtil.isEmpty(parentIdxStr)) {
					params.put(ZemosCommonConstant.fileOwner.toString(), FILE_OWNER_PREFIX + parentIdxStr.trim());
				}

				fileService.deleteFileAll(params);
			}

			// 첨부파일
			int fileListLength = EgovStringUtil.zeroConvert(params.get("fileListLength"));
			for (int i = 0; i < fileListLength; i++) {

				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("regIdx", loginVO.getIdx());
				temp.put("editIdx", loginVO.getIdx());
				temp.put("idx", EgovStringUtil.isNullToString(params.get("fileList[" + i + "][idx]")));

				fileService.saveFileTemp3(temp);
			}

			// 이미지
			params.put(ZemosCommonConstant.fileSe.toString(), ZemosCommonConstant.img.toString());
			params.put("deleteYn", "ALL");
			String vContents = EgovStringUtil.isNullToString(params.get("bbscttContents"));
			List<Map<String, Object>> fileList = fileService.selectFileList(params);
			for (Map<String, Object> map : fileList) {

				String vIdxVal = AES128Util.encrypt(EgovStringUtil.isNullToString(map.get("IDX")));
				if (!EgovStringUtil.isEmpty(vIdxVal) && vContents.indexOf(vIdxVal) >= 0) {

					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("regIdx", loginVO.getIdx());
					temp.put("editIdx", loginVO.getIdx());
					temp.put("idx", EgovStringUtil.isNullToString(map.get("IDX")));

					fileService.saveFileTemp3(temp);
				}
			}
		}

		// 새소식 && 푸쉬알림 처리 (비동기)
		if ("insert".equals(gubun) || "update".equals(gubun)) {

			if (insertIdxArr != null && insertIdxArr.length > 0) {

				for (String str : insertIdxArr) {

					Map<String, Object> bbscttNtcnPushParams = new HashMap<String, Object>();
					bbscttNtcnPushParams.put("bbscttIdx", str);
					bbscttNtcnPushParams.put("userIdx", loginVO.getIdx());
					bbscttNtcnPushParams.put("userName", loginVO.getName());
					bbscttNtcnPushParams.put("gubun", gubun);

					z3NewsService.newscttNtcnPush(bbscttNtcnPushParams);
				}
			} else {

				Map<String, Object> bbscttNtcnPushParams = new HashMap<String, Object>();
				bbscttNtcnPushParams.put("bbscttIdx", idx);
				bbscttNtcnPushParams.put("userIdx", loginVO.getIdx());
				bbscttNtcnPushParams.put("userName", loginVO.getName());
				bbscttNtcnPushParams.put("gubun", gubun);

				z3NewsService.newscttNtcnPush(bbscttNtcnPushParams);
			}
		}

		// 리턴
		modelAndView.addObject("result", result);
		modelAndView.addObject("resultMsg", resultMsg);

		return modelAndView;
	}

}