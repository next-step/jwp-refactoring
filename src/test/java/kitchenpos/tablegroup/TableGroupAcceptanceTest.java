package kitchenpos.tablegroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.ordertable.OrderTableAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> tableParam = new HashMap<>();
        tableParam.put("id", orderTable1.getId());
        Map<String, Object> tableParam2 = new HashMap<>();
        tableParam2.put("id", orderTable2.getId());
        Map<String, Object> tableGroupParams = new HashMap<>();
        tableGroupParams.put("orderTables", Arrays.asList(tableParam, tableParam2));

        // When
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(tableGroupParams);
        // Then
        단체_지정_생성_완료(createResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);
        // Then
        단체_지정_삭제_완료(deleteResponse);
    }
}
