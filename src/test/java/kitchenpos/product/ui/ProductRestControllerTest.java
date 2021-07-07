package kitchenpos.product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("상품 관리 기능 테스트")
@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/products";

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private ArrayList<Product> products;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("후라이드");
        product1.setPrice(BigDecimal.valueOf(16000.00));
        product2 = new Product();
        product2.setId(2L);
        product2.setName("양념치킨");
        product2.setPrice(BigDecimal.valueOf(16000.00));
        product3 = new Product();
        product3.setId(3L);
        product3.setName("반반치킨");
        product3.setPrice(BigDecimal.valueOf(16000.00));
        product4 = new Product();
        product4.setId(4L);
        product4.setName("통구이");
        product4.setPrice(BigDecimal.valueOf(16000.00));
        products = new ArrayList<>(Arrays.asList(product1, product2, product3, product4));
    }

    @Override
    protected Object controller() {
        return productRestController;
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void retrieve_productList() throws Exception {
        // mocking
        when(productService.list()).thenReturn(products);

        // when
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*].id").value(toArrayListBy(1, 2, 3, 4)))
                .andExpect(jsonPath("[*].name").value(products.stream().map(Product::getName).collect(Collectors.toList())))
                .andExpect(jsonPath("[*].price").value(toArrayListBy(16000.00, 16000.00, 16000.00, 16000.00)))
        ;
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void save_product() throws Exception {
        // mocking
        when(productService.create(any(Product.class))).thenReturn(product3);

        // when
        mockMvc.perform(post(REQUEST_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(product1)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(product3.getId()))
                .andExpect(jsonPath("name").value(product3.getName()))
                .andExpect(jsonPath("price").value(product3.getPrice()))
        ;
    }
}
