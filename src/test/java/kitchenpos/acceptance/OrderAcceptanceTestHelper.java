package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

class OrderAcceptanceTestHelper {

    static ExtractableResponse<Response> createOrder(Order requestBody) {
        return AcceptanceTestHelper.post("/api/orders", requestBody);
    }

    static ExtractableResponse<Response> getOrders() {
        return AcceptanceTestHelper.get("/api/orders");
    }

    static ExtractableResponse<Response> changeOrderStatus(Long orderId, Order requestBody) {
        return AcceptanceTestHelper.put(String.format("/api/orders/%d/order-status", orderId), requestBody);
    }

    static OrderLineItem mapToOrderLineItem(Menu menu, int quantity) {
        return new OrderLineItem(menu.getId(), quantity);
    }

}
