package com.myphoto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myphoto.dao.CommentDao;
import com.myphoto.dao.CommentVODao;
import com.myphoto.dao.ContentLikeLogDao;
import com.myphoto.dao.UserDao;
import com.myphoto.entity.Comment;
import com.myphoto.entity.CommentVO;
import com.myphoto.entity.ContentLikeLog;
import com.myphoto.entity.base.PageObject;
import com.myphoto.service.CommentService;

@Service
@Transactional
@SuppressWarnings("all")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ContentLikeLogDao contentLikeLogDao;
	@Autowired
	private CommentVODao commentVODao;

	@Override
	public void saveOrUpdateComment(Comment t) {
		commentDao.saveOrUpdate(t);
	}

	@Override
	public Comment getCommentById(Integer id) {
		return commentDao.findFirstByHql(
				"from Comment where id=?",id);
	}

	@Override
	public ContentLikeLog getContentLikeLogByUserAndPro(Integer beLikeId,
			String userId) {
		ContentLikeLog contentLikeLog = contentLikeLogDao.findFirstByHql(
				"from ContentLikeLog where rootId=? and liker=?",beLikeId,userId);
		return contentLikeLog;
	}

	@Override
	public void saveOrUpdateContentLikeLog(ContentLikeLog tem) {
		contentLikeLogDao.saveOrUpdate(tem);
	}

	@Override
	public PageObject getCommentVO(PageObject pageObject, Integer rootId,
			String userId) {
		StringBuffer hql = new StringBuffer("from CommentVO where 1=1 and productId="+rootId);
		pageObject=commentVODao.findForPageByHql(pageObject, hql.toString());
		List<CommentVO> list = pageObject.getData();
		List<CommentVO> dateCount = commentVODao.findByHql(hql.toString());
		List l = new ArrayList();
		if(list.size()>0){
			for(int j=0;j<list.size();j++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", list.get(j).getId());
				map.put("productId", list.get(j).getProductId());
				map.put("parentReplyId", list.get(j).getParentReplyId());
				map.put("parentReplyConetent", list.get(j).getParentReplyConetent());
				map.put("parentRelyer", list.get(j).getParentRelyer());
				map.put("content", list.get(j).getContent());
				map.put("replyer", list.get(j).getReplyer());
				map.put("commentTime", list.get(j).getCommentTime());
				map.put("replyerHeadImage", list.get(j).getReplyerHeadImage());
				map.put("replyerNickName", list.get(j).getReplyerNickName());
				map.put("parentRelyerHeadImage", list.get(j).getParentRelyerHeadImage());
				map.put("parentRelyerNickName", list.get(j).getParentRelyerNickName());
				map.put("likeNum", list.get(j).getLikeNum());
				ContentLikeLog cll = contentLikeLogDao.findFirstByHql(
						"from ContentLikeLog where rootId=? and liker=?",list.get(j).getId(),userId);
				if(cll!=null&&cll.getStatus()==1){
					map.put("likeState", 1);
				}else if(cll!=null&&cll.getStatus()==0){
					map.put("likeState",0);
				}
				l.add(map);	
			}
		}
		pageObject.setData(l);
		pageObject.setDataCount(dateCount.size());
		return pageObject;
	}
	
}
