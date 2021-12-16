package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuResponse;

public class OrderAcceptanceStaticTest {

	public static final String ORDER_PATH = "/api/orders";
	public static final String SLASH = "/";
	public static final String ORDER_STATUS = "/order-status";

	public static Order 주문_상태가_변경_되어_있음(Order order, String orderStatus) {
		order.setOrderStatus(orderStatus);
		return 주문_상태_변경_요청(order).as(Order.class);
	}

	public static void 주문_상태_변경에_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(Order params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.put(ORDER_PATH + SLASH + params.getId() + ORDER_STATUS)
			.then().log().all()
			.extract();
	}

	public static void 주문_상태가_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static Order 주문이_생성_되어_있음(Order params) {
		return 주문_요청(params).as(Order.class);
	}

	public static ExtractableResponse<Response> 주문_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when()
			.get(ORDER_PATH)
			.then().log().all()
			.extract();
	}

	public static void 주문_목록이_조회됨(ExtractableResponse<Response> response, List<Long> idList) {
		List<Long> responseIdList = response.body()
			.jsonPath().getList(".", Order.class)
			.stream()
			.map(Order::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static void 주문_생성에_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static List<OrderLineItem> 주문_메뉴_생성(MenuResponse menu, int quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(quantity);
		return Collections.singletonList(orderLineItem);
	}

	public static Order 주문_요청값_생성(Long id, List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setOrderTableId(id);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static ExtractableResponse<Response> 주문_요청(Order params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post(ORDER_PATH)
			.then().log().all()
			.extract();
	}

	public static void 주문_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
