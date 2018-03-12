package com.lithium.test.cases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.lithium.inject.Injector;
import com.lithium.inject.config.Inject;

public class TestConfiguration {
	
	@Inject
	static Injector injector;

	@Test
	public void testInteger(){
		Integer i = injector.getDependency(Integer.class);
		assertTrue("Integer was not loaded from configuration", i == 3);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testList(){
		List<String> list = injector.getDependency(List.class);
		assertFalse("Stored list is empty", list.isEmpty());
		assertTrue("List was not loaded from configuration", list.get(0).equals("first entry"));
	}
}
