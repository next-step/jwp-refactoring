package kitchenpos.ui;

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

@WebMvcTest(controllers = {ProductRestController.class})
class ProductRestControllerTest {
    private static final String PRODUCT_API_URI = "/api/products";

    @Autowired
    private ProductRestController productRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private MockMvc mockMvc;
    private Product 강정치킨;
    private Product 후라이드;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        후라이드 = new Product(2L, "후라이드", BigDecimal.valueOf(16000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(productService.create(any())).willReturn(강정치킨);

        // when
        ResultActions actions = mockMvc.perform(post(PRODUCT_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(강정치킨)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", PRODUCT_API_URI + "/1"))
                .andExpect(content().string(containsString("강정치킨")));
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        given(productService.list()).willReturn(Arrays.asList(강정치킨, 후라이드));

        // when
        ResultActions actions = mockMvc.perform(get(PRODUCT_API_URI));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("강정치킨")))
                .andExpect(content().string(containsString("후라이드")));
    }
}
