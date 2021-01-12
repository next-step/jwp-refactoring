package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void createTable() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        when(tableService.create(any())).thenReturn(orderTable);

        mockMvc.perform(post("/api/tables")
                .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(redirectedUrl("/api/tables/" + orderTable.getId()))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블에 상태값을 변경 가능하다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 4, false);
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.empty", is(false)))
                .andExpect(status().isOk());
    }

    @DisplayName("테이블에 인원을 변경 가능하다.")
    @Test
    void changeGuests() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 5, false);
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .content(objectMapper.writeValueAsString(orderTable)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.numberOfGuests", is(5)))
                .andExpect(status().isOk());
    }
}