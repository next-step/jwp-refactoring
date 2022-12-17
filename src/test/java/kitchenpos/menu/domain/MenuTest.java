package kitchenpos.menu.domain;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.product.domain.PriceTest.MINUS_PRICE;
import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 테스트")
public class MenuTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //gvien:
        final Name 이름 = Name.from("자메이카 통다리 1인 세트");
        final Price 가격 = Price.from(BigDecimal.ONE);
        final Long 메뉴_그룹_id = 1L;
        final MenuProductBag 메뉴_상품_목록 = MenuProductBag.from(Arrays.asList(
                MenuProduct.of(상품("통다리"), 1),
                MenuProduct.of(상품("콜라"), 1)
        ));
        //when, then:
        assertThat(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록)).isEqualTo(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록));
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 0보다 적은 경우")
    @Test
    void 생성_예외_메뉴의_가격이_0보다_적은_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> Menu.of(
                Name.from("자메이카 통다리 1인 세트"),
                Price.from(MINUS_PRICE),
                1L,
                MenuProductBag.from(Arrays.asList(
                        MenuProduct.of(상품("통다리"), 1),
                        MenuProduct.of(상품("콜라"), 1)
                ))));
    }

    public static Menu 메뉴(Name name, Price price, Long menuGroupId, MenuProductBag menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}
