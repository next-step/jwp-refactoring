package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.api.ProductRestController;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import org.assertj.core.util.Lists;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {ProductRestController.class})
class ProductRestControllerTest {
    private static final String URI = "/api/products";

    @Autowired
    private ProductRestController productRestController;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Product 후라이드치킨;
    private Product 양념치킨;
    private ProductRequest 후라이드치킨_request;
    private ProductResponse 후라이드치킨_response;
    private ProductResponse 양념치킨_response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(12000));
        양념치킨 = Product.of(2L, "양념치킨", BigDecimal.valueOf(13000));
        후라이드치킨_request = ProductRequest.of(후라이드치킨);
        후라이드치킨_response = ProductResponse.of(후라이드치킨);
        양념치킨_response = ProductResponse.of(양념치킨);
    }

    @DisplayName("상품을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(productService.create(any())).willReturn(후라이드치킨);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(후라이드치킨_request)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/" + 후라이드치킨_response.getId()))
                .andExpect(jsonPath("$.name").value(후라이드치킨_response.getName()))
                .andExpect(jsonPath("$.price").value(후라이드치킨_response.getPrice()));
    }

    @DisplayName("상품을 모두 조회한다.")
    @Test
    void list2() throws Exception {
        //given
        given(productService.list()).willReturn(Lists.list(후라이드치킨, 양념치킨));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(후라이드치킨_response.getId()))
                .andExpect(jsonPath("$[0].name").value(후라이드치킨_response.getName()))
                .andExpect(jsonPath("$[0].price").value(후라이드치킨_response.getPrice()))
                .andExpect(jsonPath("$[1].id").value(양념치킨_response.getId()))
                .andExpect(jsonPath("$[1].name").value(양념치킨_response.getName()))
                .andExpect(jsonPath("$[1].price").value(양념치킨_response.getPrice()));
    }
}
