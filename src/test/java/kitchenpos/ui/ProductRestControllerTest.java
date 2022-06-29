package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.utils.BaseTest;
import kitchenpos.utils.generator.ProductFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Product")
class ProductRestControllerTest extends BaseTest {

    private static final String PRODUCT_API_URL_TEMPLATE = "/api/products";

    @Test
    @DisplayName("상품 목록을 조회한다.")
    public void getProducts() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.get(PRODUCT_API_URL_TEMPLATE);

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].id").exists())
            .andExpect(jsonPath("$.[*].name").exists())
            .andExpect(jsonPath("$.[*].price").exists())
        ;
    }

    @Test
    @DisplayName("상품을 추가한다.")
    public void createProduct() throws Exception {
        // Given
        final String name = "뽀빠이 닭강정";
        final BigDecimal price = BigDecimal.valueOf(23000);
        final Product given = ProductFixtureGenerator.generateProduct(name, price);

        // When
        ResultActions resultActions = mockMvcUtil.post(PRODUCT_API_URL_TEMPLATE, given);

        // Then
        Product product = (Product) mockMvcUtil.as(resultActions, Product.class);
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, "/api/products/" + product.getId()))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(name))
        ;
    }
}
