package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductAcceptanceUtils.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Product;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {

	/**
	 * given 상품 이름과 가격을 정하고
	 * when 상품 등록을 요청하면
	 * then 상품이 등록된다
	 */
	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void createProductTest() {
		// given
		String name = "후라이드";
		BigDecimal price = BigDecimal.valueOf(16000);

		// when
		ExtractableResponse<Response> 상품_등록_요청 = 상품_등록_요청(name, price);

		// then
		상품_등록_됨(상품_등록_요청, name, price);
	}

	/**
	 * given 상품이 등록되어 있고
	 * when 상품 목록을 조회하면
	 * then 상품 목록이 조회된다
	 */
	@DisplayName("상품들을 조회할 수 있다.")
	@Test
	void listProductTest() {
		// given
		String name = "후라이드";
		BigDecimal price = BigDecimal.valueOf(16000);
		Product 후라이드 = 상품_등록_되어_있음(name, price);

		// when
		ExtractableResponse<Response> 상품_목록_조회_요청 = 상품_목록_조회_요청();

		// then
		상품_목록_조회_됨(상품_목록_조회_요청, 후라이드);
	}
}
