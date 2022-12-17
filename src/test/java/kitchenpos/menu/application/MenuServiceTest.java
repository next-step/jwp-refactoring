package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.common.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.menu.application.MenuService.MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE;
import static kitchenpos.menu.application.MenuService.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.Menu.MENU_PRICE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MenuService")
@SpringBootTest
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("A"));
        Product product = productRepository.save(new Product(new Name("A"), new Price(BigDecimal.valueOf(2))));
        menuProduct = menuProductRepository.save(new MenuProduct(null, product, 1L));
        Menu menu = menuRepository.save(new Menu(new Name("A"), new Price(BigDecimal.valueOf(2)), menuGroup, Arrays.asList(menuProduct)));
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("가격을 필수값으로 갖는다.")
    @ParameterizedTest
    @NullSource
    void create_fail_MenuGroupNull(BigDecimal price) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts, menuGroup.getId(), price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격은 0원보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1"})
    void create_fail_minimumPrice(BigDecimal price) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts, menuGroup.getId(), price, "A")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_fail_menuGroup(BigDecimal price) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts, null, price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"3"})
    void create_fail_priceSum(BigDecimal price) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(menuProducts, menuGroup.getId(), price, "menuA")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_PRICE_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_success(BigDecimal price) {
        String name = "menuA";
        MenuResponse response = menuService.create(new MenuCreateRequest(Collections.singletonList(menuProduct), menuGroup.getId(), price, name));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(name),
                () -> assertEquals(0, response.getPrice().compareTo(price)),
                () -> assertThat(response.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        assertThat(menuService.list()).hasSize(1);
    }
}
