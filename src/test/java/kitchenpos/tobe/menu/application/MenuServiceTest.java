package kitchenpos.tobe.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.tobe.fixture.MenuFixture;
import kitchenpos.tobe.fixture.MenuGroupFixture;
import kitchenpos.tobe.fixture.MenuProductFixture;
import kitchenpos.tobe.fixture.MenuProductsFixture;
import kitchenpos.tobe.fixture.ProductFixture;
import kitchenpos.tobe.menu.domain.FakeMenuValidator;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuGroup;
import kitchenpos.tobe.menu.domain.MenuGroupRepository;
import kitchenpos.tobe.menu.domain.MenuProducts;
import kitchenpos.tobe.menu.domain.MenuRegisterValidator;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.menu.domain.MenuValidator;
import kitchenpos.tobe.menu.dto.MenuRequest;
import kitchenpos.tobe.menu.dto.MenuResponse;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.domain.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    private MenuRegisterValidator menuValidator;

    private MenuValidator fakeMenuValidator;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuRegisterValidator(menuGroupRepository, productRepository);
        fakeMenuValidator = new FakeMenuValidator();
        menuService = new MenuService(menuValidator, menuRepository);
    }

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
            fakeMenuValidator
        );
        given(menuRepository.save(any(Menu.class))).willReturn(expected);
        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        given(productRepository.findAllByIdIn(anyList()))
            .willReturn(Arrays.asList(product1, product2));

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

        @DisplayName("메뉴 가격이 구성된 메뉴 상품들 가격의 총 합을 초과할 경우")
        @Test
        void overpriced() {
            // given
            final MenuRequest request = MenuFixture.ofRequest(
                "후라이드치킨",
                Long.MAX_VALUE,
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

        @DisplayName("메뉴 상품이 등록되어 있지 않은 경우")
        @Test
        void noSuchMenuProduct() {
            // given
            final MenuGroup menuGroup = MenuGroupFixture.of(1L, "신메뉴");
            given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));

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
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(NoSuchElementException.class);
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

        @DisplayName("메뉴 그룹이 등록되어 있지 않은 경우")
        @Test
        void noSuchMenuGroup() {
            // given
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
            ThrowableAssert.ThrowingCallable response = () -> menuService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(NoSuchElementException.class);
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
            MenuFixture.of("후라이드치킨", 3_000L, menuProducts1, 1L, fakeMenuValidator),
            MenuFixture.of("양념치킨", 7_000L, menuProducts2, 1L, fakeMenuValidator)
        );
        given(menuRepository.findAll()).willReturn(expected);

        // when
        final List<MenuResponse> response = menuService.list();

        // then
        assertThat(response.size()).isEqualTo(2);
    }
}
