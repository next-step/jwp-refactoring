package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.service.OrderTableServiceJpa;
import kitchenpos.table.util.OrderTableBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableRestController.class)
class OrderTableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderTableServiceJpa tableService;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void createTable() throws Exception {
        OrderTableRequest orderTableRequest =
                new OrderTableBuilder()
                        .withNumberOfGuests(4)
                        .withEmpty(true).requestBuild();
        OrderTableResponse orderTableResponse =
                new OrderTableBuilder()
                        .withId(1L)
                        .withNumberOfGuests(4)
                        .withEmpty(true).responseBuilder();

        when(tableService.create(any())).thenReturn(orderTableResponse);

        mockMvc.perform(post("/api/tables")
                .content(objectMapper.writeValueAsString(orderTableRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(redirectedUrl("/api/tables/" + orderTableResponse.getId()))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블에 상태값을 변경 가능하다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTableRequest orderTableRequest =
                new OrderTableBuilder()
                        .withEmpty(false).requestBuild();
        OrderTableResponse orderTableResponse =
                new OrderTableBuilder()
                        .withId(1L)
                        .withNumberOfGuests(5)
                        .withEmpty(false).responseBuilder();
        when(tableService.changeEmpty(anyLong(), anyBoolean())).thenReturn(orderTableResponse);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableResponse.getId())
                .content(objectMapper.writeValueAsString(orderTableRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.empty", is(false)))
                .andExpect(status().isOk());
    }

    @DisplayName("테이블에 인원을 변경 가능하다.")
    @Test
    void changeGuests() throws Exception {
        OrderTableRequest orderTableRequest =
                new OrderTableBuilder()
                        .withNumberOfGuests(5).requestBuild();

        OrderTableResponse orderTableResponse =
                new OrderTableBuilder()
                        .withId(1L)
                        .withNumberOfGuests(5)
                        .withEmpty(true).responseBuilder();

        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTableRequest.class))).thenReturn(orderTableResponse);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableResponse.getId())
                .content(objectMapper.writeValueAsString(orderTableRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.numberOfGuests", is(5)))
                .andExpect(status().isOk());
    }
}