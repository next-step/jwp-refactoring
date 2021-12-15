package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductAcceptanceStaticTest {

	public static final String PRODUCTS_PATH = "/api/products";

	public static void 상품_목록_조회됨(ExtractableResponse<Response> response, List<Long> idList) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		List<Long> responseIdList = response.body()
			.jsonPath().getList(".", ProductResponse.class)
			.stream()
			.map(ProductResponse::getId)
			.collect(Collectors.toList());

		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static ExtractableResponse<Response> 상품_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when().get(PRODUCTS_PATH)
			.then().log().all()
			.extract();
	}

	public static ProductResponse 상품이_생성_되어있음(ProductRequest params) {
		return 상품_생성_요청(params).as(ProductResponse.class);
	}

	public static void 상품_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static void 상품이_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post(PRODUCTS_PATH)
			.then().log().all()
			.extract();
	}

	public static ProductRequest 상품_요청값_생성(String name, Integer price) {
		return ProductRequest.of(name, price);
	}
}
