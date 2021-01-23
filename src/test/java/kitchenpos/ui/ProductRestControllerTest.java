package kitchenpos.ui;

import kitchenpos.application.DomainTestUtils;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

@AutoConfigureMockMvc
class ProductRestControllerTest extends DomainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("상품을 생성한다")
    @Test
    void create() throws Exception {
        Product 뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        String body = objectMapper.writeValueAsString(뿌링클);

        컨트롤러_생성_요청_및_검증(mockMvc, PRODUCT_URI, body);
    }

    @DisplayName("상품을 조회한다")
    @Test
    void search() throws Exception {
        컨트롤러_조회_요청_및_검증(mockMvc, PRODUCT_URI);
    }
}