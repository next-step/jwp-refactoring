package kitchenpos.menu.domain;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.helper.MenuProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 Domain 단위 테스트")
class MenuTest {

    @DisplayName("메뉴 가격은 총 금액을 넘을 수 없다.")
    @Test
    void checkAmount() {
        //given
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(MenuProductFixtures.양념치킨_메뉴);
        menuProducts.add(MenuProductFixtures.후라이드치킨_메뉴상품);
        Menu menu = 메뉴_만들기(1L,"반반치킨 세트", 50_000, null, menuProducts );

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(menu::checkAmount);
    }

    @DisplayName("메뉴 가격이 null 이거나 0원 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException().isThrownBy(() ->  메뉴_만들기("반반치킨 세트", -1000));
        assertThatIllegalArgumentException().isThrownBy(() ->  메뉴_만들기("반반치킨 세트", null));
    }

}
