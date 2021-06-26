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

@DisplayName("상품 관련 테스트")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {
    public static final String PRODUCT_URI = "/api/products";

    private Product product1;
    private Product product2;

    private MockMvc mockMvc;

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

        product1 = new Product();
        product1.setId(1L);
        product1.setName("후라이드");
        product1.setPrice(new BigDecimal(16000));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("양념치킨");
        product2.setPrice(new BigDecimal(16000));
    }

    public String toJsonString(Product product) throws JsonProcessingException {
        return objectMapper.writeValueAsString(product);
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(productService.create(any())).willReturn(product1);

        // when
        final ResultActions actions = mockMvc.perform(post(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(product1)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/products/1"))
                .andExpect(content().string(containsString("후라이드")));
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(productService.list()).willReturn(Arrays.asList(product1, product2));

        // when
        final ResultActions actions = mockMvc.perform(get(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("후라이드")))
                .andExpect(content().string(containsString("양념치킨")));
    }
}