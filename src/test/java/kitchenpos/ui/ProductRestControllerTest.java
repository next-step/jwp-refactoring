package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ApiTest {

    @MockBean
    private ProductService productService;
    private Product product;

    @BeforeEach
    public void setUp() {
        super.setUp();

        product = new Product();
        product.setId(1L);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(10000));
    }

    @Test
    @DisplayName("상품을 생성한다")
    void createTest() throws Exception {

        // given
        when(productService.create(any())).thenReturn(product);

        // then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("치킨")))
        ;
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void listTest() throws Exception {

        // given
        when(productService.list()).thenReturn(Collections.singletonList(product));

        // then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("치킨")))
        ;
    }
}