package com.acme.todolist;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.acme.todolist.domain.TodoItem;


public class TodoItemTest {

	@Test
	void testLate() {
		Instant time =Instant.parse("2022-04-27T10:31:43.00Z");
		TodoItem item = new TodoItem("1111",time,"test");
		
		assertEquals("[LATE!] test",item.finalContent());
	}
}
