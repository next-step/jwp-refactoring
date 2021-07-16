package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTestMethod {
	public static ExtractableResponse<Response> createOrderTable(OrderTable orderTable) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when().post("/api/tables/")
			.then().log().all()
			.extract();
	}
}
