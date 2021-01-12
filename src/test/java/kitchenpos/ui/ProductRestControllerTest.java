package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProduct() throws Exception {
        int 치킨가격 = 17000;
        Product 후라이드치킨 = new Product("후라이드치킨", new BigDecimal(치킨가격));
        Product 양념치킨 = new Product("양념치킨", new BigDecimal(치킨가격));

        when(productService.list()).thenReturn(Arrays.asList(
                후라이드치킨,
                양념치킨));

        mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("후라이드치킨", "양념치킨")))
                .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(17000, 17000)));
    }
}