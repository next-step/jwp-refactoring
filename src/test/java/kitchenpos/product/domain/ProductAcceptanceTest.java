package kitchenpos.product.domain;

import static kitchenpos.product.domain.ProductAcceptanceStaticTest.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("인수테스트 : 상품 관련")
class ProductAcceptanceTest extends AcceptanceTest {

	@Test
	void 상품을_생성한다() {
		// given
		ProductRequest 후라이드_치킨_생성_요청값 = 상품_요청값_생성("후라이드", 16000);

		// when
		ExtractableResponse<Response> response = 상품_생성_요청(후라이드_치킨_생성_요청값);

		// then
		상품이_생성됨(response);
	}

	@Test
	void 상품의_가격이_0이하_일때_생성에_실패한다() {
		// given
		ProductRequest 가격이_0이하인_치킨_생성_요청값 = 상품_요청값_생성("후라이드", -1000);

		// when
		ExtractableResponse<Response> response = 상품_생성_요청(가격이_0이하인_치킨_생성_요청값);

		// then
		상품_생성_실패됨(response);
	}

	@Test
	void 상품의_가격이_없으면_생성에_실패한다() {
		// given
		ProductRequest 가격이_없는_치킨_생성_요청값 = 상품_요청값_생성("후라이드", null);

		// when
		ExtractableResponse<Response> response = 상품_생성_요청(가격이_없는_치킨_생성_요청값);

		// then
		상품_생성_실패됨(response);
	}

	@Test
	void 상품_목록을_조회한다() {
		// given
		ProductRequest 파닭_생성_요청값 = 상품_요청값_생성("파닭", 16000);
		ProductRequest 불닭_생성_요청값 = 상품_요청값_생성("불닭", 16000);

		ProductResponse 파닭 = 상품이_생성_되어있음(파닭_생성_요청값);
		ProductResponse 불닭 = 상품이_생성_되어있음(불닭_생성_요청값);

		// when
		ExtractableResponse<Response> response = 상품_목록_조회_요청();

		// then
		상품_목록_조회됨(response, Arrays.asList(파닭.getId(), 불닭.getId()));
	}
}
