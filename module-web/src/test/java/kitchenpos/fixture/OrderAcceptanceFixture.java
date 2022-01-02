package kitchenpos.fixture;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.OrderTableAcceptanceFixture.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;

public class OrderAcceptanceFixture extends AcceptanceTest {

    public static Order 첫_주문 = new Order(테이블_6인);

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
         return post("/api/orders", orderRequest);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(OrderRequest OrderRequest, Object... pathParam) {
        return put("/api/orders/{orderId}/order-status", OrderRequest, pathParam);
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return get("/api/orders");
    }
}
