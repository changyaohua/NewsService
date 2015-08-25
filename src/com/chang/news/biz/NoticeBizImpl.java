package com.chang.news.biz;

import java.util.List;

import com.chang.news.bean.NoticeBean;
import com.chang.news.dao.NoticeDao;
import com.chang.news.dao.NoticeDaoImpl;

public class NoticeBizImpl implements NoticeBiz{

	NoticeDao noticeDao = new NoticeDaoImpl();

	@Override
	public boolean insertNewsData(List<NoticeBean> noticeList,String sqlTableName) {
		boolean isSuccess = false;
		try {
			isSuccess = noticeDao.insertNewsData(noticeList,sqlTableName);
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

	@Override
	public List<NoticeBean> fetchNoticeByPageNO(int pageNo,String sqlTableName) {
		List<NoticeBean> noticeBeanList = null;
		try {
			noticeBeanList = noticeDao.fetchNoticeByPageNO(pageNo,sqlTableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeBeanList;
	}

	@Override
	public int fetchNoticeRows(String sqlTableName) {
		int count = 0;
		try {
			count  = noticeDao.fetchNoticeRows(sqlTableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public NoticeBean fetchFirstNotice() {
		NoticeBean noticeBean = null;
		try {
			noticeBean = noticeDao.fetchFirstNotice();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeBean;
	}
	
}
