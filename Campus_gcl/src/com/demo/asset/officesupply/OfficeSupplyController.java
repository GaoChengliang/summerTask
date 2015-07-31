package com.demo.asset.officesupply;

import java.util.List;

import com.demo.asset.supplier.Supplier;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class OfficeSupplyController extends Controller{
	
	/*查看*/
	public void index() {
		Page<OfficeSupply> page  = OfficeSupply.dao.paginate(getParaToInt(0, 1), 8, "select ost.*, st.NAME SNAME", 
				"from office_supply_table ost inner join supplier_table st on ost.SUPPLIER_ID = st.ID order by ost.IN_TIME asc");
		setAttr("officeSupplyPage", page);
		render("office_supply_root.html");
	}
	
	
	public void add() {
		List<Supplier> suppliers = Supplier.dao.find("select * from supplier_table order by ID asc");
		setAttr("suppliersList", suppliers);
		setAttr("lastPage", getParaToInt(0));
		render("add.html");
	}
	
	public void save() {
		getModel(OfficeSupply.class).set("REMAIN_NUMBER", getPara("officeSupply.NUMBER")).save();
		int page = getParaToInt();
		if(page <= 0){
			page = 1;
		}
		redirect("/asset/officeSupply/" + page);
	}
	
	public void edit() {
		List<Supplier> suppliers = Supplier.dao.find("select * from supplier_table order by ID asc");
		setAttr("suppliersList", suppliers);
		setAttr("currentPage",getParaToInt(1));
		setAttr("officeSupply", OfficeSupply.dao.findById(getParaToInt(0)));
	}
	
	public void update() {
		getModel(OfficeSupply.class).update();
		redirect("/asset/officeSupply/" + getParaToInt());
	}
	
	public void delete() {
		OfficeSupply.dao.deleteById(getParaToInt());
		redirect("/asset/officeSupply");
	}
	
//----------------------------------------------------------------------------------------
	
	
/*出库记录*/	
	public void outRecord(){
		Page<OfficeSupplyOut> page  = OfficeSupplyOut.dao.paginate(getParaToInt(0, 1), 8, "select *", 
				"from office_supply_out_table order by OUT_TIME asc");
		setAttr("outRecordPage", page);
		render("out_record.html");
	}
	
	public void addOut() {
		setAttr("lastPage", getParaToInt(0));
		render("add_out.html");
	}
	
	
	public void saveOut() {
		List<OfficeSupply> supply = OfficeSupply.dao.find("select * from office_supply_table where OSID='"+ getPara("officeSupplyOut.OSID") +"'" );
		if(supply.size() == 0){
			renderText("当前资产不存在，请返回检查并重新输入！");
			return;
		}
		OfficeSupply os = supply.get(0);
		if(os.getInt("REMAIN_NUMBER") < getParaToInt("officeSupplyOut.NUMBER")){
			renderText("出库数量超过当前库存，请返回检查并重新输入！");
			return;			
		}
		os.set("REMAIN_NUMBER", os.getInt("REMAIN_NUMBER") - getParaToInt("officeSupplyOut.NUMBER")).update();
		getModel(OfficeSupplyOut.class).save();
		int page = getParaToInt();
		if(page <= 0){
			page = 1;
		}
		redirect("/asset/officeSupply/outRecord/" + page);
	}

//--------------------------------------------------------------------------------------
	
/*申请*/	
	
	
	public void applyAdd(){
		setAttr("officeApply", OfficeSupply.dao.findById(getParaToInt(0)));
		render("add_apply.html");
	}
	
	public void saveApply() {
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("APPLY_STATUS","SAVED");
		osa.set("USER_ACCOUNT", getCurrentUser()).save();
		
		redirect("/asset/officeSupply/applyRecord");
	}
	
	public void subApply() {
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("APPLY_STATUS","SUBMMITED");
		osa.set("USER_ACCOUNT", getCurrentUser());
		osa.set("REVIEW_STATUS", "PENDING").save();
		redirect("/asset/officeSupply/applyRecord");
	}
	
	public void saveEditApply() {
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("APPLY_STATUS","SAVED");
		osa.set("USER_ACCOUNT", getCurrentUser()).update();
		
		redirect("/asset/officeSupply/applyRecord");
	}
	
	public void subEditApply() {
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("APPLY_STATUS","SUBMMITED");
		osa.set("USER_ACCOUNT", getCurrentUser());
		osa.set("REVIEW_STATUS", "PENDING").update();
		redirect("/asset/officeSupply/applyRecord");
	}
	
	public void applyEdit(){
		setAttr("officeSupplyApply", OfficeSupplyApply.dao.findById(getParaToInt(0)));
		render("edit_apply.html");
	}
	
	public void disapply(){
		OfficeSupplyApply.dao.findById(getParaToInt(0)).set("APPLY_STATUS", "CANCELED").update();
		redirect("/asset/officeSupply/applyRecord");
	}
	
	public void applyRecord(){
		
		Page<OfficeSupplyApply> page  = OfficeSupplyApply.dao.paginate(getParaToInt(0, 1), 8, "select *", 
				"from office_supply_apply_table where USER_ACCOUNT='"+ getCurrentUser() +"' order by ID desc");
		setAttr("applyRecordPage", page);
		render("apply_record.html");
	}
	

	
	
//---------------------------------------------------------------------------------------	
	
	public String getCurrentUser(){
		String userAccount = "test";
		return userAccount;
	}
	
//-------------------------------------------------------------------------------------------

	/*审核申请*/
	public void applyExamine(){
		Page<OfficeSupplyApply> page  = OfficeSupplyApply.dao.paginate(getParaToInt(0, 1), 8, "select *", 
				"from office_supply_apply_table where (APPLY_STATUS = 'SUBMMITED' or APPLY_STATUS = 'CANCELED') and REVIEW_STATUS = 'PENDING' order by ID asc");
		setAttr("applyRecordPage", page);
		render("apply_examine.html");
	}
	
	public void editExamine() {
		setAttr("officeApply", OfficeSupplyApply.dao.findById(getParaToInt(0)));
		setAttr("currentPage",getParaToInt(1));
		render("edit_examine.html");
	}
	
	public void agree() {
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("REVIEW_STATUS", "PASSED").update();
		redirect("/asset/officeSupply/applyExamine/" + getParaToInt());
	}

	public void reject(){
		OfficeSupplyApply osa = getModel(OfficeSupplyApply.class);
		osa.set("REVIEW_STATUS", "REJECTED").update();
		redirect("/asset/officeSupply/applyExamine/" + getParaToInt());
	}
}


