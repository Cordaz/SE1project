package it.polimi.ingsw.BianchiCorneo.maps.sectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Set of sectors
 * 
 * @author Mattia Bianchi
 *
 */
public class SectorList  implements Serializable {
	private static final long serialVersionUID = -6890637607620290681L;
	private ArrayList<Sector> sectorList;
	
	/**
	 * Constructor that receives a set in input and builds the new set of sectors 
	 * 
	 * @param set
	 */
	public SectorList(Set<Sector> set) {
		sectorList = new ArrayList<Sector>(set);
	}
	
	/**
	 * Basic constructor for the set
	 */
	public SectorList() {
		sectorList = new ArrayList<Sector>();
	}

	/**
	 * Add a sector to the set
	 * 
	 * @param s sector to add
	 */
	public void add(Sector s) {
		sectorList.add(s);
	}
	
	/**
	 * Removes the first occurrence of the specified element from this list, if it is present. 
	 * If the list does not contain the element, it is unchanged.
	 * 
	 * @param s sector to remove
	 */
	public void remove(Sector s) {
		sectorList.remove(s);
	}
	
	/**
	 * Check if the set is empty
	 * 
	 * @return true if the list contains no elements
	 */
	public boolean isEmpty() {
		return sectorList.isEmpty();
	}
	
	/**
	 * Get one sector placed at the indicated position
	 * 
	 * @param index index of the element to return
	 * @return the sector at the given position in the set
	 */
	public Sector get(int index) {
		return sectorList.get(index);
	}
	
	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return sectorList.size();
	}
	
	/**
	 * Returns true is a given sector is in the list
	 * 
	 * @param s sector to check
	 * @return true if the sector is in the list
	 */
	public boolean contains(Sector s) {
		for (Sector x : sectorList)
			if (x.sameAs(s))
				return true;
		return false;
	}
}
