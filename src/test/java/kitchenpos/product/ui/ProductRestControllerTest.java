package kitchenpos.product.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockMvcTestConfig
class ProductRestControllerTest {

    private static final String BASE_URL = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("상품 생성 성공")
    @ValueSource(ints = { 0, 1000, 10000 })
    @ParameterizedTest
    void createProductSuccess(int price) throws Exception {

        Product product = new Product();
        product.setName("product");
        product.setPrice(new BigDecimal(price));

        String content = objectMapper.writeValueAsString(product);

        mockMvc.perform(post(BASE_URL).content(content)
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @DisplayName("상품 가격이 음수면 상품 생성 실패")
    @ValueSource(ints = { -1, -1000, -10000 })
    @ParameterizedTest
    void createProductFail01(int price) throws JsonProcessingException {

        Product product = new Product();
        product.setName("product");
        product.setPrice(new BigDecimal(price));

        String content = objectMapper.writeValueAsString(product);
        createRequestFail(content);
    }

    @DisplayName("상품 가격이 입력되지 않으면 상품 생성 실패")
    @Test
    void createProductFail02() throws Exception {

        Product product = new Product();
        product.setName("product");

        String content = objectMapper.writeValueAsString(product);
        createRequestFail(content);
    }

    @DisplayName("모든 상품 목록을 가져온다.")
    @Test
    void findProductTest() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Product> list = Arrays.asList(objectMapper.readValue(content, Product[].class));
        assertThat(list).hasSize(6); // default data size
    }

    private void createRequestFail(String content) {
        try {
            mockMvc.perform(post(BASE_URL).content(content)
                                          .contentType(
                                              MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(result -> assertTrue(
                       result.getResolvedException() instanceof IllegalArgumentException))
                   .andReturn();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
