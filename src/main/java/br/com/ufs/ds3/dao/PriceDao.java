package br.com.ufs.ds3.dao;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Price;

public class PriceDao extends Dao<Price> {
	public PriceDao() {
	}
	
	public PriceDao(DB db) {
		super(db);
	}
}
