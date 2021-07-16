package kitchenpos.acceptance;

import java.math.BigDecimal;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

public class ProductAcceptanceTestMethod {
	public static ExtractableResponse<Response> createProduct(Product product) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}
}
