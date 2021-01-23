package kitchenpos.ordertable.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderTableRestController.class)
class OrderTableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderTableService orderTableService;

    @DisplayName("테이블을 등록한다.")
    @Test
    public void create() throws Exception {
        OrderTableResponse orderTable = new OrderTableResponse(1L, 1L, 10, false);
        when(orderTableService.create(any())).thenReturn(orderTable);

        mockMvc.perform(post("/api/tables")
            .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(redirectedUrl("/api/tables/" + orderTable.getId()))
            .andExpect(status().isCreated());
    }

    @DisplayName("테이블을 빈테이블로 변경한다.")
    @Test
    public void changeEmpty() throws Exception {
        OrderTableResponse orderTable = new OrderTableResponse(1L, 1L, 10, false);
        when(orderTableService.changeEmpty(anyLong(), any(OrderTableRequest.class))).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
            .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.empty", is(false)))
            .andExpect(status().isOk());
    }

    @DisplayName("테이블의 게스트 숫자를 변경한다.")
    @Test
    public void changeNumberOfGuests() throws Exception {
        OrderTableResponse orderTable = new OrderTableResponse(1L, 1L, 10, false);
        when(orderTableService.changeNumberOfGuests(anyLong(), any(OrderTableRequest.class))).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
            .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.numberOfGuests", is(10)))
            .andExpect(status().isOk());
    }
}
