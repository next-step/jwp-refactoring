package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("상품 생성 Api 테스트")
    @Test
    void create() throws Exception {
        ProductRequest product = new ProductRequest();
        product.setName("맥도날드햄버거");
        product.setPrice(BigDecimal.valueOf(5000));

        String requestBody = objectMapper.writeValueAsString(product);

        Product responseProduct = new Product();
        responseProduct.setId(1L);
        responseProduct.setName("패스트푸드");
        responseProduct.setPrice(new Price(BigDecimal.valueOf(5000)));
        String responseBody = objectMapper.writeValueAsString(responseProduct);

        when(productService.create(any())).thenReturn(responseProduct);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("상품 목록 Api 테스트")
    @Test
    void list() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("패스트푸드");
        product.setPrice(new Price(BigDecimal.valueOf(5000)));

        List<Product> products = Arrays.asList(product);

        String responseBody = objectMapper.writeValueAsString(products);

        when(productService.list()).thenReturn(products);
        mockMvc.perform(get("/api/products")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }
}
