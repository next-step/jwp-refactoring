package kitchenpos.acceptance.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.util.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

public class OrderAcceptance extends AcceptanceTest {

	public static final String ORDER_REQUEST_URL = "/api/orders";

	public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest request) {
		return RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(ORDER_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 메뉴_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get(ORDER_REQUEST_URL)
			.then().log().all().extract();
	}

	public static void 주문_상태_변경됨(OrderResponse order, Map<String, String> param,
		ExtractableResponse<Response> response) {
		OrderResponse orderResponse = response.as(OrderResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(orderResponse.getId()).isEqualTo(order.getId()),
			() -> assertThat(orderResponse.getOrderStatus()).isEqualTo(param.get("orderStatus"))
		);
	}

	public static void 주문_등록됨(OrderRequest request, ExtractableResponse<Response> response) {
		OrderResponse orderResponse = response.as(OrderResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(orderResponse.getId()).isNotNull(),
			() -> assertThat(orderResponse.getOrderTableId()).isEqualTo(request.getOrderTableId()),
			() -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(orderResponse.getOrderLineItems()).hasSize(request.getOrderLineItems().size())
		);
	}

	public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<OrderResponse> expected) {
		List<Long> expectedIds = expected.stream()
			.map(OrderResponse::getId)
			.collect(Collectors.toList());

		List<Long> responseIds = response.jsonPath().getList(".", OrderResponse.class).stream()
			.map(OrderResponse::getId)
			.collect(Collectors.toList());

		assertThat(responseIds).containsAll(expectedIds);
	}

	public static void 주문_상태_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static ExtractableResponse<Response> 주문_상태변경_요청(OrderResponse order, Map<String, String> param) {
		return RestAssured
			.given().log().all()
			.body(param)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(ORDER_REQUEST_URL + "/{orderId}/order-status", order.getId())
			.then().log().all().extract();
	}
}
