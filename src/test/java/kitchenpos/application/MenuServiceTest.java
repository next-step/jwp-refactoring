package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product 후라이드 = ProductFixture.of(1L, "후라이드", BigDecimal.valueOf(8_000));
        final Product 양념치킨 = ProductFixture.of(2L, "양념치킨", BigDecimal.valueOf(8_000));
        final List<MenuProduct> menuProducts = Arrays.asList(
            MenuFixture.ofMenuProduct(1L, 1L, 1L),
            MenuFixture.ofMenuProduct(2L, 2L, 1L)
        );
        final Menu request = MenuFixture.ofCreateRequest(
            "반반치킨",
            BigDecimal.valueOf(16_000),
            2L,
            menuProducts
        );
        final Menu expected = MenuFixture.of(
            1L,
            "반반치킨",
            BigDecimal.valueOf(16_000),
            2L,
            menuProducts
        );

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(후라이드.getId())).willReturn(Optional.of(후라이드));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(menuDao.save(any(Menu.class))).willReturn(expected);

        // when
        final Menu actual = menuService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
            () -> assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId()),
            () -> assertThat(actual.getMenuProducts())
                .containsExactlyElementsOf(expected.getMenuProducts())
        );
    }


    @DisplayName("메뉴를 등록할 수 없다.")
    @Nested
    class CreateFailTest {

        @DisplayName("메뉴 가격이 null 이거나 0보다 작은 경우")
        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-16000"})
        void create_fail_invalidPrice(final BigDecimal price) {
            // given
            final Menu request = MenuFixture.ofCreateRequest(
                "후라이드치킨",
                price,
                1L,
                Collections.emptyList()
            );

            // when
            ThrowableAssert.ThrowingCallable actual = () -> menuService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 등록되어 있지 않은 경우")
        @Test
        void create_fail_noSuchMenuGroup() {
            // given
            final Menu request = MenuFixture.ofCreateRequest(
                "후라이드치킨",
                BigDecimal.valueOf(16_000),
                2L,
                Collections.emptyList()
            );

            // when
            ThrowableAssert.ThrowingCallable actual = () -> menuService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 등록되어 있지 않은 경우")
        @Test
        void create_fail_noSuchMenuProduct() {
            // given
            final Menu request = MenuFixture.ofCreateRequest(
                "후라이드치킨",
                BigDecimal.valueOf(16_000),
                2L,
                Collections.emptyList()
            );
            given(menuGroupDao.existsById(anyLong())).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable actual = () -> menuService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 메뉴 상품 금액의 합보다 큰 경우")
        @Test
        void create_fail_invalidTotalPrice() {
            // given
            final Product 후라이드 = ProductFixture.of(1L, "후라이드", BigDecimal.valueOf(8_000));
            final Product 양념치킨 = ProductFixture.of(2L, "양념치킨", BigDecimal.valueOf(8_000));
            final List<MenuProduct> menuProducts = Arrays.asList(
                MenuFixture.ofMenuProduct(1L, 1L, 1L),
                MenuFixture.ofMenuProduct(2L, 2L, 1L)
            );
            final Menu request = MenuFixture.ofCreateRequest(
                "반반치킨",
                BigDecimal.valueOf(160_000),
                2L,
                menuProducts
            );

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(후라이드.getId())).willReturn(Optional.of(후라이드));
            given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));

            // when
            ThrowableAssert.ThrowingCallable actual = () -> menuService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<Menu> expected = Arrays.asList(
            MenuFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 2L, Collections.emptyList()),
            MenuFixture.of(2L, "양념치킨", BigDecimal.valueOf(16_000), 2L, Collections.emptyList())
        );
        given(menuDao.findAll()).willReturn(expected);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
