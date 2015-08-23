package com.chang.news.biz;

import java.util.List;

import com.chang.news.bean.NoticeBean;


/**
 * 用于对用户信息进行操作的业务层，主要用于调用其实现类的对应的方法
 * 
 * @since 2015年8月20号
 * @author 常耀华
 * @version v1.0
 *
 */
public interface NoticeBiz {

	public boolean insertNewsData(List<NoticeBean> noticeList);

	public List<NoticeBean> fetchAllNotice();
	
}
