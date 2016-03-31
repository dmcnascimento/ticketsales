package br.com.ufs.ds3.dao;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Chair;

public class ChairDao extends Dao<Chair> {
	public ChairDao() {
	}
	
	public ChairDao(DB db) {
		super(db);
	}
}
