package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;

public class ProductAcceptanceTestMethod {
	public static ExtractableResponse<Response> createProduct(ProductRequest product) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findProduct() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/products")
			.then().log().all()
			.extract();
	}
}
