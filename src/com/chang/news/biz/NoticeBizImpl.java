package com.chang.news.biz;

import java.util.List;

import com.chang.news.bean.NoticeBean;
import com.chang.news.dao.NoticeDao;
import com.chang.news.dao.NoticeDaoImpl;

public class NoticeBizImpl implements NoticeBiz{

	NoticeDao noticeDao = new NoticeDaoImpl();

	@Override
	public boolean insertNewsData(List<NoticeBean> noticeList) {
		boolean isSuccess = false;
		try {
			isSuccess = noticeDao.insertNewsData(noticeList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isSuccess;
	}

	@Override
	public List<NoticeBean> fetchAllNotice() {
		List<NoticeBean> noticeBeanList = null;
		try {
			noticeBeanList = noticeDao.fetchAllNotice();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeBeanList;
	}
	
}