package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import kitchenpos.utils.RestAssuredUtils;

@DisplayName("상품 관리")
class ProductAcceptanceTest extends AcceptanceTest {

	static final String REQUEST_PATH = "/api/products";

	Long 상품_아이디;

	/**
	 * Feature: 상품 관리 기능
	 * Background
	 *   When 가격이 0원 이상인 상품을 등록을 요청하면
	 *   Then 상품이 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		int 상품가격 = 1000;
		Product 상품 = 상품(상품가격);

		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(REQUEST_PATH, 상품);
		assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		상품_아이디 = 등록_응답.body().as(Product.class).getId();
	}

	/**
	 * Scenario: 상품 관리
	 * When 상품 목록을 조회하면
	 * Then 상품 목록이 조회된다.
	 */
	@Test
	void 상품_관리() {
		상품_목록_조회();
	}

	/**
	 * Scenario: 상품 등록 실패
	 * When 가격이 0원 미만인 상품을 등록 요청하면
	 * Then 상품 등록에 실패한다
	 */
	@Test
	void 상품_등록_실패() {
		int 상품가격 = -1;
		Product 상품 = 상품(상품가격);

		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(REQUEST_PATH, 상품);

		상품_등록_실패함(등록_응답);
	}

	private void 상품_목록_조회() {
		ExtractableResponse<Response> 목록_응답 = RestAssuredUtils.getAll(REQUEST_PATH);

		상품_목록_조회됨(목록_응답, 상품_아이디);
	}

	private void 상품_목록_조회됨(ExtractableResponse<Response> response, Long ...expectedProductId) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.body().as(new TypeRef<List<Product>>(){}))
			.extracting(Product::getId)
			.contains(expectedProductId);
	}

	private static void 상품_등록_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
	}

	private Product 상품(int price) {
		Product product = new Product();
		product.setName("나만의 스파게티");
		product.setPrice(BigDecimal.valueOf(price));
		return product;
	}
}
