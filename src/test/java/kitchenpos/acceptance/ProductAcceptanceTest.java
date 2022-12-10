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
		Product 상품 = 상품("스파게티", 상품가격);

		ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청(상품);

		상품_아이디 = 상품_등록됨(상품_등록_응답);
	}

	/**
	 * Scenario: 상품 관리
	 * When 상품 목록을 조회하면
	 * Then 상품 목록이 조회된다.
	 */
	@Test
	void 상품_관리() {
		List<Product> 상품_목록 = 상품_목록_조회();
		상품_목록_조회됨(상품_목록, 상품_아이디);
	}

	/**
	 * Scenario: 상품 등록 실패
	 * When 가격이 0원 미만인 상품을 등록 요청하면
	 * Then 상품 등록에 실패한다
	 */
	@Test
	void 상품_등록_실패() {
		int 상품가격 = -1;
		Product 상품 = 상품("스파게티", 상품가격);

		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(REQUEST_PATH, 상품);

		상품_등록_실패함(등록_응답);
	}

	public static Long 상품_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response.body().as(Product.class).getId();
	}

	public static ExtractableResponse<Response> 상품_등록_요청(Product 상품) {
		return RestAssuredUtils.post(REQUEST_PATH, 상품);
	}

	private List<Product> 상품_목록_조회() {
		ExtractableResponse<Response> response = RestAssuredUtils.get(REQUEST_PATH);

		return response.body().as(new TypeRef<List<Product>>(){});
	}

	private void 상품_목록_조회됨(List<Product> products, Long expectedProductId) {
		assertThat(products)
			.extracting(Product::getId)
			.contains(expectedProductId);
	}

	private static void 상품_등록_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
	}

	public static Product 상품(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(BigDecimal.valueOf(price));
		return product;
	}
}
