package kitchenpos.product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("상품 관리 기능")
@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/products";

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void retrieve_productList() throws Exception {
        // given
        ProductResponse productResponse1 = ProductResponse.of(1L, "A", BigDecimal.valueOf(1_000.00));
        ProductResponse productResponse2 = ProductResponse.of(2L, "B", BigDecimal.valueOf(1_000.00));
        List<ProductResponse> productResponses = Arrays.asList(productResponse1, productResponse2);

        given(productService.findAllProducts()).willReturn(productResponses);

        // when
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value(productResponse1.getName()))
                .andExpect(jsonPath("[0].price").value(productResponse1.getPrice()))
                .andExpect(jsonPath("[1].name").value(productResponse2.getName()))
                .andExpect(jsonPath("[1].price").value(productResponse2.getPrice()))
        ;
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void save_product() throws Exception {
        // given
        ProductResponse productResponse1 = ProductResponse.of(1L, "A", BigDecimal.valueOf(1_000.00));
        given(productService.create(any(ProductRequest.class))).willReturn(productResponse1);

        // then
        mockMvc.perform(post(REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new ProductRequest("A", BigDecimal.valueOf(10_000.00)))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(productResponse1.getName()))
                .andExpect(jsonPath("price").value(productResponse1.getPrice()))
        ;
    }
}
