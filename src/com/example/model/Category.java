package com.example.model;
public class Category {
	Integer categoryIconId;
	String categoryName;

	public Category() {
	}
	
	public Category(String name, Integer iconId){
		this.categoryIconId=iconId;
		this.categoryName=name;
	}
	public Integer getCategoryIconId() {
		return categoryIconId;
	}

	public void setCategoryIconId(Integer categoryIconId) {
		this.categoryIconId = categoryIconId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}