package com.example.todolists_springboot.contrller;


import com.example.todolists_springboot.controller.UserController;
import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.service.TaskService;
import com.example.todolists_springboot.service.TaskUserService;
import com.example.todolists_springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private TaskUserService taskUserService;
    @MockBean
    private TaskService taskService;
    @Autowired
    private JacksonTester<User> userJson;
    @Autowired
    private JacksonTester<List<User>> userListJson;
    @Autowired
    private JacksonTester<Task> taskJson;
    @Autowired
    private JacksonTester<List<Task>> taskListJson;

    @Test
    void should_add_a_user_and_return_the_user() throws Exception {
        User requestBody = new User("test");
        MockHttpServletRequestBuilder requestBuilder = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(userService).addUser(requestBody);
    }

    @Test
    void should_get_all_the_users() throws Exception {
        User test = new User(1L, "test");
        when(userService.getUsers()).thenReturn(List.of(test));
        MockHttpServletResponse response = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(userListJson.write(List.of(test)).getJson());
        verify(userService).getUsers();
    }

    @Test
    void should_get_the_user_by_id() throws Exception {
        User test = new User(1L, "test");
        when(userService.getUserById(1L)).thenReturn(Optional.of(test));
        MockHttpServletResponse response = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(userJson.write(test).getJson());
        verify(userService).getUserById(1L);
    }

    @Test
    void should_update_the_user_by_id_when_the_user_exists() throws Exception {
        User requestBody = new User("test test");
        User newUser = new User(1L, "test test");
        when(userService.updateUser(1L, requestBody)).thenReturn(newUser);
        MockHttpServletRequestBuilder requestBuilder = put("/users/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(userJson.write(newUser).getJson());
        verify(userService).updateUser(1L, requestBody);
    }

    @Test
    void should_update_the_user_by_id_when_the_user_does_not_exist() throws Exception {
        User requestBody = new User("test test");
        User newUser = new User(1L, "test test");
        when(userService.updateUser(1L, requestBody)).thenThrow(new UserNotFoundException());
        MockHttpServletRequestBuilder requestBuilder = put("/users/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(userService).updateUser(1L, requestBody);
    }

    @Test
    void should_delete_the_user_when_the_user_exits() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete("/users/{id}", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void should_delete_the_user_when_the_user_does_not_exit() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).deleteUser(1L);
        MockHttpServletRequestBuilder requestBuilder = delete("/tasks/{id}", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_create_a_task_to_the_user_when_the_user_not_exist() throws Exception {
        Task requestBody = new Task("test");
        when(taskUserService.createTaskForUser(1L, requestBody)).thenThrow(new UserNotFoundException());
        MockHttpServletRequestBuilder requestBuilder = post("/users/create_task/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(taskUserService).createTaskForUser(1L, requestBody);
    }

    @Test
    void should_create_a_task_to_the_user_when_the_user_exist_but_task_not_exist() throws Exception {
        Task requestBody = new Task("test");
        User savedUser = new User(1L, "lacey");
        User theReturnUser = new User(1L, "lacey", List.of(new Task(1L, "test", false)));
        Task theReturnedTask = new Task(1L, "test", false);

        when(userService.getUserById(1L)).thenReturn(Optional.of(savedUser));
        when(taskService.getTaskByTaskId(1L)).thenReturn(null);
        when(taskUserService.createTaskForUser(1L, requestBody)).thenReturn(theReturnedTask);
        MockHttpServletRequestBuilder requestBuilder = post("/users/create_task/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(theReturnedTask).getJson());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(taskUserService).createTaskForUser(1L, requestBody);
    }

    @Test
    void should_create_a_task_to_the_user_when_the_user_and_task_exist_and_task_belong_to_user() throws Exception {
        Task requestBody = new Task(1L, "test", false);
        User savedUser = new User(1L, "lacey");
        Task theReturnedTask = new Task(1L, "test", false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(savedUser));
        when(taskService.getTaskByTaskId(1L)).thenReturn(Optional.of(requestBody));
        when(taskUserService.getUsersOfTaskByTaskId(1L)).thenReturn(List.of(savedUser));
        when(taskUserService.createTaskForUser(1L, requestBody)).thenReturn(theReturnedTask);
        MockHttpServletRequestBuilder requestBuilder = post("/users/create_task/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(theReturnedTask).getJson());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(taskUserService).createTaskForUser(1L, requestBody);
    }

    @Test
    void should_create_a_task_to_the_user_when_the_user_and_task_exist_and_task_not_belong_to_user() throws Exception {
        Task requestBody = new Task(1L, "test", false);
        User savedUser = new User(1L, "lacey");
        Task theReturnedTask = new Task(1L, "test", false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(savedUser));
        when(taskService.getTaskByTaskId(1L)).thenReturn(Optional.of(requestBody));
        when(taskUserService.getUsersOfTaskByTaskId(1L)).thenReturn(new ArrayList<>());
        when(taskUserService.createTaskForUser(1L, requestBody)).thenReturn(theReturnedTask);
        MockHttpServletRequestBuilder requestBuilder = post("/users/create_task/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(theReturnedTask).getJson());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(taskUserService).createTaskForUser(1L, requestBody);
    }

    @Test
    void should_get_all_tasks_of_the_user_by_user_id() throws Exception {
        List<Task> theReturnedTasks = List.of(new Task(1L, "test", false), new Task(2L, "test2", false));
        when(taskUserService.getTasksOfUserByUserId(1L)).thenReturn(theReturnedTasks);
        MockHttpServletResponse response = mockMvc.perform(get("/users/tasks_by_user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskListJson.write(theReturnedTasks).getJson());
        verify(taskUserService).getTasksOfUserByUserId(1L);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_user_does_not_exist() throws Exception {
        Task requestBody = new Task(1L, "test test", false);
        when(taskUserService.updateTaskForUser(1L, 1L, requestBody)).thenThrow(new UserNotFoundException());
        MockHttpServletRequestBuilder requestBuilder = put("/users/{user_id}/tasks/update/{task_id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(taskUserService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_task_does_not_exist() throws Exception {
        Task requestBody = new Task(1L, "test test", false);
        when(taskUserService.updateTaskForUser(1L, 1L, requestBody)).thenThrow(new TaskNotFoundException());
        MockHttpServletRequestBuilder requestBuilder = put("/users/{user_id}/tasks/update/{task_id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(taskUserService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_task_exist() throws Exception {
        Task requestBody = new Task(1L, "test test", false);
        when(taskUserService.updateTaskForUser(1L, 1L, requestBody)).thenReturn(requestBody);
        MockHttpServletRequestBuilder requestBuilder = put("/users/{user_id}/tasks/update/{task_id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        verify(taskUserService).updateTaskForUser(1L, 1L, requestBody);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(requestBody).getJson());
    }

    @Test
    void should_get_all_tasks_of_the_user_by_user_name() throws Exception {

        List<Task> theReturnedTasks = List.of(new Task(1L, "test", false), new Task(2L, "test2", false));
        when(taskUserService.getTasksOfUserByUserName("test")).thenReturn(theReturnedTasks);
        MockHttpServletResponse response = mockMvc.perform(get("/users/tasks/{name}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskListJson.write(theReturnedTasks).getJson());
        verify(taskUserService).getTasksOfUserByUserName("test");
    }

    @Test
    void should_delete_all_the_tasks_of_the_user_when_the_user_not_exist() throws Exception {
        doThrow(new UserNotFoundException()).when(taskUserService).deleteAllTasksOfUserByUserId(1L);
        MockHttpServletRequestBuilder requestBuilder = delete("/users/tasks/{id}", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_delete_all_the_tasks_of_the_user_when_the_user_exist() throws Exception {
        when(taskUserService.deleteAllTasksOfUserByUserId(1L)).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = delete("/users/tasks/{id}", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(taskUserService).deleteAllTasksOfUserByUserId(1L);
    }

    @Test
    void should_shared_all_the_tasks_to_the_user_when_the_user_not_exist() throws Exception {
        when(taskUserService.addSharedTasks(1L, 2L)).thenThrow(new UserNotFoundException());
        MockHttpServletRequestBuilder requestBuilder = put("/users/{sharedId}/shared/{id}", 1L, 2L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_shared_all_the_tasks_to_the_user_when_the_user_exist() throws Exception {
        List<Task> theReturnedtasks = List.of(new Task(1L, "test", false));
        when(taskUserService.addSharedTasks(1L, 2L)).thenReturn(theReturnedtasks);
        MockHttpServletRequestBuilder requestBuilder = put("/users/{sharedId}/shared/{id}", 1L, 2L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskListJson.write(theReturnedtasks).getJson());
    }
}
