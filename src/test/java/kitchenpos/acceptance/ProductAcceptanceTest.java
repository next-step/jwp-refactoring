package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.응답코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends BaseAcceptanceTest{


    @DisplayName("상품을 관리한다")
    @Test
    public void manageProduct() {
        //상품 생성
        //given
        Product 상품 = new Product("상품", 1000);
        //when
        ExtractableResponse<Response> 상품_생성_요청 = 상품_생성_요청(상품);
        //then
        응답코드_확인(상품_생성_요청, HttpStatus.CREATED);

        //상품 조회
        //when
        ExtractableResponse<Response> 상품_목록_조회_요청 = 상품_목록_조회_요청();
        //then
        응답코드_확인(상품_목록_조회_요청, HttpStatus.OK);
        상품_조회됨(상품_목록_조회_요청, 상품_생성_요청.as(Product.class).getId());

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

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_조회됨(final ExtractableResponse<Response> response, Long id) {
        assertThat(response.jsonPath().getList(".", Product.class).stream()
            .anyMatch(product -> product.getId().equals(id))).isTrue();
    }


}
