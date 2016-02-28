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
	 * adds meal to meals arrraylist
	 * @param meal
	 */
	public void addMeal(Meal meal){
		//add meal to meals arraylist
		meals.add(meal);
	}
	/**
	 * Removes meal from arraylist
	 * @param meal
	 */
	public void removeMeal(Meal meal){
		//find index of meal in meals arraylist
		int mealIndex = meals.indexOf(meal);
		
		meals.remove(mealIndex);
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
