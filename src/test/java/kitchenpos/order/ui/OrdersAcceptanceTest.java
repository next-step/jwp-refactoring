package kitchenpos.order.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.ui.MenuAcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.ui.OrderTableAcceptanceTest;
import kitchenpos.product.ui.ProductAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrdersAcceptanceTest extends AcceptanceTest {

	@DisplayName("주문정보를 관리한다")
	@Test
	void create() {
		//background
		//상품이 등록되어 있음
		Long productId1 = ProductAcceptanceTest.상품이_등록되어_있다("치즈돈까스", 6_500);
		Long productId2 = ProductAcceptanceTest.상품이_등록되어_있다("미니우동", 1_500);
		//메뉴가 등록되어 있음
		Long menuId = MenuAcceptanceTest
			  .메뉴가_등록되어_있음("치즈카츠 정식", "정식메뉴", 8_000, productId1, productId2);
		//주문테이블이 등록되어 있음
		Long orderTableId = OrderTableAcceptanceTest.주문_테이블이_등록되어_있음(5);

		OrderRequest orderRequest = new OrderRequest(orderTableId,
			  Arrays.asList(new OrderLineItemRequest(menuId, 1)));

		//주문정보 등록
		ExtractableResponse<Response> response = 주문정보_등록을_요청한다(orderRequest);
		주문정보가_등록됨(response);

		//주문상태변경
		ExtractableResponse<Response> changeStatusResponse = 주문의_상태_변경을_요청(response);
		주문의_상태가_변경됨(changeStatusResponse);

		//주문정보목록조회
		ExtractableResponse<Response> listResponse = 주문_목록을_조회한다();
		주문목록이_조회됨(response, listResponse);
	}

	private ExtractableResponse<Response> 주문정보_등록을_요청한다(
		  OrderRequest orderRequest) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(orderRequest)
			  .when().post("/api/orders")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 주문의_상태_변경을_요청(
		  ExtractableResponse<Response> response) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new OrderRequest(OrderStatus.MEAL.name()))
			  .when().put("/api/orders/" + response.jsonPath().getLong("id") + "/order-status")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 주문_목록을_조회한다() {
		ExtractableResponse<Response> listResponse = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/api/orders/")
			  .then().log().all()
			  .statusCode(HttpStatus.OK.value())
			  .extract();
		return listResponse;
	}

	private void 주문정보가_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();
	}

	private void 주문의_상태가_변경됨(ExtractableResponse<Response> changeStatusResponse) {
		assertThat(changeStatusResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeStatusResponse.jsonPath().getString("orderStatus")).isEqualTo(OrderStatus.MEAL.name());
	}

	private void 주문목록이_조회됨(ExtractableResponse<Response> response,
		  ExtractableResponse<Response> listResponse) {
		List<OrderResponse> orderList = listResponse.jsonPath().getList(".", OrderResponse.class);
		assertThat(orderList).contains(response.body().as(OrderResponse.class));
	}
}
