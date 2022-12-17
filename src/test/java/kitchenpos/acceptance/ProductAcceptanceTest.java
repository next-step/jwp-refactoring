package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

public class ProductAcceptanceTest extends MockMvcAcceptanceTest{

    /**
     * given: 상품 이름과 가격을 입력하고
     * when: 상품 등록을 시도하면
     * then: 등록된 상품이 조회된다.
     */
    @Test
    @DisplayName("상품 등록")
    void createTest() throws Exception {
        // given
        String name = "1번 상품";
        int price = 10000;

        // when
        ResultActions 상품_등록_요청_결과 = 상품_등록_요청(name, price);

        // then
        상품_등록_요청_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    /**
     * given: 상품 이름과 상품 가격을 음수로 입력하고,
     * when: 상품 등록을 시도하면
     * then: 가격이 음수일 수 없으므로, 상품등록에 실패한다.
     */
    @Test
    @DisplayName("상품 등록 시, 가격이 음수면 상품 등록에 실패한다.")
    void createFailTest1() {
        // given
        String name = "1번 상품";
        int price = -100;

        // when & then
        assertThatThrownBy(
                () -> 상품_등록_요청(name, price)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * given: 상품 이름을 입력하고,
     * when: 상품 등록을 시도하면
     * then: 가격이 없으므로, 상품등록에 실패한다.
     */
    @Test
    @DisplayName("상품 등록 시, 가격이 없으면 상품 등록에 실패한다.")
    void createFailTest2() {
        // given
        String name = "1번 상품";

        // when & then
        assertThatThrownBy(
                () -> 상품_등록_요청(name)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Background
     *  given: 기존에 상품들이 등록되어 있음
     *
     * Scenario: 상품 전체 조회
     *  when: 상품 전체 조회를 시도하면
     *  then: 등록된 모든 상품이 조회된다.
     */
    @Test
    @DisplayName("상품 전체 조회 테스트")
    void listTest() throws Exception {
        // given
        상품_등록_요청("1번 상품", 10000);
        상품_등록_요청("2번 상품", 20000);

        // when
        ResultActions 상품_전체_조회_요청_결과 = 상품_전체_조회_요청();

        // then
        상품_전체_조회_요청_결과
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").isArray())
                .andExpect(jsonPath(".name", hasItems("1번 상품", "2번 상품")))
                .andDo(print());
    }

    private ResultActions 상품_등록_요청(String name) throws Exception {
        return mockPost("/api/products", new ProductRequest(name, null));
    }

    private ResultActions 상품_전체_조회_요청() throws Exception {
        return mockGet("/api/products");
    }
}
