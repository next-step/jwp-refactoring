package kitchenpos.product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
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
import kitchenpos.utils.domain.ProductObjects;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("상품 관리 기능")
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
        // given
        List<ProductResponse> productResponses = productObject.getProducts()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
        given(productService.findAllProducts()).willReturn(productResponses);

        // when
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name").value(productObject.getProduct1().getProductName().toString()))
                .andExpect(jsonPath("[0].price").value(productObject.getProduct1().getProductPrice().toBigDecimal()))
                .andExpect(jsonPath("[5].name").value(productObject.getProduct6().getProductName().toString()))
                .andExpect(jsonPath("[5].price").value(productObject.getProduct6().getProductPrice().toBigDecimal()))
        ;
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void save_product() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest(productObject.getProduct3().getProductName().toString(),
                productObject.getProduct3().getProductPrice().toBigDecimal());
        ProductResponse result = ProductResponse.of(productObject.getProduct3());
        given(productService.create(any(ProductRequest.class))).willReturn(result);

        // then
        mockMvc.perform(post(REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(result.getName()))
                .andExpect(jsonPath("price").value(result.getPrice()))
        ;
    }
}
