package com.demo.asset.supplier;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;


@SuppressWarnings("serial")
public class Supplier extends Model<Supplier> {
	public static final Supplier me = new Supplier();
	
	public Page<Supplier> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from supplier_table order by ID asc");
	}
}