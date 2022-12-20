package kitchenpos.product.ui;

import io.restassured.RestAssured;
import kitchenpos.ProductUiTest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 ui 테스트")
class ProductRestControllerTest extends ProductUiTest {

    private Product 상품;

    @BeforeEach
    void setup() {
        super.setUp();
        상품 = 상품("kitchenpos/product", BigDecimal.ONE);
    }

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //when:
        final Product 저장된_상품 = 상품_생성_됨(상품);
        //then:
        assertThat(저장된_상품).isEqualTo(상품);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() {
        //given:
        상품_생성_됨(상품);
        //when:
        final List<Product> 상품_목록 = 상품_목록_조회_됨();
        //then:
        assertThat(상품_목록).isNotEmpty();
    }

    private Product 상품_생성_됨(Product 상품) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(상품)
                .when().post("/api/products")
                .then().log().all()
                .extract().as(Product.class);
    }

    private List<Product> 상품_목록_조회_됨() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract().jsonPath().getList(".", Product.class);
    }
}
