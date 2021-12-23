package kitchenpos.order.table;

import kitchenpos.domain.OrderTable;
import kitchenpos.ui.TableRestController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTableControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TableRestController(tableService)).build();
    }

    private OrderTable createOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);
        return orderTable;
    }

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTable() throws Exception {

        OrderTable orderTable = createOrderTable();

        when(tableService.create(any())).thenReturn(orderTable);

        //when
        ResultActions resultActions = post("/api/tables", orderTable);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("주문 테이블 조회하기")
    @Test
    void getTables() throws Exception {

        //given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(createOrderTable());
        when(tableService.list()).thenReturn(orderTables);

        //when
        ResultActions resultActions = get("/api/tables", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['numberOfGuests']").isNumber());
        resultActions.andExpect(jsonPath("$[0]['numberOfGuests']").value(10));
        resultActions.andExpect(jsonPath("$[0]['empty']").value(Boolean.FALSE));
    }

    @DisplayName("주문 테이블 비우기")
    @Test
    void emptyTable() throws Exception {

        //given
        final boolean isEmpty = true;
        OrderTable orderTable = createOrderTable();
        orderTable.setEmpty(isEmpty);

        when(tableService.changeEmpty(anyLong(), any())).thenReturn(orderTable);

        //when
        ResultActions resultActions = put("/api/tables/1/empty", new LinkedMultiValueMap<>(), orderTable);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.empty").value(isEmpty));
    }

    @DisplayName("주문 테이블 손님 수 조정하기")
    @Test
    void changeGuestNumberOfTable() throws Exception {

        //given
        final int updatedNumberOfGuests = 7;
        OrderTable orderTable = createOrderTable();
        orderTable.setNumberOfGuests(updatedNumberOfGuests);

        when(tableService.changeNumberOfGuests(anyLong(), any())).thenReturn(orderTable);

        //when
        ResultActions resultActions = put("/api/tables/1/number-of-guests", new LinkedMultiValueMap<>(), orderTable);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.numberOfGuests").value(updatedNumberOfGuests));

    }
}
