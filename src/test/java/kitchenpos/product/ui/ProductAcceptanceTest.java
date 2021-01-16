package kitchenpos.product.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends AcceptanceTest {

	@DisplayName("상품을 관리한다.")
	@Test
	void create() {
		ProductRequest request = new ProductRequest("상품", new BigDecimal(0));

		//when
		ExtractableResponse<Response> response = 상품등록을_요청한다(request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();

		//when
		ExtractableResponse<Response> listResponse = 상품_목록을_조회한다();
		//then
		assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(listResponse.jsonPath().getList(".", ProductResponse.class)).isNotEmpty();
	}

	@DisplayName("금액이 0원미만 상품은 등록할 수 없다.")
	@Test
	void createWithUnderZeroPrice() {
		ProductRequest request = new ProductRequest("상품", new BigDecimal(-1));

		//when
		ExtractableResponse<Response> response = 상품등록을_요청한다(request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static Long 상품이_등록되어_있다(String name, int price) {
		ExtractableResponse<Response> response = 상품등록을_요청한다(
			  new ProductRequest(name, new BigDecimal(price)));
		return response.jsonPath().getLong("id");
	}

	public static ExtractableResponse<Response> 상품등록을_요청한다(ProductRequest request) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(request)
			  .when().post("/api/products2")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 상품_목록을_조회한다() {
		ExtractableResponse<Response> listResponse = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/api/products2")
			  .then().log().all()
			  .extract();
		return listResponse;
	}
}
