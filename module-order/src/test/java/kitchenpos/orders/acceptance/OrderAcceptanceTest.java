package kitchenpos.orders.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.dto.OrderLineItemRequest;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;

@DisplayName("주문 기능 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

	private static OrderRequest changeOrderStatus = new OrderRequest("COMPLETION");

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		OrderRequest orderRequest = new OrderRequest(7L, Lists.newArrayList(orderLineItemRequest));

		//when
		ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

		//then
		주문_생성_성공(response);
	}

	@Test
	@DisplayName("주문에 메뉴가 정해지지 않아서 생성 실패")
	public void createOrderFailOrderLineItemIsEmptyTest() {
		//given
		OrderRequest orderRequest = new OrderRequest(1L, Lists.emptyList());

		//when
		ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

		//then
		주문_관련_요청_실패(response, "주문할 메뉴를 골라주세요");
	}

	@Test
	@DisplayName("주문한 테이블이 없어서 생성 실패")
	public void createOrderFailNotExistedTableTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		OrderRequest orderRequest = new OrderRequest(99L, Lists.newArrayList(orderLineItemRequest));

		//when
		ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

		//then
		주문_관련_요청_실패(response, "테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderTest() {
		//given
		//when
		ExtractableResponse<Response> response = 주문_목록_조회_요청();

		//then
		주문_목록_조회_성공(response);
	}

	@Test
	@DisplayName("주문 상태 변경(식사중 -> 계산완료) 테스트")
	public void changeOrderStatusTest() {
		//given
		//when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrderStatus, 1L);

		//then
		주문_상태_변경_성공(response, OrderStatus.COMPLETION.name());
	}

	@Test
	@DisplayName("없는 주문에 상태변경 요청해서 실패")
	public void changeOrderStatusFailNotExistedOrderTest() {
		//given
		//when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrderStatus, 99L);

		//then
		주문_관련_요청_실패(response, "주문이 존재하지 않습니다");
	}

	@Test
	@DisplayName("계산완료 된 주문이라서 상태변경 실패")
	public void changeOrderStatusFailOrderIsCompletionTest() {
		//given
		//when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrderStatus, 5L);

		//then
		주문_관련_요청_실패(response, "계산완료 된 주문은 상태를 변경 할 수 없습니다");
	}

	private void 주문_상태_변경_성공(ExtractableResponse<Response> response, String orderStatus) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(orderStatus);
	}

	private ExtractableResponse<Response> 주문_상태_변경_요청(OrderRequest orderRequest, long orderId) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderRequest)
			.when().put("/api/orders/{orderId}/order-status", orderId)
			.then().log().all()
			.extract();
	}

	private void 주문_관련_요청_실패(ExtractableResponse<Response> response, String message) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.body().asString()).isEqualTo(message);
	}

	private void 주문_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/orders/8");
	}

	private ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderRequest)
			.when().post("/api/orders/")
			.then().log().all()
			.extract();
	}

	private void 주문_목록_조회_성공(ExtractableResponse<Response> response) {
		List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(orderResponses).hasSizeGreaterThanOrEqualTo(7);
	}

	private ExtractableResponse<Response> 주문_목록_조회_요청() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/orders/")
			.then().log().all()
			.extract();
	}
}
