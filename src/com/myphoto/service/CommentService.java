package com.myphoto.service;

import com.myphoto.entity.Comment;
import com.myphoto.entity.ContentLikeLog;
import com.myphoto.entity.base.PageObject;


public interface CommentService {
	public void saveOrUpdateComment(Comment t);

	public Comment getCommentById(Integer id);

	/**
	 * @author wcj
	 * 2019-3-19下午9:13:19
	 * @param beLikeId
	 * @param userId
	 * @return
	 * ContentLikeLog
	 */
	public ContentLikeLog getContentLikeLogByUserAndPro(Integer beLikeId,
			String userId);

	/**
	 * @author wcj
	 * 2019-3-19下午9:25:40
	 * @param tem
	 * void
	 */
	public void saveOrUpdateContentLikeLog(ContentLikeLog tem);

	/**
	 * @author wcj
	 * 2019-3-20下午11:34:01
	 * @param pageObject
	 * @param rootId
	 * @param userId
	 * @param rootType
	 * @return
	 * PageObject
	 */
	public PageObject getCommentVO(PageObject pageObject, Integer rootId,
			String userId);
}
