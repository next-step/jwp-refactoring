package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.CreateMenuException;
import kitchenpos.exception.CreateMenuProductException;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NegativePriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A, 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A, 우아한_초밥_2.getId(), 2);

        A_우아한_초밥_1.mappedByMenu(A);
        A_우아한_초밥_2.mappedByMenu(A);

        menuGroupRepository.save(초밥_메뉴그룹);
        productRepository.save(우아한_초밥_1);
        productRepository.save(우아한_초밥_2);
        menuRepository.save(A);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create01() {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                BigDecimal.valueOf(30_000),
                초밥_메뉴그룹.getId(),
                Lists.newArrayList(
                        MenuProductRequest.of(A_우아한_초밥_1.getProductId(), A_우아한_초밥_1.findQuantity()),
                        MenuProductRequest.of(A_우아한_초밥_2.getProductId(), A_우아한_초밥_2.findQuantity()))
        );

        // when
        MenuResponse response = menuService.create(menuRequest);

        // then
        Menu findMenu = menuRepository.findById(response.getId()).get();
        assertThat(response).isEqualTo(MenuResponse.from(findMenu));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다. (null)")
    @ParameterizedTest
    @NullSource
    void create02(BigDecimal price) {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                price,
                초밥_메뉴그룹.getId(),
                Lists.newArrayList(
                        MenuProductRequest.of(A_우아한_초밥_1.getProductId(), A_우아한_초밥_1.findQuantity()),
                        MenuProductRequest.of(A_우아한_초밥_2.getProductId(), A_우아한_초밥_2.findQuantity()))
        );

        // when & then
        assertThatExceptionOfType(NegativePriceException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다. (0원 이하)")
    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void create03(long price) {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                BigDecimal.valueOf(price),
                초밥_메뉴그룹.getId(),
                Lists.newArrayList(
                        MenuProductRequest.of(A_우아한_초밥_1.getProductId(), A_우아한_초밥_1.findQuantity()),
                        MenuProductRequest.of(A_우아한_초밥_2.getProductId(), A_우아한_초밥_2.findQuantity()))
        );

        // when & then
        assertThatExceptionOfType(NegativePriceException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴는 반드시 메뉴 그룹에 포함되어야 한다.")
    @Test
    void create04() {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                BigDecimal.valueOf(30_000),
                0L,
                Lists.newArrayList(
                        MenuProductRequest.of(A_우아한_초밥_1.getProductId(), A_우아한_초밥_1.findQuantity()))
        );

        // when & then
        assertThatExceptionOfType(NotFoundMenuGroupException.class)
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴를 구성하는 상품이 없는 경우 메뉴로 등록할 수 없다.")
    @Test
    void create05() {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                BigDecimal.valueOf(30_000),
                초밥_메뉴그룹.getId(),
                Lists.newArrayList(
                        MenuProductRequest.of(0L, 0L))
        );

        // when & then
        assertThatExceptionOfType(CreateMenuProductException.class)
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 가격은 메뉴를 구성하는 상품 가격 * 수량의 합보다 클 수 없다")
    @Test
    void create06() {
        // given
        MenuRequest menuRequest = MenuRequest.of("A",
                BigDecimal.valueOf(10_000_000),
                초밥_메뉴그룹.getId(),
                Lists.newArrayList(
                        MenuProductRequest.of(A_우아한_초밥_1.getProductId(), A_우아한_초밥_1.findQuantity()))
        );

        // when & then
        assertThatExceptionOfType(MenuPriceException.class)
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).contains(MenuResponse.from(A));
    }
}
