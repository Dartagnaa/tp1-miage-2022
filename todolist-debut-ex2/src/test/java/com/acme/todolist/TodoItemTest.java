package com.acme.todolist;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.acme.todolist.domain.TodoItem;


public class TodoItemTest {

	
	@Test
	void testLate_when_created_25_hours_ago() {
		Instant time =Instant.now().minus(25,ChronoUnit.HOURS);
		TodoItem item = new TodoItem("1111",time,"test");
		
		assertEquals(item.finalContent(),"[LATE!] test");
	}
	
	@Test
	void testLate_when_just_created() {
		Instant time =Instant.now();
		TodoItem item = new TodoItem("1111",time,"test");
		
		assertEquals(item.finalContent(),"test");
	}
	
}
