package br.com.ufs.ds3.dao;

import br.com.ufs.ds3.db.DB;
import br.com.ufs.ds3.entity.Event;

public class EventDao extends Dao<Event> {
	public EventDao() {
	}
	
	public EventDao(DB db) {
		super(db);
	}
}
