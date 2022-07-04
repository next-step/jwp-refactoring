package kitchenpos.ui;

import static kitchenpos.utils.MockMvcUtil.as;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성_요청;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.product.Product;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Product")
public class ProductRestControllerTest extends BaseTest {

    public static final String PRODUCT_API_URL_TEMPLATE = "/api/products";

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
            .andExpect(jsonPath("$.[*].price").exists());
    }

    @Test
    @DisplayName("상품을 추가한다.")
    public void createProduct() throws Exception {
        // Given
        final String name = "뽀빠이 닭강정";
        final int price = 23_000;

        // When
        ResultActions resultActions = mockMvcUtil.post(상품_생성_요청(name, price));

        // Then
        Product product = as(resultActions, Product.class);
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.LOCATION, "/api/products/" + product.getId()))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(name));
    }
}
