package kitchenpos.product.ui;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.common.vo.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    private static final String BASE_PATH = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("상품 등록")
    @Test
    void create() throws Exception {
        //given
        String productName = "육개장";
        BigDecimal price = BigDecimal.valueOf(9000);
        ProductRequest requestProduct = new ProductRequest(productName, price);
        ProductResponse expectedProduct = ProductResponse.from(
            new Product(1L, productName, Price.valueOf(price)));
        given(productService.create(any())).willReturn(expectedProduct);

        //when, then
        mockMvc.perform(post(BASE_PATH).content(CommonTestFixtures.asJsonString(requestProduct))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedProduct.getId()))
            .andExpect(jsonPath("$.name").value(expectedProduct.getName()));
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() throws Exception {
        //given
        List<ProductResponse> expectedProducts = ProductResponse.fromList(Arrays.asList(
            new Product(1L, "육개장", Price.valueOf(BigDecimal.valueOf(9000))),
            new Product(1L, "과메기", Price.valueOf(BigDecimal.valueOf(22000)))
        ));

        given(productService.list()).willReturn(expectedProducts);

        //when, then
        mockMvc.perform(get(BASE_PATH)).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                containsInAnyOrder(
                    expectedProducts.stream().mapToInt(product -> product.getId().intValue())
                        .boxed()
                        .toArray(Integer[]::new))));
    }
}
