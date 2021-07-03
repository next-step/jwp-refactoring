package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("상품 관련 기능 테스트")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    public static final String PRODUCT_URI = "/api/products";
    private MockMvc mockMvc;
    private Product 상품1;
    private Product 상품2;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired ProductRestController productRestController) {
        // MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        상품1 = new Product();
        상품1.setId(1L);
        상품1.setName("반반콤보");
        상품1.setPrice(new BigDecimal(18000));

        상품2 = new Product();
        상품2.setId(2L);
        상품2.setName("허니콤보");
        상품2.setPrice(new BigDecimal(18000));
    }

    public String toJson(Product product) throws JsonProcessingException {
        return objectMapper.writeValueAsString(product);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        given(productService.create(any())).willReturn(상품1);

        final ResultActions actions = mockMvc.perform(post(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(상품1)));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/products/1"))
                .andExpect(content().string(containsString("반반콤보")));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void searchProductList() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(상품1, 상품2));

        final ResultActions actions = mockMvc.perform(get(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("반반콤보")))
                .andExpect(content().string(containsString("허니콤보")));
    }
}