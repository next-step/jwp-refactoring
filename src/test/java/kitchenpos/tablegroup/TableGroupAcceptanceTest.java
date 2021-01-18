package kitchenpos.tablegroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.OrderTableAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends TableGroupAcceptanceTestSupport {
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void beforeEach() {
        orderTable1 = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(1, true).as(OrderTable.class);
        orderTable2 = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(2, true).as(OrderTable.class);
    }

    @DisplayName("단체 지정의 생성 / 삭제")
    @Test
    void manageTableGroup() {
        // Given
        OrderTable param1 = new OrderTable();
        param1.setId(orderTable1.getId());
        param1.setNumberOfGuests(3);
        param1.setEmpty(false);
        OrderTable param2 = new OrderTable();
        param2.setId(orderTable2.getId());
        param2.setNumberOfGuests(5);
        param2.setEmpty(false);
        TableGroup params = new TableGroup();
        params.setOrderTables(Arrays.asList(param1, param2));

        // When
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(params);

        // Then
        단체_지정_생성_완료(createResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);

        // Then
        단체_지정_삭제_완료(deleteResponse);
    }
}
