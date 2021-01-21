package kitchenpos.ui;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductRestControllerTest extends ControllerTest {

    @Test
    void create() throws Exception {
        Product 뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        String body = objectMapper.writeValueAsString(뿌링클);

        컨트롤러_생성_요청(PRODUCT_URI, body);
    }

    @Test
    void search() throws Exception {
        컨트롤러_조회_요청(PRODUCT_URI);
    }
}