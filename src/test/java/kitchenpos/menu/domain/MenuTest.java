package kitchenpos.menu.domain;

import static common.MenuGroupFixture.메뉴그룹_한마리;
import static common.MenuProductFixture.양념치킨_1개;
import static common.MenuProductFixture.콜라_1개;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.common.exception.Message;
import kitchenpos.product.domain.Amount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuTest {

    @Test
    void 메뉴_정상등록() {
        // given
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        Menu menu = Menu.of("양념치킨", Amount.of(18000), 메뉴그룹_한마리.getId());
        MenuProduct 양념치킨_1개 = 양념치킨_1개();
        MenuProduct 콜라_1개 = 콜라_1개();

        // when
        menu.addMenuProducts(MenuProducts.of(asList(양념치킨_1개, 콜라_1개)));

        // then
        assertAll(() -> {
            Assertions.assertThat(menu.getMenuGroupId()).isEqualTo(메뉴그룹_한마리.getId());
            Assertions.assertThat(menu.getName()).isEqualTo("양념치킨");
            Assertions.assertThat(menu.getPrice().getPrice()).isEqualTo(new BigDecimal("18000"));
            Assertions.assertThat(menu.getProducts()).contains(양념치킨_1개, 콜라_1개);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 메뉴의_이름값이_빈값이면_예외(String input) {
        MenuGroup 메뉴그룹_한마리 = 메뉴그룹_한마리();
        Assertions.assertThatThrownBy(() -> {
                Menu.of(input, Amount.of(10000), 메뉴그룹_한마리.getId());
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.MENU_NAME_IS_NOT_NULL.getMessage());
    }

}