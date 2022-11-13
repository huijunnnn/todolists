package com.example.todolists_springboot.contrller;


import com.example.todolists_springboot.controller.UserController;
import com.example.todolists_springboot.domain.Task;
import com.example.todolists_springboot.domain.User;
import com.example.todolists_springboot.handler.exception.TaskNotExistInYourTasksException;
import com.example.todolists_springboot.handler.exception.TaskNotFoundException;
import com.example.todolists_springboot.handler.exception.UserNotFoundException;
import com.example.todolists_springboot.service.AssignmentService;
import com.example.todolists_springboot.service.TaskService;
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

import java.util.List;

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
    private AssignmentService assignmentService;
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
    void should_add_an_user_and_return_the_user() throws Exception {
        User requestBody = new User("test");

        MockHttpServletRequestBuilder requestBuilder = post("/api/users")
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
        when(userService.getAllUsers()).thenReturn(List.of(test));

        MockHttpServletResponse response = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(userListJson.write(List.of(test)).getJson());
        verify(userService).getAllUsers();
    }

    @Test
    void should_get_the_user_by_id_when_the_user_exists() throws Exception {
        User test = new User(1L, "test");
        when(userService.getUserById(1L)).thenReturn(test);
        MockHttpServletResponse response = mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(userJson.write(test).getJson());
        verify(userService).getUserById(1L);
    }

    @Test
    void should_get_the_user_by_id_when_the_user_not_exist() {
        doThrow(new UserNotFoundException()).when(userService).getUserById(1L);

        MockHttpServletResponse response = null;
        try {
            response = mockMvc.perform(get("/api/users/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
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
    void should_update_the_user_by_id_when_the_user_does_not_exist() {
        User requestBody = new User("test test");
        User newUser = new User(1L, "test test");
        when(userService.updateUser(1L, requestBody)).thenThrow(new UserNotFoundException());

        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = put("/api/users/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson.write(requestBody).getJson());
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(userService).updateUser(1L, requestBody);
    }

    @Test
    void should_delete_the_user_when_the_user_exits() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete("/api/users/{id}", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void should_delete_the_user_when_the_user_does_not_exit() {
        doThrow(new UserNotFoundException()).when(userService).deleteUser(1L);

        MockHttpServletRequestBuilder requestBuilder = delete("/api/tasks/{id}", 1L);
        MockHttpServletResponse response = null;
        try {
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_create_a_task_to_the_user_when_the_user_not_exist() {
        Task requestBody = new Task("test");
        when(assignmentService.createTaskForUser(1L, requestBody)).thenThrow(new UserNotFoundException());

        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = post("/api/users/{id}/tasks", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(taskJson.write(requestBody).getJson());
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(assignmentService).createTaskForUser(1L, requestBody);
    }

    @Test
    void should_get_all_tasks_of_the_user_by_user_id_when_the_user_exists() throws Exception {
        List<Task> theReturnedTasks = List.of(new Task(1L, "test", false), new Task(2L, "test2", false));
        when(assignmentService.getTasksOfUserByUserId(1L)).thenReturn(theReturnedTasks);

        MockHttpServletResponse response = mockMvc.perform(get("/api/users/{id}/tasks", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskListJson.write(theReturnedTasks).getJson());
        verify(assignmentService).getTasksOfUserByUserId(1L);
    }

    @Test
    void should_get_all_tasks_of_the_user_by_user_id_when_the_user_not_exist() {
        doThrow(new UserNotFoundException()).when(assignmentService).getTasksOfUserByUserId(1L);

        MockHttpServletResponse response = null;
        try {
            response = mockMvc.perform(get("/api/users/{id}/tasks", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(assignmentService).getTasksOfUserByUserId(1L);
    }

    @Test
    void should_delete_all_the_tasks_of_the_user_when_the_user_not_exist() {
        doThrow(new UserNotFoundException()).when(assignmentService).deleteAllTasksOfUserByUserId(1L);

        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = delete("/api/users/{id}/tasks", 1L);
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_delete_all_the_tasks_of_the_user_when_the_user_exists() throws Exception {
        when(assignmentService.deleteAllTasksOfUserByUserId(1L)).thenReturn(null);

        MockHttpServletRequestBuilder requestBuilder = delete("/api/users/{id}/tasks", 1L);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(assignmentService).deleteAllTasksOfUserByUserId(1L);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_user_does_not_exist() throws Exception {
        Task requestBody = new Task("test test", false);
        when(assignmentService.updateTaskForUser(1L, 1L, requestBody)).thenThrow(new UserNotFoundException());

        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = put("/api/users/{user_id}/tasks/{task_id}", 1L, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(taskJson.write(requestBody).getJson());
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(assignmentService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_task_does_not_exist() {
        Task requestBody = new Task("test test", false);
        when(assignmentService.updateTaskForUser(1L, 1L, requestBody)).thenThrow(new TaskNotFoundException());
        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = put("/api/users/{user_id}/tasks/{task_id}", 1L, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(taskJson.write(requestBody).getJson());
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof TaskNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("Task not found");
            }
        }
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(assignmentService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_task_exist_but_not_belong_to_the_user() {
        Task requestBody = new Task("test test", false);
        when(assignmentService.updateTaskForUser(1L, 1L, requestBody)).thenThrow(new TaskNotFoundException());

        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = put("/api/users/{user_id}/tasks/{task_id}", 1L, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(taskJson.write(requestBody).getJson());
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof TaskNotExistInYourTasksException) {
                assertThat(e.getMessage()).isEqualTo("Task not found in your tasks");
            }
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(assignmentService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_update_the_task_of_the_user_by_id_when_the_task_exist_and_belong_to_the_user() throws Exception {
        Task requestBody = new Task("test test", false);
        Task returnedtask = new Task(1L, "test test", false);
        when(assignmentService.updateTaskForUser(1L, 1L, requestBody)).thenReturn(returnedtask);

        MockHttpServletRequestBuilder requestBuilder = put("/api/users/{user_id}/tasks/{task_id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(requestBody).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(returnedtask).getJson());
        verify(assignmentService).updateTaskForUser(1L, 1L, requestBody);
    }

    @Test
    void should_shared_the_task_to_the_user_when_the_user_not_exist() {
        when(assignmentService.addSharedTask(1L, 2L)).thenThrow(new UserNotFoundException());
        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = post("/api/users/{id}/tasks/addition/{sharedTaskId}", 1L, 2L);
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("User not found");
            }
        }
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_shared_the_task_to_the_user_when_the_task_not_exist() {
        when(assignmentService.addSharedTask(1L, 2L)).thenThrow(new TaskNotFoundException());
        MockHttpServletResponse response = null;
        try {
            MockHttpServletRequestBuilder requestBuilder = post("/api/users/{id}/tasks/addition/{sharedTaskId}", 1L, 2L);
            response = mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            if (e instanceof TaskNotFoundException) {
                assertThat(e.getMessage()).isEqualTo("Task not found");
            }
        }
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_share_the_task_to_the_user_when_the_user_and_task_exist() throws Exception {
        Task theReturnedTask = new Task(2L, "test", false);
        Task task = new Task("test");
        when(assignmentService.addSharedTask(1L, 2L)).thenReturn(theReturnedTask);
        MockHttpServletRequestBuilder requestBuilder = post("/api/users/{id}/tasks/addition/{sharedTaskId}", 2L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson.write(task).getJson());
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(taskJson.write(theReturnedTask).getJson());
    }
}
