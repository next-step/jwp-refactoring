package kitchenpos.acceptance.product;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;

@DisplayName("상품 관련 기능 테스트")
public class ProductAcceptanceTest extends ProductAcceptance {

	@DisplayName("상품을 생성한다.")
	@Test
	void createProductTest() {
		// given
		ProductRequest request = ProductRequest.of("교촌치킨", 20000);

		// when
		ExtractableResponse<Response> response = 상품_등록_요청(request);

		// then
		상품_등록됨(response);
	}

	@DisplayName("0보다 작은 금액으로 상품을 생성한다.")
	@Test
	void createProductErrorTest() {
		// given
		ProductRequest request = ProductRequest.of("교촌치킨", -1000);

		// when
		ExtractableResponse<Response> response = 상품_등록_요청(request);

		// then
		상품_등록_실패됨(response);
	}

	@DisplayName("상품을 중복 생성한다[중복 생성 된다].")
	@Test
	void createDuplicateProductTest() {
		// given
		ProductRequest request = ProductRequest.of("교촌치킨", 20000);

		// when
		ExtractableResponse<Response> response1 = 상품_등록_요청(request);
		ExtractableResponse<Response> response2 = 상품_등록_요청(request);

		// then
		상품_등록됨(response1);
		상품_등록됨(response2);
	}

	@DisplayName("상품들을 조회한다.")
	@Test
	void getProductsTest() {
		// given
		ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음("네네치킨", 19000);
		ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음("도미노피자", 26000);

		// when
		ExtractableResponse<Response> response = 상품_조회_요청();

		// then
		상품_목록_조회됨(response);
		상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
	}
}
