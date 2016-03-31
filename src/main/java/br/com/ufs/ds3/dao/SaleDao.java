package br.com.ufs.ds3.dao;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Sale;

public class SaleDao extends Dao<Sale> {
	public SaleDao() {
	}
	
	public SaleDao(DB db) {
		super(db);
	}
}
