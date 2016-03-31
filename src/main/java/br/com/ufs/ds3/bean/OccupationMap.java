package br.com.ufs.ds3.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.NumberedChairSession;

public class OccupationMap {
	private NumberedChairSession session;
	private List<ChairAvailability> chairAvailabilities = new ArrayList<>();
	private int rows;
	private int cols;

	private OccupationMap(NumberedChairSession session) {
	}
	
	public NumberedChairSession getSession() {
		return session;
	}
	
	public List<ChairAvailability> getChairAvailabilities() {
		return Collections.unmodifiableList(chairAvailabilities);
	}
	
	public ChairAvailability getChairAvailability(Chair chair) {
		return chairAvailabilities.stream().filter(chairAvailability -> chairAvailability.getChair().equals(chair)).findFirst().get();
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public static class OccupationMapBuilder {
		private OccupationMap occupationMap;
		
		public static OccupationMapBuilder fromSession(NumberedChairSession session) {
			OccupationMapBuilder builder = new OccupationMapBuilder();
			builder.occupationMap = new OccupationMap(session);
			return builder;
		}
		
		public OccupationMapBuilder addChairAvailability(ChairAvailability chairAvailability) {
			occupationMap.chairAvailabilities.add(chairAvailability);
			return this;
		}
		
		public OccupationMap build() {
			Collections.sort(occupationMap.chairAvailabilities, new Comparator<ChairAvailability>() {
				@Override
				public int compare(ChairAvailability o1, ChairAvailability o2) {
					int result = o1.getChair().getRow().compareTo(o2.getChair().getRow());
					if (result == 0) {
						result = o1.getChair().getNumber().compareTo(o2.getChair().getNumber());
					}
					return result;
				}
			});
			
			occupationMap.rows = 0;
			String lastRow = null;
			for (ChairAvailability chairAvailability : occupationMap.chairAvailabilities) {
				if (!chairAvailability.getChair().getRow().equals(lastRow)) {
					lastRow = chairAvailability.getChair().getRow();
					occupationMap.rows++;
				}
			}

			occupationMap.cols = 0;
			for (ChairAvailability chairAvailability : occupationMap.chairAvailabilities) {
				occupationMap.cols = Math.max(occupationMap.cols, chairAvailability.getChair().getNumber());
			}
			
			OccupationMap o = occupationMap;
			occupationMap = null;
			return o;
		}
	}
}
