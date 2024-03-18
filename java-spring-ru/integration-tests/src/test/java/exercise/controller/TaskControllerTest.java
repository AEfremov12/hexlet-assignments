package exercise.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    // BEGIN
    @Test
    public void testShow() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        checkTaskFromJson(body, task);
    }

    @Test
    public void testCreate() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var updatedAt = task.getUpdatedAt();
        var createdAt = task.getCreatedAt();
        var taskString = "{" + "\"id\":" + task.getId() + "," + "\"title\":" + "\"" + task.getTitle() + "\"" + "," + "\"description\":" + "\"" + task.getDescription() + "\""+ "," + "\"updatedAt\":[" + updatedAt.getYear() + "," + updatedAt.getMonthValue() + "," + updatedAt.getDayOfMonth() + "]," + "\"createdAt\":[" + createdAt.getYear() + "," + createdAt.getMonthValue() + "," + createdAt.getDayOfMonth() + "]}";
        var jsonObject = new JSONObject(taskString);
        var result = mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isCreated())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        checkTaskFromJson(body, task);
    }

    @Test
    public void testUpdate() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var dbres = taskRepository.findByTitle(task.getTitle());
        var dbId = task.getId();
        if (dbres.isPresent()) { dbId = dbres.get().getId(); }
        task.setTitle(faker.lorem().word());
        task.setDescription(faker.lorem().paragraph());
        var updatedAt = task.getUpdatedAt();
        var createdAt = task.getCreatedAt();
        var taskString = "{" + "\"id\":" + task.getId() + "," + "\"title\":" + "\"" + task.getTitle() + "\"" + "," + "\"description\":" + "\"" + task.getDescription() + "\""+ "," + "\"updatedAt\":[" + updatedAt.getYear() + "," + updatedAt.getMonthValue() + "," + updatedAt.getDayOfMonth() + "]," + "\"createdAt\":[" + createdAt.getYear() + "," + createdAt.getMonthValue() + "," + createdAt.getDayOfMonth() + "]}";
        var jsonObject = new JSONObject(taskString);
        task.setId(dbId);
        var result = mockMvc.perform(put("/tasks/" + task.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        checkTaskFromJson(body, task);
    }
    @Test
    public void testDelete() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var result = mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertThatJson(taskRepository.findById(task.getId()).isEmpty());
    }

    private void checkTaskFromJson(String body, Task task) {
        assertThatJson(body).and(
                taskFromJson -> taskFromJson.node("id").isEqualTo(task.getId()),
                taskFromJson -> taskFromJson.node("title").isEqualTo(task.getTitle()),
                taskFromJson -> taskFromJson.node("description").isEqualTo(task.getDescription()),
                taskFromJson -> taskFromJson.node("updatedAt").asString().isEqualTo(task.getUpdatedAt().toString()),
                taskFromJson -> taskFromJson.node("createdAt").asString().isEqualTo(task.getCreatedAt().toString())
        );
    }
    // END
}
