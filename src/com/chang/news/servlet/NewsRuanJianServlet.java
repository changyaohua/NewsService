package com.chang.news.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.chang.news.bean.NoticeBean;
import com.chang.news.biz.NoticeBiz;
import com.chang.news.biz.NoticeBizImpl;
import com.chang.news.dao.NoticeDaoImpl;

/**
 * Servlet implementation class HongTaiNoticeServlet
 */
@WebServlet("/NewsRuanJianServlet")
public class NewsRuanJianServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected String sqlTableName;
	protected String urlPath;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void initSQLTableName(){
		sqlTableName = "hongtai_news_ruanjian";
		urlPath = "http://ss.nuc.edu.cn/newDispatch.php";
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initSQLTableName();
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		if(pageNo == -1){
			request.setAttribute("sqlTableName", sqlTableName);
			request.setAttribute("urlPath", urlPath);
			request.getRequestDispatcher("./UpdateFetchNoticeServlet").forward(request, response);
		} else {
			NoticeBiz noticeBiz = new NoticeBizImpl();
			int allNoticeRow = noticeBiz.fetchNoticeRows(sqlTableName);
			int maxPage = allNoticeRow % NoticeDaoImpl.ROWS_PRE_PAGE == 0 ? allNoticeRow / NoticeDaoImpl.ROWS_PRE_PAGE : (allNoticeRow / NoticeDaoImpl.ROWS_PRE_PAGE + 1);
			if (pageNo <= maxPage) {
				List<NoticeBean> noticeBeanList = noticeBiz.fetchNoticeByPageNO(pageNo,sqlTableName);
				String jsonArray = JSON.toJSONString(noticeBeanList);
				response.getOutputStream().write(jsonArray.getBytes("UTF-8"));  
		        response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
