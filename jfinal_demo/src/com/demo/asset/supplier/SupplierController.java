package com.demo.asset.supplier;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class SupplierController extends Controller{
	public void index() {
		setAttr("supplierPage", Supplier.me.paginate(getParaToInt(0, 1), 8));
		render("supplier.html");
	}
	
	public void add() {
		render("add.html");
	}
	
	@Before(SupplierValidator.class)
	public void save() {
		getModel(Supplier.class).save();
		redirect("/asset/supplier");
	}
	
	public void edit() {
		setAttr("supplier", Supplier.me.findById(getParaToInt()));
	}
	
	@Before(SupplierValidator.class)
	public void update() {
		getModel(Supplier.class).update();
		redirect("/asset/supplier");
	}
	
	public void delete() {
		Supplier.me.deleteById(getParaToInt());
		redirect("/asset/supplier");
	}
}
