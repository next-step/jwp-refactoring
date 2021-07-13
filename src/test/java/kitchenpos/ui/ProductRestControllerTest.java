package kitchenpos.ui;

import kitchenpos.product.presentation.dto.ProductRequest;
import kitchenpos.product.presentation.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 통합테스트")
class ProductRestControllerTest extends IntegrationSupport {
    private static final String URI = "/api/products";

    @DisplayName("상품을 추가한다.")
    @Test
    void create() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(postAsJson(URI, ProductRequest.of("후라이드치킨", BigDecimal.valueOf(12000))));

        //then
        actions.andExpect(status().isCreated());
        //and then
        ProductResponse response = toObject(actions.andReturn(), ProductResponse.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("후라이드치킨");
        assertThat(response.getPrice().longValue()).isEqualTo(12000L);
    }

    @DisplayName("상품을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk());
        //and then
        List<ProductResponse> response = toList(actions.andReturn(), ProductResponse.class);
        assertThat(response).isNotEmpty();
    }
}
