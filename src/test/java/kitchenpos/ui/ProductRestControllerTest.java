package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends IntegrationTestHelper {

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @DisplayName("상품 생성")
    @Test
    void createTest() throws Exception {
        // given
        Product product = new Product(1l, "불고기", new BigDecimal(1000));
        Mockito.when(productService.create(any())).thenReturn(product);

        // when
        ResultActions resultActions = 상품_생성_요청(product);

        // then
        MvcResult mvcResult = 상품_생성_성공(resultActions);
        Product responseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);
        assertAll(() -> {
            assertThat(responseBody.getId()).isEqualTo(product.getId());
            assertThat(responseBody.getName()).isEqualTo(product.getName());
            assertThat(responseBody.getPrice()).isEqualTo(product.getPrice());
        });
        Mockito.verify(productService).create(any());

    }

    @DisplayName("전체 상품 조회")
    @Test
    void listTest() throws Exception {
        // then
        Product 불고기 = new Product(1l, "불고기", new BigDecimal(1000));
        Product 삼겹살 = new Product(1l, "삼겹살", new BigDecimal(1500));
        Mockito.when(productService.list()).thenReturn(Arrays.asList(불고기, 삼겹살));

        // when
        ResultActions resultActions = 전체_상품_조회_요청();

        // then
        MvcResult mvcResult = 전체_상품_조회_성공(resultActions);
        List<Product> products = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Product>>(){});
        assertThat(products).isNotEmpty().hasSize(2);

    }

    private ResultActions 상품_생성_요청(final Product product) throws Exception {
        return postRequest("/api/products", product);
    }

    private MvcResult 상품_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 전체_상품_조회_요청() throws Exception {
        return getRequest("/api/products");
    }

    private MvcResult 전체_상품_조회_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
