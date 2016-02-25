package comp2541.bison.restaurant;
/**
 * 
 * @author Jones Agwata
 *
 */
public class Meal {
	private int id;
	private String mealName;
	private String mealDescription;
	private int mealPrice;
	private String mealType;
	
	public Meal(int pId,String pName, String pDescription, int pPrice, String pType){
		this.id = pId;
		this.mealName = pName;
		this.mealDescription = pDescription;
		this.mealPrice = pPrice;
		this.mealType = pType;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getMealDescription() {
		return mealDescription;
	}

	public void setMealDescription(String mealDescription) {
		this.mealDescription = mealDescription;
	}

	public int getMealPrice() {
		return mealPrice;
	}

	public void setMealPrice(int mealPrice) {
		this.mealPrice = mealPrice;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
}
