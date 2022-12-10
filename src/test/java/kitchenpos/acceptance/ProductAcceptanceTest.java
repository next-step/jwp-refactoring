package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ProductAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void 상품_가격은_0원_이상_이어야_한다() throws Exception {
        Product 가격이_0원_미만인_상품 = new Product(1L, "후라이드치킨", new BigDecimal(-1));

        ResultActions resultActions = 상품_등록(가격이_0원_미만인_상품);

        상품_등록_실패(resultActions);
    }

    @Test
    void 상품을_등록할_수_있다() throws Exception {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));

        ResultActions resultActions = 상품_등록(후라이드치킨);

        상품_등록_성공(resultActions, 후라이드치킨);
    }

    @Test
    void 상품_목록을_조회할_수_있다() throws Exception {
        Product 후라이드치킨 = 상품이_등록되어_있다();

        ResultActions resultActions = 상품_목록_조회();

        상품_목록_조회_성공(resultActions, 후라이드치킨);
    }

    private void 상품_목록_조회_성공(ResultActions resultActions, Product product) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(product.getId()))
                .andExpect(jsonPath("$.[0].name").value(product.getName()))
                .andExpect(jsonPath("$.[0].price").value(product.getPrice().floatValue()));
    }

    private ResultActions 상품_목록_조회() throws Exception {
        return mvc.perform(get("/api/products")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private Product 상품이_등록되어_있다() throws Exception {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        ResultActions resultActions = 상품_등록(후라이드치킨);
        상품_등록_성공(resultActions, 후라이드치킨);
        return 후라이드치킨;
    }

    private void 상품_등록_성공(ResultActions resultActions, Product product) throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(product.getId()))
                .andExpect(jsonPath("name").value(product.getName()))
                .andExpect(jsonPath("price").value(product.getPrice().floatValue()));
    }

    private void 상품_등록_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private ResultActions 상품_등록(Product product) throws Exception {
        return mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
