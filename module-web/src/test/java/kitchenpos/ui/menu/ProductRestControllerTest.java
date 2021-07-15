package kitchenpos.ui.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.ui.config.MockMvcTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ProductRestController.class})
@MockMvcTestConfig
class ProductRestControllerTest {
    private static final String PRODUCT_API_URI = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductResponse 강정치킨;
    private ProductResponse 후라이드;

    @BeforeEach
    void setUp() {
        강정치킨 = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17000));
        후라이드 = new ProductResponse(2L, "후라이드", BigDecimal.valueOf(16000));
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
        actions.andDo(print())
                .andExpect(status().isCreated())
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
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("강정치킨")))
                .andExpect(content().string(containsString("후라이드")));
    }
}
