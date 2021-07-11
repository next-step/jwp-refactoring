package kitchenpos.menu;

import static kitchenpos.util.TestDataSet.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.MenuProducts;

public class MenuProductsTest {

    private MenuProducts list;

    @BeforeEach
    void setUp() {
        list = MenuProducts.of(Arrays.asList(후라이드_2개, 양념_2개));
    }

    @Test
    @DisplayName("MenuProducts 생성 성공 테스트")
    void create() {
        assertThat(list.contain(후라이드_2개)).isTrue();
        assertThat(list.contain(양념_2개)).isTrue();
    }

    @Test
    @DisplayName("입력가격이 가격의 합이 크다면 true, 아닐시 false를 반환한다.")
    void isSumUnder() {
        assertThat(list.isSumUnder(BigDecimal.valueOf(100))).isFalse();
        assertThat(list.isSumUnder(BigDecimal.valueOf(100000))).isTrue();
    }
}
