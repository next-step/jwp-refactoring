package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private ProductService productService;

    private ProductRequest 상품_요청1;
    private ProductResponse 상품_응답1;
    private ProductResponse 상품_응답2;
    private List<ProductResponse> 상품_목록_응답;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        상품_요청1 = ProductRequest.of("상품1", 10000);
        상품_응답1 = ProductResponse.of(1L, "상품1", 10000);
        상품_응답2 = ProductResponse.of(2L, "상품2", 20000);
        상품_목록_응답 = Lists.newArrayList(상품_응답1, 상품_응답2);
    }


    @DisplayName("상품을 등록한다.")
    @Test
    void create() throws Exception {
        
        given(productService.create(any())).willReturn(상품_응답1);

        
        final ResultActions actions = mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(상품_요청1)))
                .andDo(print());
        
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("상품1"))
                .andExpect(jsonPath("price").value(10000));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list() throws Exception {
        
        given(productService.list()).willReturn(상품_목록_응답);

        
        final ResultActions actions = mvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("상품1")))
                .andExpect(content().string(containsString("상품2")));
    }
}
