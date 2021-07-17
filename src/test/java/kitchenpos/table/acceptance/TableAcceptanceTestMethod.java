package kitchenpos.table.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.OrderTableRequest;

public class TableAcceptanceTestMethod {
	public static ExtractableResponse<Response> createOrderTable(OrderTableRequest orderTable) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when().post("/api/tables/")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> changeNumber(Long id, OrderTableRequest orderTable) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when().put("/api/tables/" + id + "/number-of-guests")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> changeEmpty(Long id, OrderTableRequest orderTable) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when().put("/api/tables/" + id + "/empty")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findOrderTable() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/tables")
			.then().log().all()
			.extract();
	}
}
