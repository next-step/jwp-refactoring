package kitchenpos.menu.domain;

import static common.MenuGroupFixture.메뉴그룹_한마리;
import static common.MenuProductFixture.양념치킨_1개;
import static common.MenuProductFixture.콜라_1개;
import static java.util.Arrays.asList;

import kitchenpos.product.domain.Amount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuTest {


    /**
     *  양념치킨 가격 16000
     *  콜라 1개 가격 2000
     */
    @Test
    void 메뉴생성시_등록한_상품의합보다_큰_금액을_입력시_예외() {
        Menu menu = Menu.of("양념치킨", Amount.of(20000), 메뉴그룹_한마리());
        MenuProduct 양념치킨_1개 = 양념치킨_1개(menu);
        MenuProduct 콜라_1개 = 콜라_1개(menu);
        Assertions.assertThatThrownBy(() -> {
            menu.withMenuProducts(asList(양념치킨_1개, 콜라_1개));
        }).isInstanceOf(IllegalArgumentException.class);
    }

}