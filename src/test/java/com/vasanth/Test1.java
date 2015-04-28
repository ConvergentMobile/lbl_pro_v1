package com.vasanth;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test1 {
	@BeforeClass
	public static void beforeClassTest(){
		System.out.println(" before class test case ");
	}
	@Before
	public void start(){
		System.out.println("test before");
	}

	@Test
	public void m1() {
		System.out.println("m1 method test case");

	}
	
	@Test
	public void m2() {
		System.out.println("m2 method test case");
	}
	
	@After
public void testafter(){
		System.out.println("Test after");
	}
	
	@AfterClass
	public static void afterClassTest(){
		System.out.println("AFter class test case");
	}

}
