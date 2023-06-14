package egovframework.zemos3.family.news.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.ZemosCommonConstant;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.push.service.PushService;
import egovframework.com.user.service.UserService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.zemos.zemos.service.ZemosService;
import egovframework.zemos3.family.news.service.Z3NewsService;

/**
 * 서비스 클래스
 * @author 박수호
 * @since 2022.04.29
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *     수정일            작업자                         수정내용
 *  ----------    -------    --------------------------
 *  2022.04.29     박수호          전문직 공지사항 게시판 최초 생성 
 *  </pre>
 */
@Service("z3NewsService")
public class Z3NewsServiceImpl extends EgovAbstractServiceImpl implements Z3NewsService {
	
	@Resource(name = "userService")
	protected UserService userService;
	
	@Resource(name = "zemosService")
	protected ZemosService zemosService;
	
    @Resource(name="z3NewsZenielMDAO")
    private Z3NewsZenielMDAO z3NewsZenielMDAO;
    
	@Resource(name = "pushService")
	protected PushService pushService;
	
	/**
	 * 전문직 공지사항 권한 목록 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthList(Map<String, Object> params) throws Exception {

		// 로그인 정보
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 로그인 기본정보 추가
		params.put("loginVO", loginVO);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		/** 제모스 목록 **/
		List<Map<String, Object>> zemosList = new ArrayList<Map<String, Object>>();

		String managerZemos = EgovStringUtil.isNullToString(params.get("managerZemos"));
		// 관리자 권한으로 로그인 후 현장제모스의 자유게시판을 선택했을 경우
		if (!EgovStringUtil.isEmpty(managerZemos) && managerZemos.equals(loginVO.getSabun())) {

			// 제모스 이력 정보
			String regHisIdx = EgovStringUtil.isNullToString(params.get("regHisIdx"));
			if (!EgovStringUtil.isEmpty(regHisIdx)) {
				// 삭제된 제모스 이력 정보도 가져와야 하므로 삭제 체크를 하지 않도록 셋팅함
				params.put("deleteNoCheck", true);
			}
			
			String zemosIdx = EgovStringUtil.isNullToString(params.get("zemosIdx"));
			params.put("myZemosIdx", zemosIdx);
			// #{myZemosAuth} IN ('11000','12000','21000'))
			params.put("myZemosAuth", ZemosCommonConstant.zemosWorkerCode.toString());
		} else {

			// 오너 권한 - 임원(zemosOwnerYn)
			if ("Y".equals(loginVO.getZemosOwnerYn())) {
				
				// 제모스 이력 정보
				String regHisIdx = EgovStringUtil.isNullToString(params.get("regHisIdx"));
				if (!EgovStringUtil.isEmpty(regHisIdx)) {
					// 삭제된 제모스 이력 정보도 가져와야 하므로 삭제 체크를 하지 않도록 셋팅함
					params.put("deleteNoCheck", true);
				}
				
				// 전체 게시판 조회
				params.put("zemosOwnerYn", loginVO.getZemosOwnerYn());
			// 관리자 권한 - 잡컨설턴트(zemosMngrYn), 파트장(zemosMainMngrYn), 대행 잡컨설턴트(21000)
			} else if ("Y".equals(loginVO.getZemosMngrYn())
					|| "Y".equals(loginVO.getZemosMainMngrYn())
					|| ZemosCommonConstant.zemosJobMngCode.toString().equals(loginVO.getZemosAuth())) {
				
				// 제모스 현장 리스트
				params.put("selectText", 	"");
				params.put("wrkplcNm", 		"");
				
				// 제모스 이력 정보
				String regHisIdx = EgovStringUtil.isNullToString(params.get("regHisIdx"));
				if (!EgovStringUtil.isEmpty(regHisIdx)) {
					
					// 삭제된 제모스 이력 정보도 가져와야 하므로 삭제 체크를 하지 않도록 셋팅함
					params.put("deleteNoCheck", true);
					zemosList = zemosService.zemosMyList(loginVO, params);
					
				} else {
					
					params.put("userId", 		loginVO.getUserId());
					params.put("sabun", 		loginVO.getSabun());
					params.put("buseo", 		loginVO.getBuseo());
					
					zemosList = zemosService.zemosMyList(loginVO, params);
				}
				
			// 관리부서 권한 - 본사직원(zemosMngDeptYn)
			} else if ("Y".equals(loginVO.getZemosMngDeptYn())) {
				zemosList = zemosService.zemosMngDeptList(params);
			}

			// 오너 권한 - 임원(zemosOwnerYn)
			if ("Y".equals(loginVO.getZemosOwnerYn())) {
				
			} else {
				
				// 내가 소속된 제모스 추가 - 제모스사용자(zemosUserYn)
				if ("Y".equals(loginVO.getZemosUserYn())) {
					params.put("myZemosIdx",  loginVO.getZemosIdx());
					params.put("myZemosAuth", loginVO.getZemosAuth());
				}
			}
		}

		params.put("zemosList", zemosList);
		resultList = z3NewsZenielMDAO.newsAuthList(params);

		for (Map<String, Object> map : resultList) {

			String bbsSe 	= EgovStringUtil.isNullToString(map.get("BBS_SE"));
			String bbsSeIdx = EgovStringUtil.isNullToString(map.get("BBS_SE_IDX"));
			String iconUrl 	= EgovProperties.getProperty(ZemosCommonConstant.noZemosImage.toString());
			
			if ("ZEMOS".equals(bbsSe)) {
				iconUrl = EgovProperties.getProperty(ZemosCommonConstant.zemosImageUrl.toString()) + "?idx=" + bbsSeIdx;
			}
			
			map.put("ICON_URL", iconUrl);
		}

		return resultList;
	}
	
	/**
	 * 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthSelect(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newsAuthSelect(params);
	}
	
	/**
	 * 전문직 공지사항 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsSelect(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newsSelect(params);
	}
	
	/**
     * 전문직 공지사항 조회 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsSelectInsert(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newsSelectInsert(params);
	}
	
	/**
	 * 전문직 공지사항 최근 게시물 카운트
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsRecentCnt(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newsRecentCnt(params);
	}
	
	/**
	 * 전문직 공지사항 조회
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttListCnt(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttListCnt(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 목록 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttList(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttList(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttSelect(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttSelect(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 추가
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttInsert(Map<String, Object> params) throws Exception {
		z3NewsZenielMDAO.newscttInsert(params);
		return z3NewsZenielMDAO.newscttMaxIdx(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 수정
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttUpdate(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttUpdate(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 조회 추가
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttReadInsert(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttReadInsert(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 조회 목록 
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttReadList(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttReadList(params);
	}

	/**
	 * 전문직 공지사항 컨텐츠 댓글 목록 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttAnswerList(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttAnswerList(params);
	}
	
	/**
     * 전문직 공지사항 댓글 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsEventAnswerList(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newsEventAnswerList(params);
	}
	
	/**
	 * 전문직 공지사항 컨텐츠 태그 목록 조회
	 * 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttTagList(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttTagList(params);
	}

	/**
	 * 전문직 공지사항 컨텐츠 태그 수정
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagDeleteAll(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttTagDeleteAll(params);
	}

	/**
	 * 전문직 공지사항 컨텐츠 태그 추가
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagInsert(Map<String, Object> params) throws Exception {
		return z3NewsZenielMDAO.newscttTagInsert(params);
	}
	
	/**
	 * 전문직 공지사항 푸시하기
	 * 
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public void newscttNtcnPush(Map<String, Object> params) throws Exception {

	// 제모스 관리자/사용자에게 PUSH 알림 발송
	//////////////////////////////////////// 새소식 & 푸쉬 시작

	// 파라미터
	String bbscttIdx 	= EgovStringUtil.isNullToString(params.get("bbscttIdx"));
	String userIdx 		= EgovStringUtil.isNullToString(params.get("userIdx"));

	if ("".equals(bbscttIdx) || "".equals(userIdx)) { return; }

	params.put("userIdx", userIdx);

	// 게시판 & 게시글 조회
	List<Map<String, Object>> resultList1 = pushService.sendPushBbs1(params);
	if (resultList1 == null || resultList1.isEmpty()) {	return;	}

	String gubun = EgovStringUtil.isNullToString(params.get("gubun"));

	for (Map<String, Object> resultMap1 : resultList1) {

	String bbsIdx 		= EgovStringUtil.isNullToString(resultMap1.get("BBS_IDX"));
	String bbsNm 		= EgovStringUtil.isNullToString(resultMap1.get("BBS_NM"));
	String bbsSe 		= EgovStringUtil.isNullToString(resultMap1.get("BBS_SE"));
	String zemosIdx 	= EgovStringUtil.isNullToString(resultMap1.get("ZEMOS_IDX"));
	String secretYn 	= EgovStringUtil.isNullToString(resultMap1.get("SECRET_YN"));
	String ntcnYn 		= EgovStringUtil.isNullToString(resultMap1.get("NTCN_YN"));
	String enfrcNtcnYn 	= EgovStringUtil.isNullToString(resultMap1.get("ENFRC_NTCN_YN"));
	String regName 		= EgovStringUtil.isNullToString(resultMap1.get("REG_NAME"));
				
	params.put("bbsIdx", 		bbsIdx);
	params.put("bbsNm", 		bbsNm);
	params.put("bbsSe", 		bbsSe);
	params.put("zemosIdx", 		zemosIdx);
	params.put("ntcnYn", 		ntcnYn);
	params.put("enfrcNtcnYn", 	enfrcNtcnYn);
	params.put("regName", 		regName);

	// 비밀글은 알림 푸쉬 안보냄
	// 알림여부 사용인 경우만
	if (!"Y".equals(secretYn) && "Y".equals(ntcnYn)) {

		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();
		if ("MAIN".equals(bbsSe)) {
			resultList2 = pushService.sendPushBbs2Main(params);
		} else if ("ZEMOS".equals(bbsSe)) {

			List<Map<String, Object>> tmplist1 = pushService.sendPushBbs2Zemos1(params);
			params.put("list1", tmplist1);
			List<Map<String, Object>> tmplist2 = pushService.sendPushBbs2Zemos2(params);
			params.put("list1", tmplist2);
			resultList2 = pushService.sendPushBbs2Zemos3(params);
		}
		params.put("userList", resultList2);

	// 알림
	// 알림 저장
	// 자유게시판
	String ntcnTitle 	= ZemosCommonConstant.zemosBbsTitle.toString();
	String ntcnUrl 		= "/zemos3/com/bbs/bbscttSelect.do?idx=" + bbscttIdx;
			
	String ntcnText = regName + ZemosCommonConstant.writeNewBbsMsg.toString();
					
	// 공지사항 게시판인 경우
	if (!EgovStringUtil.isEmpty(bbsNm) && bbsNm.equals(ZemosCommonConstant.zemosNoticeTitle.toString())) {
						
		// 공지사항
		ntcnTitle 	= ZemosCommonConstant.zemosNoticeTitle.toString();
		ntcnUrl += "&notice=true";
						
		ntcnText = regName + ZemosCommonConstant.writeNewNoticeMsg.toString();
		}

					
		if (!EgovStringUtil.isEmpty(gubun)) {

			if ("update".equals(gubun)) {
							
				ntcnText = regName + ZemosCommonConstant.writeChangeBbsMsg.toString();
							
				// 공지사항 게시판인 경우
				if (!EgovStringUtil.isEmpty(bbsNm) && bbsNm.equals(ZemosCommonConstant.zemosNoticeTitle.toString())) {
					ntcnText = regName + ZemosCommonConstant.writeChangeNoticeMsg.toString();
				}
			}
		}

		// 제모스 Club 알림 및 푸쉬 처리
		pushService.sendAlramPushClub(resultList2, ntcnTitle, ntcnUrl, ntcnText, ntcnYn, ZemosCommonConstant.blankString.toString(), "BBSCTT", params);
		}
				
	}
	// 담당자 정보 조회 끝
	// 제모스 관리자/사용자에게 PUSH 알림 발송 끝
	//////////////////////////////////////// 새소식 & 푸쉬 끝
	}
	
}