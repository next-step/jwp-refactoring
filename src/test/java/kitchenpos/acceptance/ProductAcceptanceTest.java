package kitchenpos.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.*;

@DisplayName("상품 관련 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

   private static final String PRODUCTS_PATH = "/api/products";

  /**
   * Feature: 상품을 관련 기능
   *
   *   Scenario: 상품을 관리
   *     Given 상품명과 가격으로
   *     When  상품 등록 요청하면
   *     Then  등록 된다.
   *     Given 상품명과 가격은 0원이하로
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
              dynamicTest("상품을 등록한다.(가격이 0원이하면 실패한다.)", () -> {
                  //given
                  Map<String, Object> params = new HashMap<>();
                  params.put("name", "강정치킨");
                  params.put("price", 17000);

                  //when
                  ExtractableResponse<Response> response = 상품_등록_요청(params);

                  //then
                  상품_등록됨(response);

                  //given
                  Map<String, Object> params2 = new HashMap<>();
                  params.put("name", "강정치킨2");
                  params.put("price", 0);

                  //when
                  ExtractableResponse<Response> response2 = 상품_등록_요청(params2);

                  //then
                  상품_등록_실패됨(response2);
              }),

              dynamicTest("상품 목록을 조회한다.", () -> {
                  //when
                  ExtractableResponse<Response> response = 상품_목록_조회_요청();

                  //then
                  상품_목록_조회됨(response);
              })
      );

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

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> names = response.body().jsonPath().getList("name", String.class);
        assertThat(names).hasSize(1);
        assertThat(names).contains("강정치킨");
    }

    private void 상품_등록_실패됨(ExtractableResponse<Response> response2) {
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }



}
