package com.demo.asset.officesupply;

import java.util.List;

import com.demo.asset.supplier.Supplier;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class OfficeSupplyController extends Controller{
	public void index() {
		Page<OfficeSupply> page  = OfficeSupply.dao.paginate(getParaToInt(0, 1), 8, "select ost.*, st.NAME SNAME", 
				"from office_supply_table ost inner join supplier_table st on ost.SUPPLIER_ID = st.ID where ost.IS_OUT = 0 order by ost.ID asc");
		setAttr("officeSupplyPage", page);
		render("office_supply.html");
	}
	
	public void add() {
		List<Supplier> suppliers = Supplier.dao.find("select * from supplier_table order by ID asc");
		setAttr("suppliersList", suppliers);
		render("add.html");
	}
	
	public void save() {
		getModel(OfficeSupply.class).save();
		redirect("/asset/officeSupply");
	}
	
	public void edit() {
		List<Supplier> suppliers = Supplier.dao.find("select * from supplier_table order by ID asc");
		setAttr("suppliersList", suppliers);
		setAttr("officeSupply", OfficeSupply.dao.findById(getParaToInt()));
	}
	
	public void update() {
		getModel(OfficeSupply.class).update();
		redirect("/asset/officeSupply");
	}
	
	public void delete() {
		OfficeSupply.dao.deleteById(getParaToInt());
		redirect("/asset/officeSupply");
	}
	
	public void outRecord(){
		Page<OfficeSupply> page  = OfficeSupply.dao.paginate(getParaToInt(0, 1), 8, "select ost.*, st.NAME SNAME", 
				"from office_supply_table ost inner join supplier_table st on ost.SUPPLIER_ID = st.ID where ost.IS_OUT = 1 order by ost.ID asc");
		setAttr("outRecordPage", page);
		render("out_record.html");
	}
	
	public void apply(){
		setAttr("officeApply", OfficeSupply.dao.findById(getParaToInt()));
		render("apply.html");
	}
	
	public void disapply(){
		OfficeSupply.dao.findById(getParaToInt()).set("IS_APPLY", 0).update();
		redirect("/asset/officeSupply");
	}
	
	public void examine(){
		setAttr("officeApply", OfficeSupply.dao.findById(getParaToInt()));
		render("examine.html");
	}
	
	public void reject(){
		OfficeSupply.dao.findById(getParaToInt()).set("IS_APPLY", 0).update();
		redirect("/asset/officeSupply");
	}

}