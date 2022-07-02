package kitchenpos.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

   private static final String PRODUCTS_PATH = "/api/products";

  /**
   * Feature: 상품 관련 기능
   *
   *   Scenario: 상품을 관리
   *     Given 요청할 상품을 생성하고
   *     When  상품 등록 요청하면
   *     Then  등록 된다.
   *     Given 요청할 상품을 가격은 0원미만으로 생성하고
   *     When  상품 등록 요청하면
   *     Then  등록 실패한다,
   *
   *     When  상품 목록요청 하면
   *     Then  목록이 조회된다.
   * */
  @DisplayName("상품을 관리한다.")
  @TestFactory
  Stream<DynamicTest> manageProduct() {
      return Stream.of(
              dynamicTest("상품을 등록한다.", () -> {
                  //given
                  Map<String, Object> params = 요청할_상품_생성("강정치킨", 17000);

                  //when
                  ExtractableResponse<Response> response = 상품_등록_요청(params);

                  //then
                  상품_등록됨(response);

                  //given
                  Map<String, Object> params2 = 요청할_상품_생성("초코치킨", -1000);

                  //when
                  ExtractableResponse<Response> response2 = 상품_등록_요청(params2);

                  //then
                  상품_등록_실패됨(response2);
              }),

              dynamicTest("상품 목록을 조회한다.", () -> {
                  //when
                  ExtractableResponse<Response> response = 상품_목록_조회_요청();

                  //then
                  상품_목록_조회됨(response, Collections.singletonList("강정치킨"));
              })
      );

  }

    public static ProductResponse 상품_등록_되어있음(String productName, int price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", productName);
        params.put("price", price);

        return 상품_등록_요청(params).as(ProductResponse.class);
    }

    private Map<String, Object> 요청할_상품_생성(String name, int price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        return params;
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(PRODUCTS_PATH)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 상품_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(PRODUCTS_PATH)
                .then().log().all()
                .extract();
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response, List<String> productNames) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name", String.class)).containsExactlyElementsOf(productNames);
    }

    private void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
