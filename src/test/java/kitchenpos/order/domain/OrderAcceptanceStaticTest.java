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
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;

public class OrderAcceptanceStaticTest {

	public static final String ORDER_PATH = "/api/orders";
	public static final String SLASH = "/";
	public static final String ORDER_STATUS = "/order-status";

	public static OrderResponse 주문_상태가_변경_되어_있음(Long id, OrderStatus orderStatus) {
		return 주문_상태_변경_요청(id, OrderStatusRequest.from(orderStatus)).as(OrderResponse.class);
	}

	public static void 주문_상태_변경에_실패함(ExtractableResponse<Response> response, int status) {
		assertThat(response.statusCode()).isEqualTo(status);
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id, OrderStatusRequest params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.put(ORDER_PATH + SLASH + id + ORDER_STATUS)
			.then().log().all()
			.extract();
	}

	public static void 주문_상태가_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static OrderResponse 주문이_생성_되어_있음(OrderRequest params) {
		return 주문_요청(params).as(OrderResponse.class);
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
			.jsonPath().getList(".", OrderResponse.class)
			.stream()
			.map(OrderResponse::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static void 주문_생성에_실패함(ExtractableResponse<Response> response, int status) {
		assertThat(response.statusCode()).isEqualTo(status);
	}

	public static List<OrderLineItemRequest> 주문_메뉴_생성(Long menuId, Long quantity) {
		return Collections.singletonList(OrderLineItemRequest.of(menuId, quantity));
	}

	public static OrderRequest 주문_요청값_생성(Long id, List<OrderLineItemRequest> orderLineItems) {
		return OrderRequest.of(id, orderLineItems);
	}

	public static ExtractableResponse<Response> 주문_요청(OrderRequest params) {
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
