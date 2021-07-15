package kitchenpos.menu.applicattion;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.IllegalPriceException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private List<MenuProduct> menuProducts;
    private Product product1;
    private Product product2;
    private Menu menu1;
    private Menu menu2;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuProduct1 = new MenuProduct(1L, 1L, 1L, 1);
        menuProduct2 = new MenuProduct(2L, 1L, 2L, 2);
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        product1 = new Product(1L, "신메뉴1", BigDecimal.valueOf(20000));
        product2 = new Product(2L, "신메뉴2", BigDecimal.valueOf(10000));
        menu1 = new Menu("신메뉴1", BigDecimal.valueOf(20000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts);
        menu2 = new Menu("신메뉴2", BigDecimal.valueOf(30000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts);
        menuGroup = new MenuGroup(1L, "그룹1");
    }

    @DisplayName("메뉴를 등록한다. (메뉴 상품(MenuProduct) 리스트에도 메뉴를 등록한다.)")
    @Test
    void create() {
        MenuRequest menu = new MenuRequest("후라이드2마리", 20000L, 1L, menuProducts);
        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findAllById(anyList())).willReturn(Arrays.asList(product1, product2));
        given(menuRepository.save(any())).willReturn(new Menu(menu.getName(),
                BigDecimal.valueOf(menu.getPrice()), new MenuGroup(1L, "그룹1"), 30000L, menuProducts));

        MenuResponse savedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(savedMenu.getMenuProducts()).contains(menuProduct1, menuProduct2));

        verify(menuGroupRepository, times(1)).findById(any());
        verify(productRepository, times(1)).findAllById(anyList());
        verify(menuRepository, times(1)).save(any());
    }

    @DisplayName("메뉴를 등록에 실패한다 - 메뉴 그룹 아이디가 등록되어 있지 않은 경우")
    @Test
    void fail_create1() {
        MenuRequest menu = new MenuRequest("후라이드2마리", 20000L, 1L, menuProducts);
        Product product = new Product(1L, "신메뉴", BigDecimal.valueOf(20000L));
        given(productRepository.findAllById(anyList())).willReturn(Arrays.asList(product));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);

        verify(productRepository, times(1)).findAllById(anyList());
    }

    @DisplayName("메뉴를 등록에 실패한다 - 메뉴 등록시 메뉴 상품들의 총 가격(상품 * 수량의 총합) 보다 클 수 없다.")
    @Test
    void fail_create2() {
        MenuRequest menu = new MenuRequest("후라이드2마리", 20000L, 1L, menuProducts);
        Product product = new Product(1L, "신메뉴", BigDecimal.valueOf(2000L));
        given(productRepository.findAllById(anyList())).willReturn(Arrays.asList(product));
        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(new MenuGroup(1L, "그룹1")));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalPriceException.class);

        verify(productRepository, times(1)).findAllById(anyList());
        verify(menuGroupRepository, times(1)).findById(anyLong());
    }

    @DisplayName("메뉴 리스트를 조회한다. (메뉴 조회시 메뉴에 대한 수량 정보도 같이 가져온다.)")
    @Test
    void list() {
        List<MenuProduct> menuProducts1 = Arrays.asList(new MenuProduct(1L, 1L, 1L, 1L));
        List<MenuProduct> menuProducts2 = Arrays.asList(new MenuProduct(2L, 2L, 2L, 1L));
        List<MenuProduct> menuProducts3 = Arrays.asList(new MenuProduct(3L, 3L, 3L, 1L));
        Menu menu1 = new Menu("메뉴1", BigDecimal.valueOf(15000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts1);
        Menu menu2 = new Menu("메뉴2", BigDecimal.valueOf(17000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts2);
        Menu menu3 = new Menu("메뉴3", BigDecimal.valueOf(15000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts3);
        List<Menu> menus = Arrays.asList(menu1, menu2, menu3);
        given(menuRepository.findAll()).willReturn(menus);

        List<MenuResponse> selectedMenus = menuService.list();

        assertAll(
                () -> assertThat(selectedMenus.get(0).getName()).isEqualTo(menus.get(0).getName()),
                () -> assertThat(selectedMenus.get(0).getMenuProducts()).isEqualTo(menuProducts1),
                () -> assertThat(selectedMenus.get(1).getMenuProducts()).isEqualTo(menuProducts2),
                () -> assertThat(selectedMenus.get(2).getMenuProducts()).isEqualTo(menuProducts3));

        verify(menuRepository, times(1)).findAll();
    }
}
