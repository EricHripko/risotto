package comp2541.bison.restaurant;
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
	public Meal(String pName, String pDescription, int pPrice, String pType){
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
