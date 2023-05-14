package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto = new UserDto(1L, "user1", "user1@mail.ru");

    private ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "itemre1", UserMapper.toUser(userDto), LocalDateTime.now(), null);

    private List<ItemRequestController> controllerList = new ArrayList<>();

    @Test
    void getItemRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(any(Long.class), any(Long.class)))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.requestor.name", is(itemRequestDto.getRequestor().getName())))
                .andExpect(jsonPath("$.requestor.email", is(itemRequestDto.getRequestor().getEmail())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getAllOwnerItemRequests() throws Exception {
        when(itemRequestService.getAllOwnerItemRequests(any(Long.class)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(controllerList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.[0].requestor.name", is(itemRequestDto.getRequestor().getName())))
                .andExpect(jsonPath("$.[0].requestor.email", is(itemRequestDto.getRequestor().getEmail())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

    }

    @Test
    void getAllItemRequests() throws Exception {
        when(itemRequestService.getAllItemRequests(any(Long.class), any(Integer.class), nullable(Integer.class)))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(controllerList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.[0].requestor.name", is(itemRequestDto.getRequestor().getName())))
                .andExpect(jsonPath("$.[0].requestor.email", is(itemRequestDto.getRequestor().getEmail())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.requestor.name", is(itemRequestDto.getRequestor().getName())))
                .andExpect(jsonPath("$.requestor.email", is(itemRequestDto.getRequestor().getEmail())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));


    }
}