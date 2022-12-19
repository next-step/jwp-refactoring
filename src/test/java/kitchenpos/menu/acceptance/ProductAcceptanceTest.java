package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

	@DisplayName("상품 기능 통합 테스트")
	@TestFactory
	Stream<DynamicNode> product() {
		return Stream.of(
			dynamicTest("상품을 등록한다.", () -> {
				// given
				ProductRequest 정상_상품 = 상품_생성("짜장면", 9000);
				// when
				ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_요청(정상_상품);
				// then
				상품_정상_생성됨(상품_생성_결과);
			}),
			dynamicTest("가격이 0미만인 상품을 등록할 수 없다.", () -> {
				// given
				ProductRequest 가격_0_미만_상품 = 상품_생성("탕수육", -1);
				// when
				ExtractableResponse<Response> 가격_0_미만_상품_생성_결과 = 상품_생성_요청(가격_0_미만_상품);
				// then
				상품_생성_실패됨(가격_0_미만_상품_생성_결과);

			}),
			dynamicTest("이름이 없는 상품을 등록할 수 없다.", () -> {
				// given
				ProductRequest 이름없는_상품 = 상품_생성(null, 1000);
				// when
				ExtractableResponse<Response> 이름없는_상품_생성_결과 = 상품_생성_요청(이름없는_상품);
				// then
				상품_생성_실패됨(이름없는_상품_생성_결과);
			}),
			dynamicTest("상품 목록을 조회한다.", () -> {
				// given
				Long 햄버거_ID = 상품_등록됨("햄버거", 5_000);
				Long 감자튀김_ID = 상품_등록됨("감자튀김", 2_000);
				// when
				ExtractableResponse<Response> 상품_목록 = 상품_목록_조회_요청();
				// then
				상품_목록_정상_응답됨(상품_목록, 햄버거_ID, 감자튀김_ID);
			})
		);
	}

	public static Long 상품_등록됨(String name, int price) {
		return 상품_생성_요청(상품_생성(name, price)).as(ProductResponse.class).getId();
	}

	public static ProductRequest 상품_생성(String name, int price) {
		return ProductRequest.of(name, BigDecimal.valueOf(price));
	}

	public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(productRequest)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 상품_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/products")
			.then().log().all()
			.extract();
	}

	private void 상품_정상_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 상품_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 상품_목록_정상_응답됨(ExtractableResponse<Response> response, Long... 상품_목록) {
		List<Long> 상품_조회_목록 = response.jsonPath()
			.getList(".", ProductResponse.class)
			.stream()
			.map(ProductResponse::getId)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(상품_조회_목록).containsAll(Arrays.asList(상품_목록))
		);
	}

}
