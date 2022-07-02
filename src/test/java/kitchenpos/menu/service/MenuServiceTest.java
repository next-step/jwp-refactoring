package kitchenpos.menu.service;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
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

import static kitchenpos.menu.service.MenuGroupServiceTest.createMenuGroup01;
import static kitchenpos.product.application.ProductServiceTest.createProduct1;
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

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup(1L)));
        when(menuRepository.save(any())).thenReturn(createMenu01());

        // when
        MenuResponse menuResponse = menuService.create(new MenuRequest(MENU_NAME01, MENU_PRICE01, 1L, createMenuProductList()));

        // then
        assertThat(menuResponse).isNotNull();
    }

    @DisplayName("[예외] 가격 없이 메뉴를 생성한다.")
    @Test
    public void create_price_null() {
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequest(MENU_NAME01, null, 1L, createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 0원 미만으로 메뉴를 생성한다.")
    @Test
    public void create_price_under_zero() {
        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequest(MENU_NAME01, new BigDecimal(-1), 1L, createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴 그룹을 포함하지 않은 메뉴를 생성한다.")
    @Test
    public void create_without_menu_group() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequest(MENU_NAME01, MENU_PRICE01, 1L, createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 상품을 포함하지 않은 메뉴를 생성한다")
    @Test
    public void create_without_product() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup(1L)));

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequest(MENU_NAME01, MENU_PRICE01, 1L, Collections.singletonList(createMenuWithoutProduct())));
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("[예외] 메뉴 상품보다 가격이 비싼 메뉴를 생성한다.")
    @Test
    public void create_expensive_than_menu_products() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup(1L)));

        // when, then
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequest(MENU_NAME01, new BigDecimal(1000000), 1L, createMenuProductList()));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        when(menuRepository.findAll()).thenReturn(createMenuList());

        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list).isNotNull();
    }

    public static Menu createMenu01() {
        MenuGroup menuGroup = createMenuGroup01();
        Product product = createProduct1();
        MenuProduct menuProduct = new MenuProduct(product, 1);
        return new Menu(MENU_NAME01, MENU_PRICE01, menuGroup, Collections.singletonList(menuProduct));
    }

    public static MenuProduct createMenuProduct01() {
        Product product = createProduct1();
        Menu menu = MenuServiceTest.createMenu01();
        return new MenuProduct(1L, menu, product, 1);
    }

    public static MenuProduct createMenuWithoutProduct() {
        Menu menu = MenuServiceTest.createMenu01();
        return new MenuProduct(1L, menu, null, 1);
    }

    public static List<MenuProduct> createMenuProductList() {
        return Collections.singletonList(createMenuProduct01());
    }

    public static List<Menu> createMenuList() {
        return Collections.singletonList(createMenu01());
    }
}
