package egovframework.zemos3.family.news.service;

import java.util.List;
import java.util.Map;

/**
 * 인터페이스 클래스
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
public interface Z3NewsService {
	
	/**
     * 전문직 공지사항 권한 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthList(Map<String, Object> params) throws Exception;
	
	/**
     * 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthSelect(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsSelect(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 조회 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsSelectInsert(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 최근 게시물 카운트
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsRecentCnt(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 목록 카운트
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttListCnt(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttList(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttSelect(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttInsert(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 수정
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttUpdate(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 조회 목록
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttReadList(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 컨텐츠 조회 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttReadInsert(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 댓글 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttAnswerList(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 댓글 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsEventAnswerList(Map<String, Object> params) throws Exception;
	
	
	/**
     * 전문직 공지사항 태그 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttTagList(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 태그 수정
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagDeleteAll(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 태그 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagInsert(Map<String, Object> params) throws Exception;
	
	/**
     * 전문직 공지사항 푸쉬 알람 처리
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public void newscttNtcnPush(Map<String, Object> params) throws Exception;
	
}
