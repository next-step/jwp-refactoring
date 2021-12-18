package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

	@DisplayName("상품을 등록한다")
	@Test
	void createTest() {
		Product product = new Product(null, "상품1", new BigDecimal(1000));
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(product)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/api/products")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

}
