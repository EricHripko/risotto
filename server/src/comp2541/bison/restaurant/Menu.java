/**
 * 
 */
package comp2541.bison.restaurant;

import java.util.ArrayList;

/**
 * @author Jones Agwata
 *
 */
public class Menu {
	private int id;
	private String name;
	private ArrayList<Meal> meals;
	/**
	 * 
	 * @param pName
	 */
	public Menu(String pName) {
		name = pName;
	}
	/**
	 * 
	 * @param pName
	 * @param pMeal
	 */
	public Menu(String pName, ArrayList<Meal> pMeal){
		name = pName;
		meals = pMeal;
	}
	/**
	 * 
	 * @param meal
	 */
	public void addMeal(Meal meal){
		//TODO add functionality to addMeal
	}
	/**
	 * 
	 * @param meal
	 */
	public void removeMeal(Meal meal){
		//TODO add functionality to removeMeal
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
	public ArrayList<Meal> getMeals() {
		return meals;
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
	 * @param pName
	 */
	public void setName(String pName) {
		name = pName;
	}
	
	/**
	 * 
	 * @param pMeals
	 */
	public void setMeals(ArrayList<Meal> pMeals) {
		meals = pMeals;
	}
	
}
