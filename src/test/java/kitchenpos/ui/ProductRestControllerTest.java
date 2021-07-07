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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRestController productRestController;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    Product 상품;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        상품 = new Product();
        상품.setId(1L);
        상품.setName("강정치킨");
        상품.setPrice(BigDecimal.valueOf(17000));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() throws Exception {
        //given
        when(productService.create(any())).thenReturn(상품);
        String requestBody = objectMapper.writeValueAsString(상품);

        //when && then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("강정치킨")));
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() throws Exception {
        //given
        when(productService.list()).thenReturn(Arrays.asList(상품));

        //when && then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("강정치킨")));
    }
}