package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.product.domain.Price;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static kitchenpos.product.domain.ProductTestFixture.상품;

@DisplayName("메뉴 테스트")
public class MenuTest {

    public static final BigDecimal MINUS_PRICE = BigDecimal.valueOf(-1);

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //gvien:
        final MenuName 이름 = MenuName.from("자메이카 통다리 1인 세트");
        final Price 가격 = Price.from(BigDecimal.ONE);
        final Long 메뉴_그룹_id = 1L;
        final MenuProductBag 메뉴_상품_목록 = MenuProductBag.from(Arrays.asList(
                메뉴_상품(상품("통다리").getId(), 1),
                메뉴_상품(상품("콜라").getId(), 1)
        ));
        //when, then:
        assertThat(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록)).isEqualTo(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록));
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 0보다 적은 경우")
    @Test
    void 생성_예외_메뉴의_가격이_0보다_적은_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> Menu.of(
                MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(MINUS_PRICE),
                1L,
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리").getId(), 1),
                        메뉴_상품(상품("콜라").getId(), 1)
                ))));
    }

    public static Menu 메뉴(MenuName name, Price price, Long menuGroupId, MenuProductBag menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}
