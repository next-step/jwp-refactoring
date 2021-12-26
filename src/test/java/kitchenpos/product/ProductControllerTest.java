package kitchenpos.product;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService)).build();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void registerProduct() throws Exception {

        //given
        ProductRequest 후라이드 = new ProductRequest("후라이드", new BigDecimal("16000"));
        Product product = 후라이드.toEntity();
        when(productService.create(any())).thenReturn(ProductResponse.of(product));

        //when
        ResultActions resultActions = post("/api/products", 후라이드);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void getProducts() throws Exception {

        //given
        Product 후라이드 = Product.create("후라이드", new BigDecimal("16000"));
        when(productService.list()).thenReturn(ProductResponse.ofList(Arrays.asList(후라이드)));

        //when
        ResultActions resultActions = get("/api/products", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").value(후라이드.getName()));
        resultActions.andExpect(jsonPath("$[0]['price']").value(16_000));
    }

}
