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

/**
 * Servlet implementation class FirstFetchNoticeServlet
 */
@WebServlet("/UpdateFetchNoticeServlet")
public class UpdateFetchNoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateFetchNoticeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		NoticeBiz noticeBiz = new NoticeBizImpl();

		String sqlTableName = (String) request.getAttribute("sqlTableName");

		// 将新更新的数据返回客户端
		List<NoticeBean> noticeBeanList = noticeBiz.fetchNoticeByPageNO(1,
				sqlTableName);
		String jsonArray = JSON.toJSONString(noticeBeanList);
		response.getOutputStream().write(jsonArray.getBytes("UTF-8"));
		response.setContentType("text/json; charset=UTF-8"); // JSON的类型为text/json

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
