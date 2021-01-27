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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 생성 확인")
    public void whenPostOrderTable_thenReturnStatus() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        when(tableService.create(any())).thenReturn(orderTable);

        mockMvc.perform(post("/api/tables")
                .content(asJsonString(orderTable))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 주문테이블 조회")
    public void givenOrderTable_whenGetOrderTable_thenReturnStauts() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        given(tableService.list()).willReturn(Arrays.asList(orderTable));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 주문테이블 빈테이블 여부 수정")
    void givenOrderTable_whenPutOrderTable_thenReturnStatus() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        given(tableService.list()).willReturn(Arrays.asList(orderTable));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk());

        orderTable.setEmpty(false);
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .content(asJsonString(orderTable))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 주문테이블 방문한 손님 수 수정")
    void givenOrderTable_whenPutOrderTableNumberOfGuest_thenReturnStatus() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        given(tableService.list()).willReturn(Arrays.asList(orderTable));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk());

        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .content(asJsonString(orderTable))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}