package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuGroupTestFixture.세트류;
import static kitchenpos.menu.domain.MenuProductTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        Menu 짜장_탕수육_세트 = Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 세트류, 짜장면_1그릇, 탕수육_소_1그릇);

        // then
        assertAll(
                () -> assertThat(짜장_탕수육_세트).isNotNull(),
                () -> assertThat(짜장_탕수육_세트).isInstanceOf(Menu.class)
        );
    }

    @Test
    @DisplayName("가격이 없는 메뉴를 등록한다.")
    void createMenuByPriceIsNull() {
        // when & then
        assertThatThrownBy(() -> Menu.of("짜장_탕수육_세트", null, 세트류, 짜장면_1그릇, 탕수육_소_1그릇))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @Test
    @DisplayName("가격이 0원 이하인 메뉴를 등록한다.")
    void createMenuByPriceLessThanZero() {
        // when & then
        assertThatThrownBy(() -> Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(-2_000L), 세트류, 짜장면_1그릇, 탕수육_소_1그릇))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }


    @DisplayName("메뉴 그룹에 속해있지 않은 메뉴를 등록한다.")
    @Test
    void createMenuByMenuGroupNotExist() {
        // when & then
        assertThatThrownBy(() -> Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(25_000L), null, 짜장면_1그릇, 탕수육_소_1그릇))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("메뉴 그룹은 필수입니다.");
    }

    @Test
    @DisplayName("상품으로 등록되어 있지 않은 메뉴를 등록한다.")
    void createMenuByNotCreateProduct() {
        // when & then
        assertThatThrownBy(() -> Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 세트류, menuProduct(1L, null, 1L), 탕수육_소_1그릇))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품은 필수입니다.");
    }

    @DisplayName("메뉴 가격은 메뉴 상품가격의 합계보다 클 수 없다.")
    @Test
    void createMenuByNotMoreThanProductsSum() {
        // when & then
        assertThatThrownBy(() -> Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(26_000L), 세트류, 짜장면_1그릇, 탕수육_소_1그릇))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품 총 금액이 메뉴의 가격 보다 클 수 없습니다.");
    }

    @DisplayName("메뉴의 상품 목록을 반환한다")
    @Test
    void menuProductsList() {
        // given
        Menu 짜장_탕수육_세트 = Menu.of("짜장_탕수육_세트", BigDecimal.valueOf(25_000L), 세트류, 짜장면_1그릇, 탕수육_소_1그릇);

        // when
        List<MenuProduct> actual = 짜장_탕수육_세트.menuProducts();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(짜장면_1그릇, 탕수육_소_1그릇)
        );
    }
}
