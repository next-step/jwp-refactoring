package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static kitchenpos.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Product;

public class ProductAcceptanceUtils {

	public static final String PRODUCT_API_URL = "/api/products";

	public static Product 상품_등록_되어_있음(String name, BigDecimal price) {
		return 상품_등록_요청(name, price).as(Product.class);
	}

	public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
		return post(PRODUCT_API_URL, createRequest(name, price)).extract();
	}

	public static ExtractableResponse<Response> 상품_목록_조회_요청() {
		return get(PRODUCT_API_URL).extract();
	}

	public static void 상품_등록_됨(ExtractableResponse<Response> response, String expectedName, BigDecimal expectedPrice) {
		Product product = response.as(Product.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(product).isNotNull(),
			() -> assertThat(product.getName()).isEqualTo(expectedName),
			() -> assertThat(product.getPrice().intValue()).isEqualTo(expectedPrice.intValue())
		);
	}

	public static void 상품_목록_조회_됨(ExtractableResponse<Response> response, Product expectedProduct) {
		List<Product> products = response.as(new TypeRef<List<Product>>() {
		});
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.jsonPath().getList(".", Product.class)).isNotNull(),
			() -> assertThat(products)
				.first()
				.extracting(Product::getName)
				.isEqualTo(expectedProduct.getName())
		);
	}

	private static Product createRequest(String name, BigDecimal price) {
		return 상품(name, price);
	}
}
