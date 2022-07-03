package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 관련 기능")
class ProductAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 상품이름과 가격 정보를 이용하여
     * When 상품을 생성하면
     * Then 생성된 상품 데이터가 리턴된다
     */
    @DisplayName("상품 생성")
    @Test
    void 상품_생성_요청() {
        // given
        Product product = new Product();
        product.setPrice(new BigDecimal(7000));
        product.setName("녹두빈대떡");

        // when
        ExtractableResponse<Response> 상품_생성_요청_응답 = RestAssured
                .given().log().all()
                .body(product)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 상품_생성_요청_응답.statusCode()),
                () -> assertNotNull(상품_생성_요청_응답.as(Product.class).getId()),
                () -> assertEquals(product.getName(), 상품_생성_요청_응답.as(Product.class).getName()),
                () -> assertEquals(0, product.getPrice().compareTo(상품_생성_요청_응답.as(Product.class).getPrice()))
        );
    }

}
