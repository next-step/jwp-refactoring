package kitchenpos.Product.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductRestController;
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

import static kitchenpos.Product.application.ProductServiceTest.상품_생성;
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

    @Autowired
    ProductRestController productRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc();

        상품1 = 상품_생성(1L, "반반콤보", new BigDecimal(18000));

        상품2 = 상품_생성(2L, "허니콤보", new BigDecimal(18000));
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        given(productService.create(any())).willReturn(new ProductResponse(상품1));

        final ResultActions actions =상품_등록_요청();

        상품_등록됨(actions);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void searchProductList() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(new ProductResponse(상품1), new ProductResponse(상품2)));

        final ResultActions actions = 상품_목록_조회_요청();

        상품_목록_조회됨(actions);
    }

    public String toJson(Product product) throws JsonProcessingException {
        return objectMapper.writeValueAsString(product);
    }

    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private void 상품_등록됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/products/1"))
                .andExpect(content().string(containsString("반반콤보")));
    }

    private ResultActions 상품_등록_요청() throws Exception {
        return mockMvc.perform(post(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(상품1)));
    }

    private void 상품_목록_조회됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("반반콤보")))
                .andExpect(content().string(containsString("허니콤보")));
    }

    private ResultActions 상품_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(PRODUCT_URI)
                .contentType(MediaType.APPLICATION_JSON));
    }
}