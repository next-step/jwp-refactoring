package kitchenpos.table.ui;

import static kitchenpos.table.application.TableServiceTest.두명;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;
import java.util.List;
import kitchenpos.table.RestControllerTest;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 테이블 API")
@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends RestControllerTest<OrderTableRequest> {

    public static final String BASE_URL = "/api/tables";

    @MockBean
    private OrderTableService tableService;

    private OrderTable orderTable = new OrderTable(1L, 두명);

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(tableService.create(any())).willReturn(OrderTableResponse.of(orderTable));

        // When & Then
        post(BASE_URL, OrderTableRequest.of(orderTable))
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTable.isEmpty()));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<OrderTable> orderTables = Collections.singletonList(orderTable);
        given(tableService.list()).willReturn(OrderTableResponse.listOf(orderTables));

        // When & Then
        get(BASE_URL)
            .andExpect(jsonPath("$.*", hasSize(orderTables.size())));
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        // Given
        given(tableService.changeEmpty(any(), any())).willReturn(OrderTableResponse.of(orderTable));

        // When & Then
        put(BASE_URL + String.format("/%d/empty", orderTable.getId()), OrderTableRequest.of(orderTable))
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTable.isEmpty()));
    }

    @DisplayName("주문 테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        // Given
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(OrderTableResponse.of(orderTable));

        // When & Then
        put(BASE_URL + String.format("/%d/number-of-guests", orderTable.getId()), OrderTableRequest.of(orderTable))
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTable.isEmpty()));
    }

}