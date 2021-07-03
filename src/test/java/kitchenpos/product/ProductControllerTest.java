package kitchenpos.product;

import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends ControllerTest {

    @Test
    @DisplayName("상품을 생성 한다")
    public void createProduct() throws Exception {
        String name = "치킨";
        BigDecimal price = new BigDecimal(20000);
        Product product = new Product( name, price);

        상품_생성_요청(product)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(name))
            .andExpect(jsonPath("price").isNumber())
        ;
    }

    @Test
    @DisplayName("상품을 생성 실패 - 가격이 음수")
    public void createProductFailByPriceMinus() {
        String name = "피자";
        BigDecimal price = new BigDecimal(-10000);

        Product product = new Product( name, price);
        assertThrows(NestedServletException.class, () -> 상품_생성_요청(product));
    }

    @Test
    @DisplayName("상품 리스트를 가져온다")
    public void selectProductList() throws Exception {
        상품_리스트_요청()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(6)))
            .andExpect(jsonPath("$.[0].id").value(1L))
            .andExpect(jsonPath("$.[0].name").value("후라이드"))
        ;
    }

    private ResultActions 상품_생성_요청(Product product) throws Exception {
        return mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andDo(print());
    }

    private ResultActions 상품_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}
