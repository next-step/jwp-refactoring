package kitchenpos.product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import kitchenpos.utils.domain.ProductObjects;
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
    private ProductObjects productObject;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @BeforeEach
    void setUp() {
        productObject = new ProductObjects();
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void retrieve_productList() throws Exception {
        // mocking
        when(productService.list()).thenReturn(productObject.getProducts());

        // when
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(productObject.getProduct1().getId()))
                .andExpect(jsonPath("[0].name").value(productObject.getProduct1().getName()))
                .andExpect(jsonPath("[0].price").value(productObject.getProduct1().getPrice()))
                .andExpect(jsonPath("[5].id").value(productObject.getProduct6().getId()))
                .andExpect(jsonPath("[5].name").value(productObject.getProduct6().getName()))
                .andExpect(jsonPath("[5].price").value(productObject.getProduct6().getPrice()))
        ;
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void save_product() throws Exception {
        // mocking
        when(productService.create(any(Product.class))).thenReturn(productObject.getProduct3());

        // when
        mockMvc.perform(post(REQUEST_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(productObject.getProduct1())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(productObject.getProduct3().getId()))
                .andExpect(jsonPath("name").value(productObject.getProduct3().getName()))
                .andExpect(jsonPath("price").value(productObject.getProduct3().getPrice()))
        ;
    }
}
