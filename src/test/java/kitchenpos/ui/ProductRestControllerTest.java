package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
@DisplayName("ProductRestController 클래스")
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Nested
    @DisplayName("POST /api/products 은")
    class Describe_create {

        @Nested
        @DisplayName("등록할 상품이 주어지면")
        class Context_with_product {
            Product givenProduct = new Product();

            @BeforeEach
            void setUp() {
                givenProduct.setName("강정치킨");
                givenProduct.setPrice(BigDecimal.valueOf(17_000));

                when(productService.create(any(Product.class)))
                        .thenReturn(givenProduct);
            }

            @DisplayName("201 Created 와 생성된 상품을 응답한다.")
            @Test
            void It_responds_created_with_product() throws Exception {
                mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenProduct)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenProduct)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("GET /api/products 는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 상품 목록이 있으면")
        class Context_with_products {
            List<Product> givenProducts;

            @BeforeEach
            void setUp() {
                Product product = new Product();
                givenProducts = Collections.singletonList(product);
                when(productService.list())
                        .thenReturn(givenProducts);
            }

            @DisplayName("200 OK 와 상퓸 목록을 응답한다.")
            @Test
            void it_responds_ok_with_products() throws Exception {
                mockMvc.perform(get("/api/products"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenProducts)
                        ));
            }
        }
    }
}
