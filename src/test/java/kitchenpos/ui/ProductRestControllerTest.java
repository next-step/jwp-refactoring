package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ProductRestController.class)
@DisplayName("ProductRestController 테스트")
public class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductRestController productRestController;

    @Nested
    @DisplayName("POST /api/products 테스트")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 상품을 등록하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final Product mockProduct = setupSuccess("test product", 1234);

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockProduct))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final Product productResponse =
                    objectMapper.readValue(response.getContentAsString(), Product.class);
            assertAll(
                    () -> assertThat(productResponse.getName()).isEqualTo(mockProduct.getName()),
                    () -> assertThat(productResponse.getPrice()).isEqualTo(mockProduct.getPrice())
            );
        }

        private Product setupSuccess(String name, int price) {
            final Product mockProduct = new Product();
            mockProduct.setId((long) new Random().nextInt(Integer.MAX_VALUE));
            mockProduct.setName(name);
            mockProduct.setPrice(BigDecimal.valueOf(price));
            Mockito.when(productRestController.create(Mockito.any())).thenReturn(ResponseEntity.ok(mockProduct));
            return mockProduct;
        }

        @Test
        @DisplayName("가격을 입력하지 않아서 상품 등록에 실패하면 400 상태 코드를 응답받는다")
        public void errorPriceNull() throws Exception {
            // given
            final Product mockProduct = setupErrorPriceNullOrNegative("test product", BigDecimal.valueOf(1234));

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockProduct))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Product setupErrorPriceNullOrNegative(String name, BigDecimal price) {
            final Product mockProduct = new Product();
            mockProduct.setId((long) new Random().nextInt(Integer.MAX_VALUE));
            mockProduct.setName(name);
            mockProduct.setPrice(price);
            Mockito.when(productRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return mockProduct;
        }

        @Test
        @DisplayName("가격에 음수를 입력해서 상품 등록에 실패하면 400 상태 코드를 응답받는다")
        public void errorPriceNegative() throws Exception {
            // given
            final Product mockProduct = setupErrorPriceNullOrNegative("test product", BigDecimal.valueOf(-999));

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockProduct))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("GET /api/products 테스트")
    public class GetList {
        @Test
        @DisplayName("상품 목록을 조회하는 데 성공하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final List<Product> mockProducts = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(get("/api/products")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final List<Product> productsResponse =
                    objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {
                    });
            assertThat(productsResponse.size()).isEqualTo(mockProducts.size());
        }

        private List<Product> setupSuccess() {
            final List<Product> mockProducts = Arrays.asList(new Product(), new Product(), new Product());
            Mockito.when(productRestController.list()).thenReturn(ResponseEntity.ok(mockProducts));
            return mockProducts;
        }
    }
}
