package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;

public class ProductAcceptanceTest extends AcceptanceTest {
	@DisplayName("상품 등록 및 조회 시나리오")
	@Test
	void createProductAndFindProductScenario() {
		// Scenario
		// When
		ExtractableResponse<Response> productCreatedResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(8000)));
		ProductRequest createdProduct = productCreatedResponse.as(ProductRequest.class);
		// Then
		assertThat(productCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdProduct.getName()).isEqualTo("매운 라면");

		// When
		ExtractableResponse<Response> findProductResponse = findProduct();
		// Then
		String productName = findProductResponse.jsonPath().getList(".", ProductRequest.class).stream()
			.filter(product -> product.getId() == createdProduct.getId())
			.map(ProductRequest::getName)
			.findFirst()
			.get();
		assertThat(productName).isEqualTo("매운 라면");
	}

	@DisplayName("상품 오류 시나리오")
	@Test
	void productErrorScenario() {
		// Scenario
		// When
		ExtractableResponse<Response> productWithMunusPriceResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(-1000)));
		// Then
		assertThat(productWithMunusPriceResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
