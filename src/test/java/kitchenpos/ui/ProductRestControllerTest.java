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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : ProductRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    private Product product;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private WebApplicationContext wac;

    /**
     * utf-8 설정
     *
     * @see <a href="https://stackoverflow.com/questions/55640629/how-to-make-spring-boot-default-to-application-jsoncharset-utf-8-instead-of-app">deprecated msg</a>
     */
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .build();

        product = new Product();
        product.setId(1L);
        product.setName("고추바사삭치킨");
        product.setPrice(new BigDecimal("18000"));
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    public void findProducts() throws Exception {
        // given
        List<Product> products = Arrays.asList(product);
        given(productService.list()).willReturn(products);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(product.getName())))
                .andDo(print());
    }

    @Test
    @DisplayName("상품을 등록한다.")
    public void saveProduct() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(productService.create(any(Product.class))).willReturn(product);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(product))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andDo(print());

    }
}