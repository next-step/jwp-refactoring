package kitchenpos.product.ui;

import kitchenpos.common.ui.RestControllerTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends RestControllerTest {

    private static final String API_PRODUCT_ROOT = "/api/products";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    void 상품_등록() throws Exception {
        //given
        ProductRequest 후라이드치킨_요청 = ProductRequest.of("후라이드치킨", BigDecimal.valueOf(16000));
        ProductResponse 후라이드치킨_응답 = ProductResponse.of(Product.of(후라이드치킨_요청.getName(), 후라이드치킨_요청.getPrice()));
        given(productService.create(any())).willReturn(후라이드치킨_응답);

        //when
        ResultActions actions = mockMvc.perform(post(API_PRODUCT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(후라이드치킨_요청)))
                .andDo(print());

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(후라이드치킨_응답.getId()))
                .andExpect(jsonPath("$.name").value(후라이드치킨_응답.getName()))
                .andExpect(jsonPath("$.price").value(후라이드치킨_응답.getPrice()));
    }

    @Test
    void 상품_조회() throws Exception {
        //given
        List<ProductResponse> products = new ArrayList<>();
        products.add(ProductResponse.of(Product.of("후라이드치킨", BigDecimal.valueOf(16000))));
        products.add(ProductResponse.of(Product.of("양념치킨", BigDecimal.valueOf(17000))));
        given(productService.list()).willReturn(products);

        //when
        ResultActions actions = mockMvc.perform(get(API_PRODUCT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(products.get(0).getPrice()))
                .andExpect(jsonPath("$[1].id").value(products.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(products.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(products.get(1).getPrice()));
    }
}
