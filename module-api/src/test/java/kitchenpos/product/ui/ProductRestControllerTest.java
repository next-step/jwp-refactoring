package kitchenpos.product.ui;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.ui.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductRestControllerTest extends ControllerTest {

    private final String PRODUCT_URI = "/api/products";

    @DisplayName("상품을 생성한다")
    @Test
    void create() throws Exception {
        ProductRequest 뿌링클 = new ProductRequest("뿌링클", BigDecimal.valueOf(18000));
        String body = objectMapper.writeValueAsString(뿌링클);

        컨트롤러_생성_요청_및_검증(PRODUCT_URI, body);
    }

    @DisplayName("상품을 조회한다")
    @Test
    void search() throws Exception {
        컨트롤러_조회_요청_및_검증(PRODUCT_URI);
    }
}