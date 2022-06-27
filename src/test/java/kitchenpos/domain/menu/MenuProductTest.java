package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.menu.application.MenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.exception.CreateMenuProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class MenuProductTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuValidator menuValidator;

    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private Product 상품_1;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(20_000));

        메뉴그룹 = menuGroupRepository.save(메뉴그룹);
        상품_1 = productRepository.save(상품_1);

        메뉴 = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(20_000), 메뉴그룹.getId());
        메뉴 = menuRepository.save(메뉴);
    }

    @DisplayName("MenuProduct를 생성할 수 있다. (Menu, Product, Quantity)")
    @Test
    void create01() {
        // when
        MenuProduct menuProduct = MenuProduct.of(상품_1.getId(), 1L);
        메뉴.appendMenuProduct(menuProduct);

        // then
        assertAll(
                () -> assertEquals(메뉴.getId(), menuProduct.getMenuId()),
                () -> assertEquals(1L, menuProduct.findQuantity()),
                () -> assertEquals(상품_1.getId(), menuProduct.getProductId())
        );
    }

    @DisplayName("MenuProduct 생성 시 Product가 존재하지 않으면 생성할 수 없다.")
    @Test
    void create02() {
        // given
        Menu menu = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(20_000), 메뉴그룹.getId());
        MenuProduct menuProduct = MenuProduct.of(0L, 1L);
        menu.appendMenuProduct(menuProduct);

        // when & then
        assertThrows(CreateMenuProductException.class, () -> menuValidator.validate(menu));
    }

    @DisplayName("구성하고 있는 상품 정보를 바탕으로 총 금액을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 20000", "2, 40000", "3, 60000"})
    void calculate01(long quantity, long price) {
        // given
        MenuProduct menuProduct = MenuProduct.of(상품_1.getId(), quantity);

        // when
        BigDecimal totalPrice = menuProduct.calculateTotalPrice(상품_1.getPrice());

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(price));
    }
}