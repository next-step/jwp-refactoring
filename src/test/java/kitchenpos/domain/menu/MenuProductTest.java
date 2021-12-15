package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;

class MenuProductTest {

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Menu 불고기;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 9_000, 고기_메뉴그룹);
    }

    @DisplayName("MenuProduct 는 Menu, Product, Quantity 로 생성된다.")
    @Test
    void create1() {
        // when
        MenuProduct 불고기_돼기고기 = MenuProduct.of(돼지고기, 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼기고기));

        // then
        assertAll(
            () -> assertEquals(불고기_돼기고기.getMenu().getId(), 불고기.getId()),
            () -> assertEquals(불고기_돼기고기.getProduct().getId(), 돼지고기.getId()),
            () -> assertEquals(불고기_돼기고기.getQuantity(), Quantity.from(1L))
        );
    }

    @DisplayName("MenuProduct 생성 시, Menu 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        assertThatIllegalArgumentException().isThrownBy(() -> MenuProduct.of(1L, null, 돼지고기, 1L))
                                            .withMessageContaining("Menu 가 존재하지 않습니다.");
    }

    @DisplayName("MenuProduct 생성 시, Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // when & then
        assertThrows(EntityNotFoundException.class, () -> MenuProduct.of(null, 1L));
    }

    @DisplayName("MenuProduct 는 자신의 총 합산 금액을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 9000", "2, 18000", "10, 90000",})
    void calculateTotalPrice(long quantity, long totalPrice) {
        // when
        MenuProduct 불고기_돼기고기 = MenuProduct.of(돼지고기, quantity);
        불고기.addMenuProducts(Arrays.asList(불고기_돼기고기));

        // then
        assertThat(불고기_돼기고기.calculateTotalPrice()).isEqualTo(BigDecimal.valueOf(totalPrice));
    }
}
