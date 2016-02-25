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
	 */
	public Menu(String pName) {
		name = pName;
	}
	public Menu(String pName, ArrayList<Meal> pMeal){
		name = pName;
		meals = pMeal;
	}
	public void addMeal(Meal meal){
		//TODO add functionality to addMeal
	}
	public void removeMeal(Meal meal){
		//TODO add functionality to removeMeal
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Meal> getMeals() {
		return meals;
	}
	public void setMeals(ArrayList<Meal> meals) {
		this.meals = meals;
	}
	
}
