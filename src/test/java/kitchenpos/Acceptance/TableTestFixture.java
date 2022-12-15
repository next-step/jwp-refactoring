package kitchenpos.Acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.TestFixture;
import kitchenpos.domain.OrderTable;

public class TableTestFixture extends TestFixture {

    public static final String ORDER_TABLE_BASE_URI = "/api/tables";

    public static ExtractableResponse<Response> 주문_테이블_생성_요청함(OrderTable orderTable) {
        return post(ORDER_TABLE_BASE_URI, orderTable);
    }
}
