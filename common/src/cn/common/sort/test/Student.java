package cn.common.sort.test;

import cn.common.sort.CommonComparator;

public class Student extends CommonComparator {
	
	private static String compareField = "age";
	
	private String name;
	
	private int age;
	
	public Student() {
		super(compareField);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
