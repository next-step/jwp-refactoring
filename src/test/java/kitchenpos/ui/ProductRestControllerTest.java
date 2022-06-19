package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductRestControllerTest extends BaseRestController {

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductRestController(productService)).build();
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        Product product = new Product("product", BigDecimal.valueOf(1000));
        String requestBody = objectMapper.writeValueAsString(product);

        given(productService.create(any())).willReturn(new Product(1L, "product", BigDecimal.valueOf(1000)));

        //when then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("상품을 전체 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(productService.list()).willReturn(Arrays.asList(new Product(1L, "product", BigDecimal.valueOf(1000))));
        //when then
        mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.size()").value(1));
    }
}
