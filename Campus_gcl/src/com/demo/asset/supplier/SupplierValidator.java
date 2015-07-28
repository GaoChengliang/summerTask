package com.demo.asset.supplier;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class SupplierValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		validateRegex("supplier.PHONE", "\\b((\\d{11})|(0(\\d{2,3})-\\d{6,9}))\\b", "phoneMsg", "电话号码格式不正确，请重新输入！");
		validateEmail("supplier.EMAIL", "emailMsg", "邮箱格式不正确，请重新输入！");
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(Supplier.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/asset/supplier/save"))
			c.render("add.html");
		else if (actionKey.equals("/asset/supplier/update"))
			c.render("edit.html");
		
	}

}
