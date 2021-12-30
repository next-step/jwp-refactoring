package kitchenpos.menu.domain;

import kitchenpos.common.exception.OverMenuPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.product.domain.ProductTest.양념치킨_상품;
import static kitchenpos.product.domain.ProductTest.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuProductsTest {
    private MenuProducts 메뉴_상품_리스트;
    private MenuProduct 메뉴_상품 = new MenuProduct(1L, 후라이드_상품.getId(), 1L);

    @BeforeEach
    void setUp() {

        메뉴_상품_리스트 = new MenuProducts();
        메뉴_상품_리스트.add(메뉴_상품);
    }

    @Test
    @DisplayName("메뉴 상품 리스트 생성")
    void create() {
        // given
        // when
        MenuProducts actual = MenuProducts.of(Collections.singletonList(메뉴_상품));
        // then
        assertThat(actual).isEqualTo(메뉴_상품_리스트);
    }

    @Test
    @DisplayName("메뉴 상품 추가")
    void addTest() {
        // given
        MenuProduct 추가_상품 = new MenuProduct(2L, 양념치킨_상품.getId(), 2L);
        // when
        메뉴_상품_리스트.add(추가_상품);
        // then
        assertThat(메뉴_상품_리스트.getMenuProducts()).containsExactly(메뉴_상품, 추가_상품);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 32001", "2, 50000"
    })
    @DisplayName("메뉴 가격은 [`상품 가격 * 메뉴에 속하는 상품 수량`]의 합보다 클 수 없다.")
    void totalPriceTest(long quantity, Long menuPrice) {
        // given
        // when
        메뉴_상품_리스트.add(new MenuProduct(2L, 양념치킨_상품.getId(), quantity));
        // then
        assertThrows(OverMenuPriceException.class,
                () -> 메뉴_상품_리스트.validateOverPrice(
                        BigDecimal.valueOf(menuPrice)
                        , Arrays.asList(후라이드_상품, 양념치킨_상품)));
    }


}