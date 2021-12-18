package kitchenpos.product;

import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductDto;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 상품_등록_요청(ProductCreateRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_등록되어_있음(ProductCreateRequest request) {
		return 상품_등록_요청(request);
	}

	@DisplayName("상품을 등록한다.")
	@Test
	void register() {
		// when
		ExtractableResponse<Response> response = 상품_등록_요청(강정치킨_상품());

		// then
		상품_등록됨(response);
	}

	@DisplayName("상품 가격이 음수일 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnNegativePrice() {
		// when
		ExtractableResponse<Response> response = 상품_등록_요청(음수가격_상품());

		// then
		상품_등록되지_않음(response);
	}

	@DisplayName("상품 이름이 없는 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// when
		ExtractableResponse<Response> response = 상품_등록_요청(이름없는_상품());

		// then
		상품_등록되지_않음(response);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		ProductDto 강정치킨_상품 = 상품_등록되어_있음(강정치킨_상품()).as(ProductDto.class);
		ProductDto 양념치킨_상품 = 상품_등록되어_있음(양념치킨_상품()).as(ProductDto.class);

		// when
		ExtractableResponse<Response> response = 상품_목록_조회_요청();

		// then
		상품_목록_응답됨(response);
		상품_목록_포함됨(response, Arrays.asList(강정치킨_상품, 양념치킨_상품));
	}

	private void 상품_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(ProductDto.class).getId()).isNotNull();
	}

	private void 상품_등록되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ExtractableResponse<Response> 상품_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/products")
			.then().log().all()
			.extract();
	}

	private void 상품_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ProductDto> expectedProducts) {
		List<Long> actualIds = response.jsonPath().getList(".", ProductDto.class).stream()
			.map(ProductDto::getId)
			.collect(Collectors.toList());

		List<Long> expectedIds = expectedProducts.stream()
			.map(ProductDto::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).isEqualTo(expectedIds);
	}
}
