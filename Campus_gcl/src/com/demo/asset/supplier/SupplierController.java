package com.demo.asset.supplier;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class SupplierController extends Controller{
	public void index() {
		Page<Supplier> page  = Supplier.dao.paginate(getParaToInt(0, 1), 8, "select *", "from supplier_table order by ID asc");
		setAttr("supplierPage", page);
		render("supplier.html");
	}
	
	public void add() {
		render("add.html");
	}
	
	
	public void view() {
		setAttr("supplier", Supplier.dao.findById(getParaToInt(0)));
		render("view.html");
	}
	
	@Before(SupplierValidator.class)
	public void save() {
		getModel(Supplier.class).save();
		redirect("/supplier");
	}
	
	public void edit() {
		setAttr("supplier", Supplier.dao.findById(getParaToInt(0)));
		render("edit.html");
	}
	
	@Before(SupplierValidator.class)
	public void update() {
		getModel(Supplier.class).update();
		redirect("/supplier");
	}
	
	public void delete() {
		Supplier.dao.deleteById(getParaToInt());
		redirect("/supplier");
	}
}
