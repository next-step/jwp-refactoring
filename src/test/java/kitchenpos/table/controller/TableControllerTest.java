package kitchenpos.table.controller;

import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문 테이블을 생성 한다")
    public void createOrderTable() throws Exception {
        OrderTable orderTable = new OrderTable(0, false);

        테이블_생성_요청(orderTable)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("empty").value(false))
        ;
    }

    @Test
    @DisplayName("주문 테이블 리스트를 가져온다")
    public void selectOrderTableList() throws Exception {
        테이블_리스트_요청()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].numberOfGuests").value(0))
                .andExpect(jsonPath("$.[0].empty").value(false))
        ;
    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() throws Exception {
        OrderTable orderTable = new OrderTable(0, true);

        테이블_빈_테이블로_변경_요청(orderTable)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("empty").value(true))
        ;
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() throws Exception {
        OrderTable orderTable = new OrderTable(3, false);

        테이블_손님_수_변경_요청(orderTable)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(3))
                .andExpect(jsonPath("empty").value(false))
        ;
    }


    private ResultActions 테이블_생성_요청(OrderTable orderTable) throws Exception {
        return mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());
    }

    private ResultActions 테이블_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 테이블_빈_테이블로_변경_요청(OrderTable orderTable) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/empty", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());
    }

    private ResultActions 테이블_손님_수_변경_요청(OrderTable orderTable) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print());
    }

}
