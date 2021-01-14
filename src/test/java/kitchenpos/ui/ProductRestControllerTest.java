package kitchenpos.ui;

import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.service.ProductServiceJpa;
import kitchenpos.product.ui.ProductRestController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
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
    ProductServiceJpa productService;

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProduct() throws Exception {
        when(productService.list()).thenReturn(Arrays.asList(
                new ProductResponse(1L, "후라이드치킨", 17000),
                new ProductResponse(1L, "양념치킨", 17000)
                ));

        mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("후라이드치킨", "양념치킨")))
                .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(17000, 17000)));
    }
}