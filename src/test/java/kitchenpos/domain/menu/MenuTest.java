package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;

class MenuTest {

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
    }

    @DisplayName("Menu 는 Name, Price, MenuGroup, MenuProducts 로 생성된다.")
    @Test
    void create1() {
        // given
        BigDecimal price = BigDecimal.valueOf(9_000);

        // when
        Menu menu = Menu.of("불고기", price, 고기_메뉴그룹);
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(돼지고기, 1L),
                                                       MenuProduct.of(공기밥, 1L));
        menu.addMenuProducts(menuProducts);

        // then
        assertAll(
            () -> assertEquals(menu.getName(), Name.from("불고기")),
            () -> assertEquals(menu.getMenuGroup(), 고기_메뉴그룹),
            () -> assertEquals(menu.getPrice(), Price.from(price))
        );
    }

    @DisplayName("Menu 생성 시, MenuGroup 이 null 이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create2(MenuGroup menuGroup) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Menu.of("불고기", BigDecimal.valueOf(9_000), null))
                                            .withMessageContaining("Menu 는 MenuGroup 가 필수값 입니다.");
    }

    @DisplayName("Menu 생성 시, Price 가 null 이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create2(BigDecimal price) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Menu.of("불고기", price, 고기_메뉴그룹))
                                            .withMessageContaining("Menu 는 Price 가 필수값 입니다.");
    }

    @DisplayName("Menu 의 가격은 구성하는 상품의 총 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void validateMenuPrice() {
        // given
        BigDecimal price = BigDecimal.valueOf(1_000_000);
        Menu menu = Menu.of("불고기", price, 고기_메뉴그룹);
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(돼지고기, 1L),
                                                       MenuProduct.of(공기밥, 1L));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menu.addMenuProducts(menuProducts))
                                            .withMessageContaining("Menu Price 는 상품 가격 총합보다 작아야합니다.");
    }
}