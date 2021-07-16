package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTestMethod {
	public static ExtractableResponse<Response> createTableGroup(TableGroup tableGroup) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tableGroup)
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> ungroupTableGroup(Long id) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/table-groups/" + id)
			.then().log().all()
			.extract();
	}
}
