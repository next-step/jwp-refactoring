package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.ProductRequest;

public class ProductAcceptanceTest extends AcceptanceTest {
	@DisplayName("상품 생성 요청")
	@Test
	void create() {
		ExtractableResponse<Response> response = 상품_생성_요청("후라이드", 16000);

		상품_생성_성공(response);
	}

	public static ExtractableResponse<Response> 상품_생성_요청(String name, int price) {
		ProductRequest productRequest = new ProductRequest(name, price);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(productRequest)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}

	public static void 상품_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
