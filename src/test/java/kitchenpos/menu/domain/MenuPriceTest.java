package kitchenpos.menu.domain;

import static kitchenpos.helper.MenuProductFixtures.메뉴_상품_만들기;
import static kitchenpos.helper.ProductFixtures.상품_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 가격 관련 Domain 단위 테스트")
class MenuPriceTest {

    @DisplayName("메뉴가격이 금액을 초과 했는지 확인한다.")
    @Test
    void overTo() {
        //given
        MenuProducts menuProducts1 = new MenuProducts();
        menuProducts1.add(메뉴_상품_만들기(3, 상품_만들기("감자튀김", 5000)));
        menuProducts1.add(메뉴_상품_만들기(1, 상품_만들기("콜라2L", 3000)));

        MenuProducts menuProducts2 = new MenuProducts();
        menuProducts2.add(메뉴_상품_만들기(1, 상품_만들기("햄버거", 20000)));
        menuProducts2.add(메뉴_상품_만들기(1, 상품_만들기("피클", 5000)));

        //when
        MenuPrice menuPrice = new MenuPrice(20000);

        //then
        assertThat(menuPrice.overTo(menuProducts1)).isTrue();
        assertThat(menuPrice.overTo(menuProducts2)).isFalse();
    }

    @DisplayName("메뉴가격은 0원미만 or null 일 수 없다.")
    @Test
    void validate() {

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuPrice(-1000));
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuPrice(null));
    }
}
