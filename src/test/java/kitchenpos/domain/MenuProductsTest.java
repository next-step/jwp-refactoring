package kitchenpos.domain;

import static kitchenpos.util.TestDataSet.양념_2개;
import static kitchenpos.util.TestDataSet.후라이드_2개;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.menu.MenuProducts;

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
}
