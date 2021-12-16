package kitchenpos.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.utils.AcceptanceTest;

import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블;
import static kitchenpos.utils.AcceptanceFixture.*;

public class OrderDomainFixture extends AcceptanceTest {

    public static Order 첫_주문 = new Order(한식_테이블);

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest OrderRequest) {
        return post("/api/orders", OrderRequest);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(OrderRequest OrderRequest, Object... pathParam) {
        return put("/api/orders/{orderId}/order-status", OrderRequest, pathParam);
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return get("/api/orders");
    }
}
