package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductRestController;

@DisplayName("상품 기능 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

	@Test
	@DisplayName("상품 생성 성공 테스트")
	public void createProductSuccessTest() {
		//given
		ProductRequest productRequest = new ProductRequest("로제치킨", new BigDecimal(18000));
		//when
		ExtractableResponse<Response> response = 상품_생성_요청(productRequest);

		//then
		상품_생성_성공(response);
	}

	@Test
	@DisplayName("상품 목록 조회 테스트")
	public void findAllProductListSuccessTest() {
		//given
		//when
		ExtractableResponse<Response> response = 상품_목록_조회_요청();

		//then
		상품_목록_조회_성공(response);
	}

	private void 상품_목록_조회_성공(ExtractableResponse<Response> response) {
		List<ProductResponse> productResponses = response.jsonPath().getList(".", ProductResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(productResponses).hasSizeGreaterThanOrEqualTo(6);
	}

	private ExtractableResponse<Response> 상품_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/products")
			.then().log().all()
			.extract();
	}

	private void 상품_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/products/7");
	}

	private ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(productRequest)
			.when().post("/api/products")
			.then().log().all()
			.extract();
	}
}
