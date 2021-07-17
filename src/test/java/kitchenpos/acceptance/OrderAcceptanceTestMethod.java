package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderRequest;

public class OrderAcceptanceTestMethod {
	public static ExtractableResponse<Response> createOrder(OrderRequest order) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(order)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> changeOrderStatus(Long orderId, OrderRequest status) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(status)
			.when().put("/api/orders/" + orderId + "/order-status")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findOrder() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/orders")
			.then().log().all()
			.extract();
	}
}
