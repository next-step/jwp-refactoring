package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : TableRestControllerTest
 * author : haedoang
 * date : 2021/12/15
 * description :
 */
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    private OrderTable orderTable;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
    }

    @Test
    @DisplayName("주문 테이블을 조회한다")
    public void getOrderTable() throws Exception {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable);
        given(tableService.list()).willReturn(orderTables);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numberOfGuests", is(2)))
                .andExpect(jsonPath("$[0].empty", is(false)));
    }

    @Test
    @DisplayName("주문 테이블을 등록한다")
    public void postOrderTable() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(tableService.create(any(OrderTable.class))).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderTable))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.numberOfGuests", is(2)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @Test
    @DisplayName("주문 테이블의 가용 상태를 변경한다")
    public void putOrderTableEmpty() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        orderTable.setEmpty(true);
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/tables/" + orderTable.getId() + "/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderTable))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @Test
    @DisplayName("주문 테이블의 사용자 수를 변경한다")
    public void putOrderTableGuests() throws Exception {
        // given
        final int CHANGED_GUEST_NUMBERS = 10;
        ObjectMapper mapper = new ObjectMapper();
        orderTable.setNumberOfGuests(CHANGED_GUEST_NUMBERS);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(orderTable);

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(orderTable))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests", is(CHANGED_GUEST_NUMBERS)));
    }
}