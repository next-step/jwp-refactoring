package kitchenpos.menu.domain;

import static kitchenpos.menu.__fixture__.MenuGroupTestFixture.메뉴_그룹_생성;
import static kitchenpos.menu.__fixture__.MenuTestFixture.메뉴_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuProduct 메뉴_상품;
    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        Product 상품 = new Product("치킨", BigDecimal.valueOf(16_000L));
        메뉴_상품 = new MenuProduct(상품, 1L);
        메뉴_그룹 = 메뉴_그룹_생성(1L, "메뉴_그룹");
    }

    @Test
    @DisplayName("name이 null일 경우 Exception")
    void nameIsNullException() {
        assertThatThrownBy(() -> 메뉴_생성(1L, null, BigDecimal.valueOf(16_000), 메뉴_그룹, Arrays.asList(메뉴_상품)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 0 미만일 경우 Exception")
    void priceIsLessThanZero() {
        assertThatThrownBy(() -> 메뉴_생성(1L, "후라이드치킨", BigDecimal.valueOf(-16_000), 메뉴_그룹, Arrays.asList(메뉴_상품)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품 가격 합보다 클 경우 Exception")
    void validatePrice() {
        final Menu menu = 메뉴_생성(1L, "후라이드치킨", BigDecimal.valueOf(17_000), 메뉴_그룹, Arrays.asList(메뉴_상품));
        assertThatThrownBy(() -> menu.validatePrice())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
