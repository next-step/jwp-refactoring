package kitchenpos.order.acceptance;

import static kitchenpos.TestInstances.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.menu.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.menu.acceptance.ProductAcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;

public class OrderAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 생성 요청")
	@Test
	void create() {
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);
		OrderTableResponse orderTableResponse = TableAcceptanceTest.테이블_생성_요청(2, false).as(OrderTableResponse.class);

		ExtractableResponse<Response> response = 주문_생성_요청(orderTableResponse.getId(),
			Arrays.asList(orderLineItemRequest));

		주문_생성_성공(response);
		주문_생성_검증(response.as(OrderResponse.class));
	}

	@DisplayName("빈 테이블 주문 생성 요청시 실패")
	@Test
	void createWhenEmptyTable() {
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);
		OrderTableResponse orderTableResponse = TableAcceptanceTest.테이블_생성_요청(2, true).as(OrderTableResponse.class);

		ExtractableResponse<Response> response = 주문_생성_요청(orderTableResponse.getId(),
			Arrays.asList(orderLineItemRequest));

		주문_생성_실패(response);
	}

	@DisplayName("주문 조회 요청")
	@Test
	void list() {
		ExtractableResponse<Response> response = 주문_조회_요청();

		주문_조회_성공(response);
	}

	@DisplayName("주문 상태 변경 요청")
	@Test
	void changeOrderStatus() {

		ProductResponse productResponse = ProductAcceptanceTest.상품_생성_요청("후라이드", 16000).as(ProductResponse.class);
		MenuGroupResponse menuGroupResponse = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("한마리메뉴").as(MenuGroupResponse.class);
		MenuRequest menuRequest = new MenuRequest("후라이드", 12000, menuGroupResponse.getId(),
			Arrays.asList(new MenuProductRequest(productResponse.getId(), 1)));

		MenuResponse menuResponse = MenuAcceptanceTest.메뉴_생성_요청(menuRequest).as(MenuResponse.class);

		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getId(), 2);
		OrderTableResponse orderTableResponse = TableAcceptanceTest.테이블_생성_요청(2, false).as(OrderTableResponse.class);
		OrderResponse orderResponse = 주문_생성_요청(orderTableResponse.getId(),
			Arrays.asList(orderLineItemRequest)).as(OrderResponse.class);

		ExtractableResponse<Response> response = 주문_상태_변경_요청(orderResponse.getId(), OrderStatus.MEAL);

		주문_상태_변경_성공(response);
	}

	public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId,
		List<OrderLineItemRequest> orderLineItemRequests) {
		OrderRequest orderRequest = new OrderRequest(orderTableId, orderLineItemRequests);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderRequest)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
	}

	public static void 주문_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 주문_생성_검증(OrderResponse orderResponse) {
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(orderResponse.getOrderLineItems()).map(OrderLineItemResponse::getOrderId)
			.allMatch(aLong -> aLong.equals(orderResponse.getId()));
	}

	public static void 주문_생성_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static ExtractableResponse<Response> 주문_조회_요청() {

		return RestAssured
			.given().log().all()
			.when().get("/api/orders")
			.then().log().all()
			.extract();
	}

	public static void 주문_조회_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderTableId, OrderStatus orderStatus) {
		OrderRequest orderRequest = new OrderRequest(orderStatus);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderRequest)
			.when().put("/api/orders/{orderId}/order-status", orderTableId)
			.then().log().all()
			.extract();
	}

	public static void 주문_상태_변경_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
