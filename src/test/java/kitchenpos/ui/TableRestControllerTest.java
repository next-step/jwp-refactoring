package kitchenpos.ui;

import static kitchenpos.application.TableServiceTest.두명;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 테이블 API")
@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends RestControllerTest<OrderTable> {

    private static final String BASE_URL = "/api/tables";

    @MockBean
    private TableService tableService;

    private OrderTable orderTable = new OrderTable(1L, 두명);

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(tableService.create(any())).willReturn(orderTable);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(orderTable);
        post(BASE_URL, orderTable)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<OrderTable> orderTables = Collections.singletonList(orderTable);
        given(tableService.list()).willReturn(orderTables);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(orderTables);
        get(BASE_URL)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        // Given
        given(tableService.changeEmpty(any(), any())).willReturn(orderTable);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(orderTable);
        put(BASE_URL + String.format("/%d/empty", orderTable.getId()), orderTable)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("주문 테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        // Given
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(orderTable);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(orderTable);
        put(BASE_URL + String.format("/%d/number-of-guests", orderTable.getId()), orderTable)
            .andExpect(content().string(responseBody));
    }

}