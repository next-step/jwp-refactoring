package kitchenpos.menu.product;

import kitchenpos.application.ProductService;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ProductRestController(productService)).build();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void registerProduct() throws Exception {

        //given
        Product 후라이드 = new Product();
        후라이드.setName("후라이드");
        후라이드.setPrice(new BigDecimal("10000"));
        when(productService.create(any())).thenReturn(후라이드);

        //when
        ResultActions resultActions = post("/api/products", 후라이드);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void getProducts() throws Exception {

        //given
        Product 후라이드 = new Product();
        후라이드.setName("후라이드");
        후라이드.setPrice(new BigDecimal("10000"));
        
        when(productService.list()).thenReturn(Arrays.asList(후라이드));

        //when
        ResultActions resultActions = get("/api/products", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").value(후라이드.getName()));
        resultActions.andExpect(jsonPath("$[0]['price']").value(후라이드.getPrice()));
    }

}
