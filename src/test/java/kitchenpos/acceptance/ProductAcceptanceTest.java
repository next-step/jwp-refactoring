package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("제품 관리")
public class ProductAcceptanceTest extends AcceptanceTest {
    @DisplayName("제품을 관리한다")
    @Test
    void manage() {
        //when
        Product request = createRequest();
        ExtractableResponse<Response> createdResponse = 생성_요청(request);
        //then
        생성됨(createdResponse, request);
        //when
        ExtractableResponse<Response> selectedResponse = 조회_요청();
        //then
        조회됨(selectedResponse);
    }

    public static Product createRequest() {
        Product request = new Product();
        request.setName("강정치킨");
        request.setPrice(new BigDecimal(17_000));

        return request;
    }

    public static ExtractableResponse<Response> 생성_요청(Product request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, Product request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Product product = response.as(Product.class);
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getPrice().intValue()).isEqualTo(request.getPrice().intValue());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Product> menuGroups = Arrays.asList(response.as(Product[].class));
        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
