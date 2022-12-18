package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static kitchenpos.generator.OrderGenerator.*;
import static kitchenpos.generator.OrderLineItemGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderAcceptanceUtils {

	private static final String ORDERS_API_URL = "/api/orders";

	private OrderAcceptanceUtils() {
	}

	public static Order 주문_등록_되어_있음(OrderTable orderTable, Menu menu, int quantity) {
		return 주문_등록_요청(orderTable, menu, quantity).as(Order.class);
	}

	public static ExtractableResponse<Response> 주문_등록_요청(OrderTable orderTable, Menu menu, int quantity) {
		return post(ORDERS_API_URL, orderCreateRequest(orderTable, menu, quantity)).extract();
	}

	public static void 주문_등록_됨(ExtractableResponse<Response> response, int expectedQuantity, Menu expectedMenu) {
		Order order = response.as(Order.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(order.getId()).isNotNull(),
			() -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(order.getOrderedTime()).isEqualToIgnoringMinutes(LocalDateTime.now()),
			() -> assertThat(order.getOrderLineItems()).first()
				.satisfies(orderLineItem -> {
					assertThat(orderLineItem.getSeq()).isNotNull();
					assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenu.getId());
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
			() -> assertThat(response.jsonPath().getList(".", Order.class)).extracting(Order::getId)
				.containsExactly(expectedOrder.getId())
		);
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(long orderId, OrderStatus orderStatus) {
		return put(ORDERS_API_URL + "/" + orderId + "/order-status", orderStatusChangeRequest(orderStatus)).extract();
	}

	public static void 주문_상태_변경_됨(ExtractableResponse<Response> response, OrderStatus expectedOrderStatus) {
		Order order = response.as(Order.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(order.getOrderStatus()).isEqualTo(expectedOrderStatus.name())
		);
	}

	private static Order orderStatusChangeRequest(OrderStatus orderStatus) {
		return 주문(null, orderStatus, null);
	}

	private static Order orderCreateRequest(OrderTable orderTable, Menu menu, int quantity) {
		return 주문(orderTable.getId(), OrderStatus.COOKING,
			getOrderLineItems(menu, quantity));
	}

	private static List<OrderLineItem> getOrderLineItems(Menu menu, int quantity) {
		return Collections.singletonList(주문_품목(menu.getId(), quantity));
	}
}
