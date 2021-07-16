package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

public class ProductAcceptanceTest extends AcceptanceTest {
	@DisplayName("상품 등록 및 조회 시나리오")
	@Test
	void createProductAndFindProductScenario() {
		// Scenario
		// When
		ExtractableResponse<Response> productCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Product("매운 라면", new BigDecimal(8000)))
			.when().post("/api/products")
			.then().log().all()
			.extract();
		Product createdProduct = productCreatedResponse.as(Product.class);

		// Then
		assertThat(productCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdProduct.getName()).isEqualTo("매운 라면");

		// When
		ExtractableResponse<Response> findProductResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/products")
			.then().log().all()
			.extract();

		String productName = findProductResponse.jsonPath().getList(".", Product.class).stream()
			.filter(product -> product.getId() == createdProduct.getId())
			.map(Product::getName)
			.findFirst()
			.get()
			;

		assertThat(productName).isEqualTo("매운 라면");
	}
}
