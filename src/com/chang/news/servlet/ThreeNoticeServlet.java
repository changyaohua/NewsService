package com.chang.news.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SecondNoticeServlet
 */
@WebServlet("/ThreeNoticeServlet")
public class ThreeNoticeServlet extends HongTaiNoticeServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void initSQLTableName() {
    	// TODO Auto-generated method stub
    	sqlTableName = "hongtai_school_notice";
    }
}
