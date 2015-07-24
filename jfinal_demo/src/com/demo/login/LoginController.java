package com.demo.login;

import com.jfinal.core.Controller;

/**
 * IndexController
 */
public class LoginController extends Controller {
	public void index() {
		render("login.html");
	}
}