package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.ToLongFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.domain.Product;
import kitchenpos.utils.RestAssuredUtils;

@DisplayName("상품 관리")
class ProductAcceptanceTest extends AcceptanceTest2<Product> {

	static final String REQUEST_PATH = "/api/products";
	int 상품가격 = 1000;
	String 상품명 = "스파게티";

	Long 상품_아이디;

	/**
	 * Feature: 상품 관리 기능
	 * Background
	 *   When 가격이 0원 이상인 상품을 등록을 요청하면
	 *   Then 상품이 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		ExtractableResponse<Response> 상품_등록_응답 = 등록_요청();

		상품_아이디 = 등록됨(상품_등록_응답).getId();
	}

	/**
	 * Scenario: 상품 관리
	 * When 상품 목록을 조회하면
	 * Then 상품 목록이 조회된다.
	 */
	@Test
	void 상품_관리() {
		List<Product> 상품_목록 = 목록_조회();
		목록_조회됨(상품_목록, 상품_아이디);
	}

	/**
	 * Scenario: 상품 등록 실패
	 * When 가격이 0원 미만인 상품을 등록 요청하면
	 * Then 상품 등록에 실패한다
	 */
	@Test
	void 상품_등록_실패() {
		int 상품가격 = -1;
		Product 상품 = 상품(상품명, 상품가격);

		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(상품);

		등록_실패함(등록_요청_응답);
	}

	@Deprecated
	public static Long 상품_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response.body().as(Product.class).getId();
	}

	@Deprecated
	public static ExtractableResponse<Response> 상품_등록_요청(Product 상품) {
		return RestAssuredUtils.post(REQUEST_PATH, 상품);
	}

	public static Product 상품(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(BigDecimal.valueOf(price));
		return product;
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected Product 도메인_생성() {
		return 상품(상품명, 상품가격);
	}

	@Override
	protected ToLongFunction<Product> idExtractor() {
		return Product::getId;
	}

	@Override
	protected Class<Product> getDomainClass() {
		return Product.class;
	}

}
