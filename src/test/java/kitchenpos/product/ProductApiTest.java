package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductRestController.class)
public class ProductApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    public void createProduct() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("치킨");
        mockProduct.setPrice(new BigDecimal(18000));
        Mockito.when(productService.create(Mockito.any(Product.class))).thenReturn(mockProduct);

        mockMvc.perform(post("/api/products")
                .content("{\"name\" : \"치킨\", \"price\" : 18000}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("상품 조회")
    @Test
    public void findProduct() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("치킨");
        mockProduct.setPrice(new BigDecimal(18000));
        Mockito.when(productService.list()).thenReturn(Arrays.asList(mockProduct));

        mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
