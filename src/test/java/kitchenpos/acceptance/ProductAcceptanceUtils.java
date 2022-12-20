package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.ui.request.ProductRequest;
import kitchenpos.menu.ui.response.ProductResponse;

public class ProductAcceptanceUtils {

	public static final String PRODUCT_API_URL = "/api/products";

	public static ProductResponse 상품_등록_되어_있음(String name, BigDecimal price) {
		return 상품_등록_요청(name, price).as(ProductResponse.class);
	}

	public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
		return post(PRODUCT_API_URL, createRequest(name, price)).extract();
	}

	public static ExtractableResponse<Response> 상품_목록_조회_요청() {
		return get(PRODUCT_API_URL).extract();
	}

	public static void 상품_등록_됨(ExtractableResponse<Response> response, String expectedName, BigDecimal expectedPrice) {
		ProductResponse product = response.as(ProductResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(product).isNotNull(),
			() -> assertThat(product.name()).isEqualTo(expectedName),
			() -> assertThat(product.price().intValue()).isEqualTo(expectedPrice.intValue())
		);
	}

	public static void 상품_목록_조회_됨(ExtractableResponse<Response> response, ProductResponse expectedProduct) {
		List<ProductResponse> products = response.as(new TypeRef<List<ProductResponse>>() {
		});
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.jsonPath().getList(".", Product.class)).isNotNull(),
			() -> assertThat(products)
				.first()
				.extracting(ProductResponse::name)
				.isEqualTo(expectedProduct.name())
		);
	}

	private static ProductRequest createRequest(String name, BigDecimal price) {
		return new ProductRequest(name, price);
	}
}
