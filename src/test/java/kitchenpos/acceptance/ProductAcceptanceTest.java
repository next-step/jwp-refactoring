package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest 불고기;
    private ProductRequest 김치;
    private ProductResponse 생성된_불고기;
    private ProductResponse 생성된_김치;

    @BeforeEach
    public void setUp() {
        super.setUp();
        불고기 = ProductRequest.of("불고기", BigDecimal.valueOf(8_000));
        김치 = ProductRequest.of("김치", BigDecimal.valueOf(2_000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(불고기);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // given
        생성된_불고기 = 상품_생성_요청(불고기).as(ProductResponse.class);
        생성된_김치 = 상품_생성_요청(김치).as(ProductResponse.class);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response, Arrays.asList(생성된_불고기.getId(), 생성된_김치.getId()));

    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
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

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_목록_응답됨(ExtractableResponse<Response> response, List<Long> productIds) {
        List<Long> ids = response.jsonPath().getList(".", ProductResponse.class)
                        .stream()
                        .map(ProductResponse::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(productIds)
        );
    }
}
