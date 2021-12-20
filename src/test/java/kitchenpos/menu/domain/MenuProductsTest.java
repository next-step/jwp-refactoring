package kitchenpos.menu.domain;

import kitchenpos.common.exception.OverMenuPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.domain.ProductTest.양념치킨_상품;
import static kitchenpos.menu.domain.MenuProductTest.양념치킨;
import static kitchenpos.menu.domain.MenuProductTest.후라이드;
import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest {
    private MenuProducts 메뉴_상품_리스트;

    @BeforeEach
    void setUp() {
        메뉴_상품_리스트 = MenuProducts.ofEmpty();
        메뉴_상품_리스트.add(후라이드);
    }

    @Test
    @DisplayName("메뉴 상품 리스트 생성")
    void create() {
        // given
        // when
        MenuProducts actual = MenuProducts.of(Collections.singletonList(후라이드));
        // then
        assertThat(actual).isEqualTo(메뉴_상품_리스트);
    }

    @Test
    @DisplayName("메뉴 상품 추가")
    void addTest() {
        // given
        // when
        메뉴_상품_리스트.add(양념치킨);
        // then
        assertThat(메뉴_상품_리스트.getMenuProducts()).containsExactly(후라이드, 양념치킨);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 32001", "2, 50000"
    })
    @DisplayName("메뉴 가격은 [`상품 가격 * 메뉴에 속하는 상품 수량`]의 합보다 클 수 없다.")
    void totalPriceTest(long quantity, Long menuPrice) {
        // given
        // when
        메뉴_상품_리스트.add(new MenuProduct(1L, 치킨세트, 양념치킨_상품, quantity));
        // then
        assertThatThrownBy(() -> 메뉴_상품_리스트.validateOverPrice(BigDecimal.valueOf(menuPrice)))
                .isInstanceOf(OverMenuPriceException.class);
    }


}