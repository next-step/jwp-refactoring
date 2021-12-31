package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        ProductResponse 후라이드치킨_응답 = ProductResponse.from(Product.of(후라이드치킨_요청.getName(), 후라이드치킨_요청.getPrice()));
        BDDMockito.given(productService.create(ArgumentMatchers.any())).willReturn(후라이드치킨_응답);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(API_PRODUCT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(후라이드치킨_요청)))
                .andDo(MockMvcResultHandlers.print());

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(후라이드치킨_응답.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(후라이드치킨_응답.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(후라이드치킨_응답.getPrice()));
    }

    @Test
    void 상품_조회() throws Exception {
        //given
        List<ProductResponse> products = new ArrayList<>();
        products.add(ProductResponse.from(Product.of("후라이드치킨", BigDecimal.valueOf(16000))));
        products.add(ProductResponse.from(Product.of("양념치킨", BigDecimal.valueOf(17000))));
        BDDMockito.given(productService.list()).willReturn(products);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(API_PRODUCT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(products.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(products.get(0).getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(products.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(products.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(products.get(1).getPrice()));
    }
}
