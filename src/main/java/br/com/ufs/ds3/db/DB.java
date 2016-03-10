package br.com.ufs.ds3.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.ChairCondition;
import br.com.ufs.ds3.entity.Theatre;

public final class DB {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ticketsalesPU");;
	
	static {
		EntityManager entityManager = createEntityManager();
		if (entityManager.createQuery("select 1 from Theatre").setMaxResults(1).getResultList().isEmpty()) {
			Theatre theatre = new Theatre();
			theatre.setAddress("Av. Tancredo Neves");
			theatre.setName("Tobias Barreto");
			
			entityManager.getTransaction().begin();
			entityManager.persist(theatre);
			
			String[] rows = {"A", "B", "C", "D", "E"};
			for (int i = 0; i < rows.length; i++) {
				for (int j = 0; j < 10; j++) {
					Chair chair = new Chair();
					chair.setChairCondition(ChairCondition.NORMAL);
					chair.setNumber(j);
					chair.setRow(rows[i]);
					chair.setTheatre(theatre);
					entityManager.persist(chair);
				}
			}
			
			theatre = new Theatre();
			theatre.setAddress("Av. Rio de Janeiro");
			theatre.setName("Atheneu");
			entityManager.persist(theatre);
			
			String[] rows2 = {"A", "B", "C"};
			for (int i = 0; i < rows2.length; i++) {
				for (int j = 0; j < 10; j++) {
					Chair chair = new Chair();
					chair.setChairCondition(ChairCondition.NORMAL);
					chair.setNumber(j);
					chair.setRow(rows2[i]);
					chair.setTheatre(theatre);
					entityManager.persist(chair);
				}
			}
			
			entityManager.getTransaction().commit();
		}
		entityManager.close();
	}
	
	public static EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
}
