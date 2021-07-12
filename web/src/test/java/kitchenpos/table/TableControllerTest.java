package kitchenpos.table;

import kitchenpos.common.ControllerTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문 테이블을 생성 한다")
    public void createOrderTable() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, TableStatus.ORDER);

        // when
        // then
        테이블_생성_요청(orderTableRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("tableStatus").value(TableStatus.ORDER.name()))
        ;
    }

    @Test
    @DisplayName("주문 테이블 리스트를 가져온다")
    public void selectOrderTableList() throws Exception {
        // when
        // then
        테이블_리스트_요청()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].numberOfGuests").value(0))
                .andExpect(jsonPath("$.[0].tableStatus").value(TableStatus.ORDER.name()))
        ;
    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, TableStatus.EMPTY);

        // when
        // then
        테이블_빈_테이블로_변경_요청(orderTableRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("tableStatus").value(TableStatus.EMPTY.name()))
        ;
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, TableStatus.ORDER);

        // when
        // then
        테이블_손님_수_변경_요청(orderTableRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests").value(3))
                .andExpect(jsonPath("tableStatus").value(TableStatus.ORDER.name()))
        ;
    }


    private ResultActions 테이블_생성_요청(OrderTableRequest orderTableRequest) throws Exception {
        return mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());
    }

    private ResultActions 테이블_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 테이블_빈_테이블로_변경_요청(OrderTableRequest orderTableRequest) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/empty", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());
    }

    private ResultActions 테이블_손님_수_변경_요청(OrderTableRequest orderTableRequest) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());
    }

}
