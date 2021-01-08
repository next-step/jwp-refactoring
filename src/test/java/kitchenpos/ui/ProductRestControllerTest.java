package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Order;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.product.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private ProductRestController productRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setup() {
        productRestController = new ProductRestController(productService);

        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProductsTest() throws Exception {
        // given
        String url = "/api/products";
        String name = "new product";
        BigDecimal price = BigDecimal.ONE;
        ProductRequest productRequest = new ProductRequest(name, price);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        given(productService.create(productRequest)).willReturn(savedProduct);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + savedProduct.getId()))
        ;
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProductsTest() throws Exception {
        // given
        String url = "/api/products";

        given(productService.list()).willReturn(Arrays.asList(new Product(), new Product()));

        // when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
        ;
    }
}