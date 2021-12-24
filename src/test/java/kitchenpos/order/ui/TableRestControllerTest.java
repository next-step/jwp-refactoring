package kitchenpos.order.ui;

import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.ui.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends RestControllerTest {

    private static final String API_TABLE_ROOT = "/api/tables";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TableService tableService;

    @Test
    void 테이블_생성() throws Exception {
        // given
        OrderTable 빈_테이블 = OrderTableFixture.of(1L, 0, true);
        given(tableService.create(any())).willReturn(빈_테이블);

        // when
        ResultActions actions = mockMvc.perform(post(API_TABLE_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(빈_테이블)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(빈_테이블.getId()))
                .andExpect(jsonPath("$.tableGroupId").value(빈_테이블.getTableGroupId()))
                .andExpect(jsonPath("$.numberOfGuests").value(빈_테이블.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @Test
    void 테이블_조회() throws Exception {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTableFixture.of(1L, 0, true));
        orderTables.add(OrderTableFixture.of(2L, 5, false));

        given(tableService.list()).willReturn(orderTables);

        //when
        ResultActions actions = mockMvc.perform(get(API_TABLE_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderTables.get(0).getId()))
                .andExpect(jsonPath("$[0].tableGroupId").value(orderTables.get(0).getTableGroupId()))
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTables.get(0).getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(true))
                .andExpect(jsonPath("$[1].id").value(orderTables.get(1).getId()))
                .andExpect(jsonPath("$[1].tableGroupId").value(orderTables.get(1).getTableGroupId()))
                .andExpect(jsonPath("$[1].numberOfGuests").value(orderTables.get(1).getNumberOfGuests()))
                .andExpect(jsonPath("$[1].empty").value(false));
    }

    @Test
    void 테이블_상태_변경() throws Exception {
        // given
        OrderTable 빈_테이블 = OrderTableFixture.of(1L, 0, true);
        OrderTable 주문_테이블 = OrderTableFixture.of(1L, 0, false);

        given(tableService.changeEmpty(any(), any())).willReturn(주문_테이블);
        빈_테이블.setEmpty(false);
        // when
        ResultActions actions = mockMvc.perform(put(API_TABLE_ROOT + "/" + 빈_테이블.getId() + "/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(빈_테이블)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(주문_테이블.getId()))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    void 방문한_손님_수_변경() throws Exception {
        // given
        OrderTable 주문_테이블 = OrderTableFixture.of(1L, 0, false);
        OrderTable 방문한_손님_수가_변경된_주문_테이블 = OrderTableFixture.of(1L, 5, false);

        given(tableService.changeNumberOfGuests(any(), any())).willReturn(방문한_손님_수가_변경된_주문_테이블);
        주문_테이블.setNumberOfGuests(5);
        // when
        ResultActions actions = mockMvc.perform(put(API_TABLE_ROOT + "/" + 주문_테이블.getId() + "/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(주문_테이블)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(주문_테이블.getId()))
                .andExpect(jsonPath("$.numberOfGuests").value(주문_테이블.getNumberOfGuests()));
    }

}
