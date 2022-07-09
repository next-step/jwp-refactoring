package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.CreateProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {
    private static final String PRODUCT_URL = "/api/products";
    public static final CreateProductRequest 불고기버거 = new CreateProductRequest("불고기버거", 1500);
    public static final CreateProductRequest 새우버거 = new CreateProductRequest("새우버거", 2000);

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * when 제품을 추가 한다.
     * then 제품이 추가됨.
     * when 메뉴를 조회 한다.
     * then 추가된 제품이 조회됨.
     *
     */
    @Test
    @DisplayName("제품 관리 테스트")
    void product() {
        // when
        final ExtractableResponse<Response> 제품_추가_요청 = 제품_추가_요청(불고기버거);
        // then
        final ProductResponse 제품_추가_됨 = 제품_추가_됨(제품_추가_요청);

        // when
        final ExtractableResponse<Response> 전체_제품_조회 = 제품_전체_조회();
        // then
        final List<ProductResponse> products = 제품_조회_됨(전체_제품_조회);

        assertThat(products).hasSize(1);
    }

    public static ExtractableResponse<Response> 제품_추가_요청(final CreateProductRequest product) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post(PRODUCT_URL)
                .then().log().all()
                .extract();
    }

    public static ProductResponse 제품_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(ProductResponse.class);
    }

    public static ProductResponse 제품_추가_되어_있음(final CreateProductRequest product) {
        return 제품_추가_됨(제품_추가_요청(product));
    }

    public static ExtractableResponse<Response> 제품_전체_조회() {
        return RestAssured.given()
                .when().get(PRODUCT_URL)
                .then()
                .extract();
    }

    public static List<ProductResponse> 제품_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", ProductResponse.class);
    }
}
