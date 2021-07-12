package kitchenpos.product.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.product.dto.CreateProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
    @ValueSource(longs = { 0, 1000, 10000 })
    @ParameterizedTest
    void createProductSuccess(Long price) throws Exception {

        CreateProductRequest request = new CreateProductRequest("product", price);
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL).content(content)
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @DisplayName("상품 가격이 음수면 상품 생성 실패")
    @NullSource
    @ValueSource(longs = { -1, -1000, -10000 })
    @ParameterizedTest
    void createProductFail(Long price) throws JsonProcessingException {
        CreateProductRequest request = new CreateProductRequest("product", price);
        String content = objectMapper.writeValueAsString(request);
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
        List<ProductResponse> list = Arrays.asList(objectMapper.readValue(content, ProductResponse[].class));
        assertThat(list).hasSize(6); // default data size
    }

    private void createRequestFail(String content) {
        try {
            mockMvc.perform(post(BASE_URL).content(content)
                                          .contentType(
                                              MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
