package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import kitchenpos.menu.MenuFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

  @DisplayName("상품을 생성한다.")
  @Test
  void createProduct() {
    // given
    Product 치킨 = MenuFactory.ofProduct("치킨", 35000);

    // when
    ExtractableResponse<Response> response = 상품_생성_요청(치킨);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  @DisplayName("상품을 목록을 조회한다.")
  @Test
  void findAllProduct() {
    // when
    ExtractableResponse<Response> response = 상품_목록_조회됨();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  private ExtractableResponse<Response> 상품_생성_요청(Product product) {
    return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when()
            .post("/api/products")
            .then().log().all()
            .extract();
  }

  private ExtractableResponse<Response> 상품_목록_조회됨() {
    return RestAssured
            .given().log().all()
            .when()
            .get("/api/products")
            .then().log().all()
            .extract();
  }
}
