package kitchenpos.tablegroup.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.ui.OrderTableAcceptanceTestSupport;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends TableGroupAcceptanceTestSupport {
    private OrderTableResponse orderTable1;
    private OrderTableResponse orderTable2;

    @BeforeEach
    public void beforeEach() {
        orderTable1 = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(1, true).as(OrderTableResponse.class);
        orderTable2 = OrderTableAcceptanceTestSupport.주문_테이블_등록_되어있음(2, true).as(OrderTableResponse.class);
    }

    @DisplayName("단체 지정의 생성 / 삭제")
    @Test
    void manageTableGroup() {
        // Given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(orderTable1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(orderTable2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

        // When
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(tableGroupRequest);

        // Then
        단체_지정_생성_완료(createResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);

        // Then
        단체_지정_삭제_완료(deleteResponse);
    }
}
