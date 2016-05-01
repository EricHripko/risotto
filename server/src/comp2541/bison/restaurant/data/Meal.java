package comp2541.bison.restaurant.data;

import org.json.JSONObject;

/**
 * 
 * @author Jones Agwata
 *
 */
public class Meal {
	
	private int id;
	private String name;
	private String description;
	private int price;
	private String type;
	
	/**
	 * 
	 * @param pName
	 * @param pDescription
	 * @param pPrice
	 * @param pType
	 */
	public Meal(int pID, String pName, String pDescription, int pPrice, String pType){
		id = pID;
		name = pName;
		description = pDescription;
		price = pPrice;
		type = pType;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	public JSONObject getJSONObject() {
		JSONObject jsonMeal = new JSONObject();
		
		jsonMeal.put("id", id);
		jsonMeal.put("name", name);
		jsonMeal.put("description", description);
		jsonMeal.put("price", price);
		jsonMeal.put("category", type);
		
		return jsonMeal;
	}
	
	/**
	 * 
	 * @param pId
	 */
	public void setId(int pId) {
		id = pId;
	}
	
	/**
	 * 
	 * @param pMealName
	 */
	public void setName(String pMealName) {
		name = pMealName;
	}
	
	/**
	 * 
	 * @param pDescription
	 */
	public void setDescription(String pDescription) {
		description = pDescription;
	}
	
	/**
	 * 
	 * @param pPrice
	 */
	public void setPrice(int pPrice) {
		price = pPrice;
	}
	
	/**
	 * 
	 * @param pType
	 */
	public void setType(String pType) {
		type = pType;
	}
}
