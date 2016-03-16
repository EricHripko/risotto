package comp2541.bison.restaurant;

import org.json.JSONObject;

/**
 * Class implementing the tables of the restaurant.
 * 
 * @author Michele Cipriano
 *
 */
public class Table {

	private int id;
	private String description;
	private int size;
	
	public Table(int pId) {
		id = pId;
	}
	
	public Table(String pDescription, int pSize) {
		description = pDescription;
		size = pSize;
	}
	
	public Table(int pId, String pDescription, int pSize) {
		id = pId;
		description = pDescription;
		size = pSize;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @param size the size to set
	 */
	
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * 
	 * @return The JSON object of the Table.
	 */
	public JSONObject getJSONObject() {
		JSONObject jsonTable = new JSONObject();
		
		jsonTable.put("id", id);
		jsonTable.put("description", description);
		jsonTable.put("size", size);
		
		return jsonTable;
	}
	
}
