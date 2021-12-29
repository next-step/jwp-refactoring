package kitchenpos.table;

import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.ui.TableController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTableControllerTest extends ControllerTest {

    @MockBean
    protected TableService tableService;


    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TableController(tableService)).build();
    }

    private OrderTableRequest createOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableRequest, "id", 1L);
        ReflectionTestUtils.setField(orderTableRequest, "numberOfGuests", 10);
        ReflectionTestUtils.setField(orderTableRequest, "empty", false);
        return orderTableRequest;
    }

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTable() throws Exception {

        OrderTableRequest orderTableRequest = createOrderTable();
        OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTableRequest.toEntity());
        when(tableService.create(any())).thenReturn(orderTableResponse);

        //when
        ResultActions resultActions = post("/api/tables", orderTableRequest);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("주문 테이블 조회하기")
    @Test
    void getTables() throws Exception {

        //given
        final OrderTableRequest orderTable = createOrderTable();
        final List<OrderTableResponse> orderTableResponses = OrderTableResponse.ofList(Arrays.asList(orderTable.toEntity()));
        when(tableService.list()).thenReturn(orderTableResponses);

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
        final OrderTableRequest orderTableRequest = createOrderTable();
        ReflectionTestUtils.setField(orderTableRequest, "empty", isEmpty);
        final OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTableRequest.toEntity());

        when(tableService.empty(anyLong())).thenReturn(orderTableResponse);

        //when
        ResultActions resultActions = put("/api/tables/1/empty", new LinkedMultiValueMap<>(), orderTableRequest);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.empty").value(isEmpty));
    }

    @DisplayName("주문 테이블 손님 수 조정하기")
    @Test
    void changeGuestNumberOfTable() throws Exception {

        //given
        final int changeNumberOfGuests = 10;
        final OrderTableRequest orderTableRequest = createOrderTable();
        final OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTableRequest.toEntity());

        when(tableService.changeNumberOfGuests(anyLong(), any())).thenReturn(orderTableResponse);

        //when
        ResultActions resultActions = put("/api/tables/1/number-of-guests", new LinkedMultiValueMap<>(), orderTableRequest);

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.numberOfGuests").value(changeNumberOfGuests));
    }
}
