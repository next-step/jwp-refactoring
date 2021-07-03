package kitchenpos.ui;

import static kitchenpos.util.TestDataSet.강정치킨;
import static kitchenpos.util.TestDataSet.양념치킨;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@WebMvcTest(controllers = ProductRestController.class)
@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    private static final String BASE_URL = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("이름과 가격을 받아 상품을 등록할 수 있다.")
    void create() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(강정치킨);
        given(productService.create(any(Product.class))).willReturn(강정치킨);

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(강정치킨.getName()));
    }

    @Test
    @DisplayName("등록된 상품들의 리스트를 볼 수 있다.")
    void list() throws Exception {
        // given
        given(productService.list())
            .willReturn(Arrays.asList(강정치킨, 양념치킨));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(강정치킨.getName()))
            .andExpect(jsonPath("$[1].name").value(양념치킨.getName()));
    }

}
