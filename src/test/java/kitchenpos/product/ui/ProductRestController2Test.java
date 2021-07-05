package kitchenpos.product.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.ProductService2;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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

@UTF8MockMvcTest(controllers = {ProductRestController2.class})
class ProductRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService2 productService;

  @DisplayName("새로운 상품을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    ProductResponse createdProduct = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17_000D));
    when(productService.create(any())).thenReturn(createdProduct);
    ProductRequest createRequestBody = new ProductRequest("강정치킨", 17_000D);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/v2/products")
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("상품 목록을 조회한다.")
  @Test
  void findAllProductsTest() throws Exception {
    ProductResponse createdProduct1 = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17_000D));
    ProductResponse createdProduct2 = new ProductResponse(2L, "후라이드치킨", BigDecimal.valueOf(15_000D));
    when(productService.findAllProducts()).thenReturn(Arrays.asList(createdProduct1, createdProduct2));
    mockMvc
        .perform(get("/api/v2/products"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("강정치킨")))
        .andExpect(content().string(containsString("후라이드치킨")));
  }


}
