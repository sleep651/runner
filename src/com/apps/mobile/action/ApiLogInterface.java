package com.apps.mobile.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apps.mobile.domain.UserAccount;

public interface ApiLogInterface {
	public UserAccount checkTicket(String ticket);
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
