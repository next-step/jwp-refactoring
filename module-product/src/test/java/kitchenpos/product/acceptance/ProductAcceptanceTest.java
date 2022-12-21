package kitchenpos.product.acceptance;

import java.math.BigDecimal;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class ProductAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void 상품_가격은_0원_이상_이어야_한다() throws Exception {
        ProductRequest 가격이_0원_미만인_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(-1));

        ResultActions resultActions = 상품_등록(가격이_0원_미만인_상품);

        상품_등록_실패(resultActions);
    }

    @Test
    void 상품_가격은_null_이면_안된다() throws Exception {
        ProductRequest 가격이_null_인_상품 = new ProductRequest(1L, "후라이드치킨", null);

        ResultActions resultActions = 상품_등록(가격이_null_인_상품);

        상품_등록_실패(resultActions);
    }

    @Test
    void 상품을_등록할_수_있다() throws Exception {
        ProductRequest 후라이드치킨 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));

        ResultActions resultActions = 상품_등록(후라이드치킨);

        상품_등록_성공(resultActions, 후라이드치킨);
    }

    @Test
    void 상품_목록을_조회할_수_있다() throws Exception {
        ProductRequest 후라이드치킨 = 상품이_등록되어_있다();

        ResultActions resultActions = 상품_목록_조회();

        상품_목록_조회_성공(resultActions, 후라이드치킨);
    }

    private void 상품_목록_조회_성공(ResultActions resultActions, ProductRequest product) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value(product.getPrice().floatValue()));
    }

    private ResultActions 상품_목록_조회() throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/api/products")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ProductRequest 상품이_등록되어_있다() throws Exception {
        ProductRequest 후라이드치킨 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
        ResultActions resultActions = 상품_등록(후라이드치킨);
        상품_등록_성공(resultActions, 후라이드치킨);
        return 후라이드치킨;
    }

    private void 상품_등록_성공(ResultActions resultActions, ProductRequest product) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("price").value(product.getPrice().floatValue()));
    }

    private void 상품_등록_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    private ResultActions 상품_등록(ProductRequest product) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}
