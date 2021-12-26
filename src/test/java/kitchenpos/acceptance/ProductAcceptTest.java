package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.ProductAcceptStep.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;

@DisplayName("상품 인수테스트")
public class ProductAcceptTest extends AcceptanceTest {

	@DisplayName("상품을 관리한다")
	@Test
	void 상품을_관리한다() {
		// given
		Product 등록_요청_상품 = new Product();
		등록_요청_상품.setName("후라이드 치킨");
		등록_요청_상품.setPrice(BigDecimal.valueOf(15_000));

		// when
		ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청(등록_요청_상품);

		//then
		Product 등록된_상품 = 상품_등록_확인(상품_등록_응답, 등록_요청_상품);

		// when
		ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();

		// then
		상품_목록_조회_확인(상품_목록_조회_응답, 등록된_상품);
	}

}
