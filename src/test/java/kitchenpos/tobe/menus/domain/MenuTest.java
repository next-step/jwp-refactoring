package kitchenpos.tobe.menus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.fixture.MenuFixture;
import kitchenpos.tobe.fixture.MenuProductFixture;
import kitchenpos.tobe.fixture.MenuProductsFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    private Validator<Menu> validator;

    @BeforeEach
    void setUp() {
        validator = new FakeMenuValidator();
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        final String name = "반반치킨";
        final long price = 14_000L;
        final MenuProducts menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(1L, 8_000L, 1L),
            MenuProductFixture.of(2L, 6_000L, 1L)
        );
        final Long menuGroupId = 1L;

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuFixture.of(
            name,
            price,
            menuProducts,
            menuGroupId,
            validator
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("메뉴 가격이 구성된 메뉴 상품들 가격의 총 합보다 작거나 같은지 검증할 수 있다.")
    @Test
    void isNotOverpriced() {
        // given
        final String name = "반반치킨";
        final long price = 14_000L;
        final MenuProducts menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(1L, 8_000L, 1L),
            MenuProductFixture.of(2L, 6_000L, 1L)
        );
        final Long menuGroupId = 1L;
        final Menu menu = MenuFixture.of(name, price, menuProducts, menuGroupId, validator);

        // when
        final boolean isOverpriced = menu.isOverpriced();

        // then
        assertThat(isOverpriced).isFalse();
    }

    @DisplayName("메뉴 가격이 구성된 메뉴 상품들 가격의 총 합을 초과하는지 검증할 수 있다.")
    @Test
    void isOverpriced() {
        // given
        final String name = "반반치킨";
        final long price = Long.MAX_VALUE;
        final MenuProducts menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(1L, 8_000L, 1L),
            MenuProductFixture.of(2L, 6_000L, 1L)
        );
        final Long menuGroupId = 1L;
        final Menu menu = MenuFixture.of(name, price, menuProducts, menuGroupId, validator);

        // when
        final boolean isOverpriced = menu.isOverpriced();

        // then
        assertThat(isOverpriced).isTrue();
    }
}
