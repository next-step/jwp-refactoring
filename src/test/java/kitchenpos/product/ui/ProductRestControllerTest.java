package kitchenpos.product.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductResponse response;

    @Test
    @DisplayName("상품 생성 확인")
    public void whenPostProduct_thenReturnStatus() throws Exception {
        Product product = new Product(1L,"강정치킨", new BigDecimal(17000));

        when(productService.create(any())).thenReturn(response.of(product));

        mockMvc.perform(post("/api/products")
                .content(asJsonString(product))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 가격이 조건에 맞지 않을 겨우")
    public void whenPostProduct_thenReturnThrow() throws Exception {
        Product product = new Product(1L,"강정치킨", new BigDecimal(-1));

        when(productService.create(any())).thenThrow(IllegalArgumentException.class);
        try {
            mockMvc.perform(post("/api/products")
                    .content(asJsonString(product))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("상품 조회")
    public void givenProduct_whenGetProduct_thenReturnStatus() throws Exception {
        Product product = new Product(1L,"강정치킨", new BigDecimal(-1));

        List<Product> allProducts = Arrays.asList(product);

        given(productService.list()).willReturn(allProducts);

        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
