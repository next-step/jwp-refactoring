package kitchenpos.domain.menu;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.application.menu.MenuValidator;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.exception.EmptyNameException;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NegativePriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuValidator menuValidator;

    private MenuGroup 메뉴그룹;
    private Product 상품_1;
    private Product 상품_2;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(10_000));
        상품_2 = ProductFixtureFactory.create("상품_2", BigDecimal.valueOf(20_000));

        menuGroupRepository.save(메뉴그룹);
        productRepository.save(상품_1);
        productRepository.save(상품_2);
    }

    @DisplayName("Menu를 생성할 수 있다.(Name, Price, MenuGroup, MenuProducts")
    @Test
    void create01() {
        // given
        List<MenuProduct> menuProducts = Lists.newArrayList(
                MenuProduct.of(상품_1.getId(), 1L),
                MenuProduct.of(상품_2.getId(), 1L)
        );

        // when
        Menu menu = Menu.of("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹.getId());
        menu.appendAllMenuProducts(menuProducts);

        // then
        assertAll(
                () -> assertEquals("메뉴", menu.findName()),
                () -> assertEquals(BigDecimal.valueOf(30_000), menu.findPrice()),
                () -> assertEquals(메뉴그룹.getId(), menu.getMenuGroupId()),
                () -> assertEquals(menuProducts, menu.findMenuProducts())
        );
    }

    @DisplayName("Menu 생성 시 MenuGroup이 없으면 생성할 수 없다.")
    @Test
    void create02() {
        // given
        Menu 메뉴 = Menu.of("메뉴", BigDecimal.valueOf(30_000), 0L);

        // when & then
        assertThrows(NotFoundMenuGroupException.class, () -> menuValidator.validate(메뉴));
    }

    @DisplayName("Menu 생성 시 Name이 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create03(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> Menu.of(name, BigDecimal.valueOf(30_000), 메뉴그룹.getId()));
    }

    @DisplayName("Menu 생성 시 Price가 없으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create04(BigDecimal price) {
        // when & then
        assertThrows(NegativePriceException.class, () -> Menu.of("메뉴", price, 메뉴그룹.getId()));
    }

    @DisplayName("Menu 생성 시 Menu의 Price는 구성하는 Product의 Price 총 합보다 클 수 없다.")
    @Test
    void create05() {
        // given
        BigDecimal menuPrice = BigDecimal.valueOf(10_000_000);
        List<MenuProduct> menuProducts = Lists.newArrayList(
                MenuProduct.of(상품_1.getId(), 1L),
                MenuProduct.of(상품_2.getId(), 1L)
        );

        Menu 메뉴 = Menu.of("메뉴", menuPrice, 메뉴그룹.getId());
        메뉴.appendAllMenuProducts(menuProducts);

        // when & then
        assertThrows(MenuPriceException.class, () -> menuValidator.validate(메뉴));
    }
}