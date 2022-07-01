package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {
    private static final String PRODUCT_URL = "/api/products";
    public static final Product 불고기버거 = new Product();
    public static final Product 새우버거 = new Product();

    static {
        불고기버거.setName("불고기버거");
        불고기버거.setPrice(BigDecimal.valueOf(1500.0));

        새우버거.setName("새우버거");
        새우버거.setPrice(BigDecimal.valueOf(2000.0));
    }

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
        제품_추가_됨(제품_추가_요청);

        // when
        final ExtractableResponse<Response> 전체_제품_조회 = 제품_전체_조회();
        // then
        final List<Product> products = 제품_조회_됨(전체_제품_조회);
        final String 제품명 = products.stream()
                .filter(it -> ProductAcceptanceTest.불고기버거.getName().equals(it.getName()))
                .map(Product::getName)
                .findFirst()
                .get();

        assertThat(제품명).isEqualTo(불고기버거.getName());
    }

    public static ExtractableResponse<Response> 제품_추가_요청(final Product product) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post(PRODUCT_URL)
                .then().log().all()
                .extract();
    }

    public static Product 제품_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Product.class);
    }

    public static Product 제품_추가_되어_있음(final Product product) {
        return 제품_추가_됨(제품_추가_요청(product));
    }

    public static ExtractableResponse<Response> 제품_전체_조회() {
        return RestAssured.given()
                .when().get(PRODUCT_URL)
                .then()
                .extract();
    }

    public static List<Product> 제품_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", Product.class);
    }
}
