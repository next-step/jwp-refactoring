package kitchenpos.acceptance.product;

import static kitchenpos.acceptance.product.ProductFixture.상품;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.menu.ui.dto.ProductRequest;
import kitchenpos.menu.ui.dto.ProductResponse;

@DisplayName("상품목록 관리")
class ProductAcceptanceTest extends AcceptanceTest2 {

	ProductAcceptanceTestStep step = new ProductAcceptanceTestStep();

	Long 상품가격 = 1_000L;
	String 상품명 = "스파게티";
	Long 상품_아이디;

	/**
	 * Feature: 상품목록 관리 기능
	 * Background
	 * When 가격이 0원 이상인 상품을 등록을 요청하면
	 * Then 상품이 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		ProductRequest 상품 = 상품(상품명, 상품가격);
		ExtractableResponse<Response> 상품_등록_응답 = step.등록_요청(상품);

		상품_아이디 = step.등록됨(상품_등록_응답).getId();
	}

	/**
	 * Scenario: 상품목록 관리
	 * When 상품목록 목록을 조회하면
	 * Then 상품목록 목록이 조회된다.
	 */
	@Test
	void 상품_관리() {
		List<ProductResponse> 상품_목록 = step.목록_조회();
		step.목록_조회됨(상품_목록, 상품_아이디);
	}

	/**
	 * Scenario: 상품목록 등록 실패
	 * When 가격이 0원 미만인 상품을 등록 요청하면
	 * Then 상품목록 등록에 실패한다
	 */
	@Test
	void 상품_등록_실패() {
		ProductRequest 상품 = 상품(상품명, null);

		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(상품);

		step.등록_실패함(등록_요청_응답);
	}
}
