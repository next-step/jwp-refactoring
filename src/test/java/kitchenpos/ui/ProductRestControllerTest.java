package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {ProductRestController.class})
class ProductRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService productService;

  @DisplayName("새로운 상품을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    Product createdProduct = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    when(productService.create(any())).thenReturn(createdProduct);
    Product createRequestBody = new Product("강정치킨", BigDecimal.valueOf(17000));
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/products")
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("상품 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    Product createdProduct1 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    Product createdProduct2 = new Product(2L, "후라이드치킨", BigDecimal.valueOf(15000));
    when(productService.list()).thenReturn(Arrays.asList(createdProduct1, createdProduct2));
    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("강정치킨")));
  }


}
