package kitchenpos.menu.service;

import kitchenpos.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menugroup.application.MenuGroupServiceTest.createMenuGroup01;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    public static final String MENU_NAME01 = "바베큐치킨";
    public static final BigDecimal MENU_PRICE01 = new BigDecimal(30000);

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(ProductServiceTest.createProduct01()));
        when(menuRepository.save(any())).thenReturn(createMenu01());

        // when
        Menu menu = menuService.create(new Menu(MENU_NAME01, MENU_PRICE01, createMenuGroup01(), createMenuProductList()));

        // then
        assertThat(menu).isNotNull();
    }

    @DisplayName("[예외] 가격 없이 메뉴를 생성한다.")
    @Test
    public void create_price_null() {
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new Menu(MENU_NAME01, null, createMenuGroup01(), createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 0원 미만으로 메뉴를 생성한다.")
    @Test
    public void create_price_under_zero() {
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new Menu(MENU_NAME01, new BigDecimal(-1), createMenuGroup01(), createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴 그룹을 포함하지 않은 메뉴를 생성한다.")
    @Test
    public void create_without_menu_group() {
        when(menuGroupRepository.existsById(any())).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new Menu(MENU_NAME01, MENU_PRICE01, createMenuGroup01(), createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 상품을 포함하지 않은 메뉴를 생성한다")
    @Test
    public void create_without_product() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new Menu(MENU_NAME01, MENU_PRICE01, createMenuGroup01(), createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴 상품보다 가격이 비싼 메뉴를 생성한다.")
    @Test
    public void create_expensive_than_menu_products() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(ProductServiceTest.createProduct01()));

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new Menu(MENU_NAME01, new BigDecimal(1000000), createMenuGroup01(), createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        when(menuRepository.findAll()).thenReturn(createMenuList());

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list).isNotNull();
    }

    public static Menu createMenu01() {
        MenuGroup menuGroup = createMenuGroup01();
        Product product = ProductServiceTest.createProduct01();
        MenuProduct menuProduct = new MenuProduct(product, 1);
        return new Menu(MENU_NAME01, MENU_PRICE01, menuGroup, Collections.singletonList(menuProduct));
    }

    public static MenuProduct createMenuProduct01() {
        Product product = ProductServiceTest.createProduct01();
        Menu menu = MenuServiceTest.createMenu01();
        return new MenuProduct(1L, menu, product, 1);
    }

    public static List<MenuProduct> createMenuProductList() {
        return Collections.singletonList(createMenuProduct01());
    }

    public static List<Menu> createMenuList() {
        return Collections.singletonList(createMenu01());
    }
}
