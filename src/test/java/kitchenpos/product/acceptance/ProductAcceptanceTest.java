package kitchenpos.product.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> product() {
        return Stream.of(
                dynamicTest("상품을 등록한다.", () -> {
                    // given
                    Product 정상_상품 = 상품_생성("짜장면", 9000L);
                    // when
                    ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_요청(정상_상품);
                    // then
                    상품_정상_생성됨(상품_생성_결과);
                }),
                dynamicTest("가격이 0미만인 상품을 등록할 수 없다.", () -> {
                    // given
                    Product 가격_0_미만_상품 = 상품_생성("탕수육", -1L);
                    // when
                    ExtractableResponse<Response> 가격_0_미만_상품_생성_결과 = 상품_생성_요청(가격_0_미만_상품);
                    // then
                    상품_생성_실패됨(가격_0_미만_상품_생성_결과);

                }),
                dynamicTest("이름이 없는 상품을 등록할 수 없다.", () -> {
                    // given
                    Product 이름없는_상품 = 상품_생성(null, 1000L);
                    // when
                    ExtractableResponse<Response> 이름없는_상품_생성_결과 = 상품_생성_요청(이름없는_상품);
                    // then
                    상품_생성_실패됨(이름없는_상품_생성_결과);
                }),
                dynamicTest("상품 목록을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> 상품_목록 = 상품_목록_조회_요청();
                    // then
                    상품_목록_정상_응답됨(상품_목록, "짜장면");
                })
        );
    }

    public static Product 상품_등록됨(String name, Long price){
        return 상품_생성_요청(상품_생성(name, price)).as(Product.class);
    }
    public static Product 상품_생성(String name, Long price){
        return new Product(null, name, BigDecimal.valueOf(price));
    }


    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    private void 상품_목록_정상_응답됨(ExtractableResponse<Response> response, String... 상품이름_목록) {
        List<String> 상품_이름_목록 = response.jsonPath()
                .getList(".", Product.class)
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(상품_이름_목록).containsAll(Arrays.asList(상품이름_목록))
        );
    }

}
