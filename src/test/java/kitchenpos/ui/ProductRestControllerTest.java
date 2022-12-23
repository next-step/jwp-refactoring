package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {
    @MockBean
    private ProductService productService;

    private Product pasta;
    private Product pizza;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        pasta = new Product(1L, "pasta", BigDecimal.ONE);
        pizza = new Product(2L, "pizza", BigDecimal.TEN);
    }

    @DisplayName("[POST] 상품 생성")
    @Test
    void create() throws Exception {
        given(productService.create(any(Product.class))).willReturn(pasta);

        perform(postAsJson("/api/products", pasta))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/products/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("pasta"))
            .andExpect(jsonPath("$.price").value(BigDecimal.ONE));
    }

    @DisplayName("[GET] 상품 목록 조회")
    @Test
    void list() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(pasta, pizza));

        perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("pasta"))
            .andExpect(jsonPath("$[0].price").value(BigDecimal.ONE))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("pizza"))
            .andExpect(jsonPath("$[1].price").value(BigDecimal.TEN));
    }
}
