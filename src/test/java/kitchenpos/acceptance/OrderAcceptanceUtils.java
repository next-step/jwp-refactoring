package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;

public class OrderAcceptanceUtils {

	private static final String ORDERS_API_URL = "/api/orders";

	private OrderAcceptanceUtils() {
	}

	public static Order 주문_등록_되어_있음(long tableId, long menuId, int quantity) {
		return 주문_등록_요청(tableId, menuId, quantity).as(Order.class);
	}

	public static ExtractableResponse<Response> 주문_등록_요청(long tableId, long menuId, int quantity) {
		return post(ORDERS_API_URL, orderCreateRequest(tableId, menuId, quantity)).extract();
	}

	public static void 주문_등록_됨(ExtractableResponse<Response> response, int expectedQuantity,
		MenuResponse expectedMenu) {
		OrderResponse order = response.as(OrderResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(order.getOrderedTime()).isEqualToIgnoringMinutes(LocalDateTime.now()),
			() -> assertThat(order.getOrderLineItems()).first()
				.satisfies(orderLineItem -> {
					assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenu.id());
					assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity);
				})
		);
	}

	public static ExtractableResponse<Response> 주문_목록_조회_요청() {
		return get(ORDERS_API_URL).extract();
	}

	public static void 주문_목록_조회_됨(ExtractableResponse<Response> response, Order expectedOrder) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.jsonPath().getList(".", OrderResponse.class))
				.extracting(OrderResponse::getId)
				.containsExactly(expectedOrder.getId())
		);
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(long orderId, OrderStatus orderStatus) {
		return put(ORDERS_API_URL + "/" + orderId + "/order-status", orderStatusChangeRequest(orderStatus)).extract();
	}

	public static void 주문_상태_변경_됨(ExtractableResponse<Response> response, OrderStatus expectedOrderStatus) {
		OrderResponse order = response.as(OrderResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(order.getOrderStatus()).isEqualTo(expectedOrderStatus.name())
		);
	}

	private static OrderStatusRequest orderStatusChangeRequest(OrderStatus orderStatus) {
		return new OrderStatusRequest(orderStatus.name());
	}

	private static OrderRequest orderCreateRequest(long tableId, long menuId, int quantity) {
		return new OrderRequest(tableId, Collections.singletonList(orderLineItem(menuId, quantity)));
	}

	private static OrderLineItemRequest orderLineItem(long menuId, int quantity) {
		return new OrderLineItemRequest(menuId, quantity);
	}
}
