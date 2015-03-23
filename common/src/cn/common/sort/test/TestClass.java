package cn.common.sort.test;

import java.util.ArrayList;
import java.util.List;

import cn.common.sort.SortUtils;

public class TestClass {
	
	public static void main(String[] args) {
		List<Student> datas = new ArrayList<>();
		Student stu1 = new Student();
		stu1.setAge(20);
		stu1.setName("stu1");
		datas.add(stu1);
		
		Student stu2 = new Student();
		stu2.setAge(19);
		stu2.setName("stu2");
		datas.add(stu2);
		
		Student stu3 = new Student();
		stu3.setAge(21);
		stu3.setName("stu3");
		datas.add(stu3);
		
		Student stu4 = new Student();
		stu4.setAge(18);
		stu4.setName("stu4");
		datas.add(stu4);
		
		Student stu5 = new Student();
		stu5.setAge(19);
		stu5.setName("stu5");
		datas.add(stu5);
		
		SortUtils.sort(datas, SortUtils.SORD_ASC);
		for (Student stu : datas) {
			System.out.println(stu.getName() + " " + stu.getAge());
		}
	}
}
