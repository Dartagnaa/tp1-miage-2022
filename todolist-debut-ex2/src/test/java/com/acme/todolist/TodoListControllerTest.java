package com.acme.todolist;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import com.acme.todolist.adapters.rest_api.TodoListController;
import com.acme.todolist.application.port.in.AddTodoItem;
import com.acme.todolist.application.port.in.GetTodoItems;
import com.acme.todolist.domain.TodoItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;


@WebMvcTest(controllers = TodoListController.class)
@ContextConfiguration(classes= {TodoListController.class})
public class TodoListControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GetTodoItems getTodoItems;
	
	@MockBean
	private AddTodoItem addTodoItem;

	private ObjectMapper mapper = new ObjectMapper();

	@PostConstruct
	void init() {
		mapper.registerModule(new JavaTimeModule());
		mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
	}

	@Test
	public void should_fetch_all_todo_items() throws Exception{
		List<TodoItem> listItem = mockTodoItem("u",Instant.now(),"test");
		
		when(getTodoItems.getAllTodoItems()).thenReturn(listItem);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/todos"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].content").value("test"))
		.andExpect(jsonPath("$[1].id").value("u1"))
		.andExpect(jsonPath("$[9].id").value("u9"))
		.andExpect(jsonPath("$[0]").exists())
		.andExpect(jsonPath("$[*].id").isNotEmpty());
	}
	
	@Test
	public void should_not_fetch_all_todo_items() throws Exception{
		
		when(getTodoItems.getAllTodoItems()).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/todos"))
		.andExpect(status().isOk());
	}
	

	@Test
	public void should_add_item() throws Exception{
		TodoItem item = mockTodoItem("u",Instant.now(),"test").get(0);
		doNothing().when(addTodoItem).addTodoItem(item);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/todos")
				.content(asJsonString(item))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());

	}
	
	public String asJsonString(final Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	List<TodoItem> mockTodoItem(String id,Instant time,String content){
		List<TodoItem> todoItems = new ArrayList<>();
		for(int index = 0;index<10;index++) {
			TodoItem item = new TodoItem(id+index,time,content);
			todoItems.add(item);
		}
		return todoItems;
	}

}
