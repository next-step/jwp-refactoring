package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.IllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.domain.MenuProductTest.getMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {
    @DisplayName("메뉴에 상품을 추가한다.")
    @Test
    void 메뉴_상품_추가() {
        MenuProduct 양념치킨_메뉴_상품 = getMenuProduct(1L, "양념치킨", 19000, 2L);
        MenuProduct 맥주_메뉴_상품 = getMenuProduct(2L, "맥주", 5000, 4L);
        MenuGroup 튀김류 = MenuGroup.of(1L, "튀김류");
        Menu 양념치킨_세트 = Menu.of(1L, "양념치킨_세트", Price.of(BigDecimal.valueOf(55000)), 튀김류);

        양념치킨_세트.addMenuProducts(Arrays.asList(양념치킨_메뉴_상품, 맥주_메뉴_상품));

        assertAll(
                () -> assertThat(양념치킨_세트.getMenuProducts().asList()).containsExactly(양념치킨_메뉴_상품, 맥주_메뉴_상품),
                () -> assertThat(양념치킨_메뉴_상품.equalsMenu(양념치킨_세트)).isTrue(),
                () -> assertThat(맥주_메뉴_상품.equalsMenu(양념치킨_세트)).isTrue()
        );
    }

    @DisplayName("메뉴에 상품을 추가 시, 메뉴 가격이 상품보다 높을 경우 예외")
    @Test
    void 메뉴_상품_추가_가격_예외() {
        MenuProduct 양념치킨_메뉴_상품 = getMenuProduct(1L, "양념치킨", 19000, 2L);
        MenuProduct 맥주_메뉴_상품 = getMenuProduct(2L, "맥주", 5000, 4L);
        MenuGroup 튀김류 = MenuGroup.of(1L, "튀김류");
        Menu 양념치킨_세트 = Menu.of(1L, "양념치킨_세트", Price.of(BigDecimal.valueOf(59000)), 튀김류);

        Throwable thrown = catchThrowable(() -> 양념치킨_세트.addMenuProducts(Arrays.asList(양념치킨_메뉴_상품, 맥주_메뉴_상품)));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
    }
}
