package egovframework.zemos3.family.news.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.ZenielMAbstractDAO;

/**
 * DAO 클래스
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
@Repository("Z3NewsDAO")
public class Z3NewsDAO extends ZenielMAbstractDAO {
	
	/**
     * 전문직 공지사항 권한 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newsAuthList", params);
	}
	
	/**
     * 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsAuthSelect(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newsAuthSelect", params);
	} 
	
	/**
     * 전문직 공지사항 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsSelect(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newsSelect", params);
	}
	
	/**
     * 전문직 공지사항 조회 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsSelectInsert(Map<String, Object> params) throws Exception {
		return insert("zenielm.zemos3.family.news.newsSelectInsert", params);
	}
	
	/**
     * 전문직 공지사항 최근 게시물 카운트
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newsRecentCnt(Map<String, Object> params) throws Exception {
		return selectOne("zenielm.zemos3.family.news.newsRecentCnt", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 목록 카운트
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttListCnt(Map<String, Object> params) throws Exception {
		return selectOne("zenielm.zemos3.family.news.newscttListCnt", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newscttList", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttSelect(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newscttSelect", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttInsert(Map<String, Object> params) throws Exception {
		return insert("zenielm.zemos3.family.news.newscttInsert", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 수정
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttUpdate(Map<String, Object> params) throws Exception {
		return update("zenielm.zemos3.family.news.newscttUpdate", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 조회 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttReadInsert(Map<String, Object> params) throws Exception {
		return insert("zenielm.zemos3.family.news.newscttReadInsert", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 조회 목록 
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttReadList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newscttReadList", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 댓글 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttAnswerList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newscttAnswerList", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 댓글 리스트 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newsEventAnswerList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newsEventAnswerList", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 태그 목록 조회
	 * @param Map<String, Object> params
	 * @return List<Map<String, Object>>
	 * @exception Exception
	 */
	public List<Map<String, Object>> newscttTagList(Map<String, Object> params) throws Exception {
		return selectList("zenielm.zemos3.family.news.newscttTagList", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 태그 수정
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagDeleteAll(Map<String, Object> params) throws Exception {
		return delete("zenielm.zemos3.family.news.newscttTagDeleteAll", params);
	}
	
	/**
     * 전문직 공지사항 컨텐츠 태그 추가
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttTagInsert(Map<String, Object> params) throws Exception {
		return insert("zenielm.zemos3.family.news.newscttTagInsert", params);
	}
	
	/**
     * 전문직 공지사항 최대키값 조회
	 * @param Map<String, Object> params
	 * @return int
	 * @exception Exception
	 */
	public int newscttMaxIdx(Map<String, Object> params) throws Exception {
		return selectOne("zenielm.zemos3.family.news.newscttMaxIdx", params);
	}
}