package com.chang.news.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.chang.news.bean.NoticeBean;
import com.chang.util.DBUtil;

public class NoticeDaoImpl implements NoticeDao {

	DBUtil dbUtil = new DBUtil();
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	
	public static final int ROWS_PRE_PAGE = 15;
	
	@Override
	public boolean insertNewsData(List<NoticeBean> noticeList) throws Exception {
		
		boolean result = false;
		Connection connection = dbUtil.getConnection();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SS");
		TimeZone t = format.getTimeZone();
		t.setRawOffset(0);
		format.setTimeZone(t);
		Long startTime = System.currentTimeMillis();
		connection.setAutoCommit(false);
		String sql = "insert into hongtai_important_notice (title,time,url) value (?,?,?)";
		preparedStatement = connection.prepareStatement(sql);
		for (NoticeBean noticeBean : noticeList) {
			preparedStatement.setString(1, noticeBean.getTitle());
			preparedStatement.setString(2, noticeBean.getTime());
			preparedStatement.setString(3, noticeBean.getUrl());
			preparedStatement.addBatch();
		}
		int[] count = preparedStatement.executeBatch();
		connection.commit();
		Long endTime = System.currentTimeMillis();
		System.out.println("”√ ±" + format.format(new Date(endTime - startTime)));
		if (count.length != 0) {
			result = true;
		}
		dbUtil.closeDBSourse(connection, preparedStatement, resultSet);
		return result;
	}

	@Override
	public List<NoticeBean> fetchAllNotice() throws Exception {
		List<NoticeBean> noticeBeanList = null;
		Connection connection = dbUtil.getConnection();
		String sql = "select * from hongtai_important_notice order by id desc";
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();
		noticeBeanList = new ArrayList<NoticeBean>();
		NoticeBean noticeBean = null;
		while(resultSet.next()){
			noticeBean = new NoticeBean();
			noticeBean.setTitle(resultSet.getString("title"));
			noticeBean.setTime(resultSet.getString("time"));
			noticeBean.setUrl(resultSet.getString("url"));
			noticeBeanList.add(noticeBean);
		}
		dbUtil.closeDBSourse(connection, preparedStatement, resultSet);
		return noticeBeanList;
	}

	@Override
	public List<NoticeBean> fetchNoticeByPageNO(int pageNo) throws Exception {
		List<NoticeBean> noticeBeanList = null;
		Connection connection = dbUtil.getConnection();
		String sql = "select * from hongtai_important_notice order by id desc limit ?,?";
		preparedStatement = connection.prepareStatement(sql);
		int startIndex = (pageNo - 1) * ROWS_PRE_PAGE;
		preparedStatement.setInt(1, startIndex);
		preparedStatement.setInt(2, ROWS_PRE_PAGE);
		resultSet = preparedStatement.executeQuery();
		noticeBeanList = new ArrayList<NoticeBean>();
		NoticeBean noticeBean = null;
		while(resultSet.next()){
			noticeBean = new NoticeBean();
			noticeBean.setTitle(resultSet.getString("title"));
			noticeBean.setTime(resultSet.getString("time"));
			noticeBean.setUrl(resultSet.getString("url"));
			noticeBeanList.add(noticeBean);
		}
		dbUtil.closeDBSourse(connection, preparedStatement, resultSet);
		return noticeBeanList;
	}

	@Override
	public int fetchNoticeRows() throws Exception {
		int count = 0;
		Connection connection = dbUtil.getConnection();
		String sql = "select count(*) from hongtai_important_notice";
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();
		if(resultSet.next()) {
			count =resultSet.getInt(1);
		}
		dbUtil.closeDBSourse(connection, preparedStatement, resultSet);
		return count;
	}

	@Override
	public NoticeBean fetchFirstNotice() throws Exception {
		NoticeBean noticeBean = null;
		Connection connection = dbUtil.getConnection();
		String sql = "select * from hongtai_important_notice order by id desc";
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			noticeBean = new NoticeBean();
			noticeBean.setTitle(resultSet.getString("title"));
			noticeBean.setTime(resultSet.getString("time"));
			noticeBean.setUrl(resultSet.getString("url"));
			break;
		}
		dbUtil.closeDBSourse(connection, preparedStatement, resultSet);
		return noticeBean;
	}

}
