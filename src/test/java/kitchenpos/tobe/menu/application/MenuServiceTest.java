package kitchenpos.tobe.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.fixture.MenuFixture;
import kitchenpos.tobe.fixture.MenuGroupFixture;
import kitchenpos.tobe.fixture.MenuProductFixture;
import kitchenpos.tobe.fixture.MenuProductsFixture;
import kitchenpos.tobe.fixture.ProductFixture;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuGroup;
import kitchenpos.tobe.menu.domain.MenuProducts;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.menu.dto.MenuRequest;
import kitchenpos.tobe.menu.dto.MenuResponse;
import kitchenpos.tobe.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private Validator<Menu> menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void register() {
        // given
        final Product product1 = ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(7_000L));
        final Product product2 = ProductFixture.of(2L, "사이다", BigDecimal.valueOf(3_000L));
        final MenuProducts menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(product1.getId(), 7_000L, 1L),
            MenuProductFixture.of(product2.getId(), 3_000L, 1L)
        );
        final MenuGroup menuGroup = MenuGroupFixture.of(1L, "신메뉴");
        final Menu expected = MenuFixture.of(
            1L,
            "후라이드치킨",
            3_000L,
            menuProducts,
            menuGroup.getId(),
            menuValidator
        );
        given(menuRepository.save(any(Menu.class))).willReturn(expected);

        final MenuRequest request = MenuFixture.ofRequest(
            "후라이드치킨",
            3_000L,
            Arrays.asList(
                MenuProductFixture.ofRequest(1L, 1_000L, 1L),
                MenuProductFixture.ofRequest(2L, 2_000L, 1L)
            ),
            1L
        );

        // when
        final MenuResponse response = menuService.register(request);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull()
        );
    }

    @DisplayName("메뉴를 등록할 수 없다.")
    @Nested
    class RegisterFailTest {

        @DisplayName("메뉴 이름이 null 또는 \"\"일 경우")
        @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
        @NullAndEmptySource
        void invalidName(final String name) {
            // given
            final MenuRequest request = MenuFixture.ofRequest(
                name,
                3_000L,
                Arrays.asList(
                    MenuProductFixture.ofRequest(1L, 1_000L, 1L),
                    MenuProductFixture.ofRequest(2L, 2_000L, 1L)
                ),
                1L
            );

            // when
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 null 또는 0보다 작을 경우")
        @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
        @NullSource
        @ValueSource(strings = {"-16000"})
        void invalidPrice(final BigDecimal price) {
            // given
            final MenuRequest request = MenuFixture.ofRequest(
                "후라이드치킨",
                price,
                Arrays.asList(
                    MenuProductFixture.ofRequest(1L, 1_000L, 1L),
                    MenuProductFixture.ofRequest(2L, 2_000L, 1L)
                ),
                1L
            );

            // when
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 포함되지 않은 경우")
        @Test
        void emptyMenuProducts() {
            // given
            final MenuRequest request = MenuFixture.ofRequest(
                "후라이드치킨",
                3_000L,
                Collections.emptyList(),
                1L
            );

            // when
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품 수량이 0보다 작거나 같은 경우")
        @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
        @ValueSource(longs = {0L, Long.MIN_VALUE})
        void invalidQuantity(final long quantity) {
            // given
            final MenuRequest request = MenuFixture.ofRequest(
                "후라이드치킨",
                3_000L,
                Arrays.asList(
                    MenuProductFixture.ofRequest(1L, 1_000L, quantity),
                    MenuProductFixture.ofRequest(2L, 2_000L, quantity)
                ),
                1L
            );

            // when
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("등록된 메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final MenuProducts menuProducts1 = MenuProductsFixture.of(
            MenuProductFixture.of(1L, 1_000L, 1L),
            MenuProductFixture.of(2L, 2_000L, 1L)
        );
        final MenuProducts menuProducts2 = MenuProductsFixture.of(
            MenuProductFixture.of(3L, 3_000L, 1L),
            MenuProductFixture.of(4L, 4_000L, 1L)
        );
        final List<Menu> expected = Arrays.asList(
            MenuFixture.of("후라이드치킨", 3_000L, menuProducts1, 1L, menuValidator),
            MenuFixture.of("양념치킨", 7_000L, menuProducts2, 1L, menuValidator)
        );
        given(menuRepository.findAll()).willReturn(expected);

        // when
        final List<MenuResponse> response = menuService.list();

        // then
        assertThat(response.size()).isEqualTo(2);
    }
}
