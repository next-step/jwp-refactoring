package kitchenpos.menu;

import kitchenpos.old.ui.application.MenuService;
import kitchenpos.old.ui.dao.MenuDao;
import kitchenpos.menugroup.MenuGroupDao;
import kitchenpos.old.ui.dao.MenuProductDao;
import kitchenpos.product.ProductDao;
import kitchenpos.old.ui.domain.Menu;
import kitchenpos.old.ui.domain.MenuProduct;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    public static final long PRODUCT_COFFE_ID = 2l;
    public static final long PRODUC_CHICKEN_ID = 1l;
    public static final long MENU_ID = 999l;
    public static final long MENU_GROUP_ID = 1l;
    public static final long INVALID_MENU_GROUP_ID = 2l;
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

    @DisplayName("생성 테스트")
    @Test
    public void create() {
        메뉴_그룹_등록됨();
        치킨_등록됨();
        커피_등록됨();
        메뉴_상품_모킹됨();

        MenuProduct 치킨_메뉴_상품 = 치킨_메뉴_상품();
        MenuProduct 커피_메뉴_상품 = 커피_메뉴_상품();

        Menu mockMenu = 메뉴_모킹됨();

        Menu creatingMenu = new Menu();
        creatingMenu.setMenuGroupId(MENU_GROUP_ID);
        creatingMenu.setPrice(new BigDecimal(3000));
        creatingMenu.setMenuProducts(new ArrayList<MenuProduct>() {{
            add(치킨_메뉴_상품);
            add(커피_메뉴_상품);
        }});

        Menu result = menuService.create(creatingMenu);

        assertThat(result).isEqualTo(mockMenu);
        assertThat(치킨_메뉴_상품.getMenuId()).isEqualTo(MENU_ID);
        assertThat(커피_메뉴_상품.getMenuId()).isEqualTo(MENU_ID);
    }

    @DisplayName("메뉴 생성 불가능 케이스 1 - 금액이 올바르지 않은 경우")
    @Test
    public void invalidCase1() {
        Menu creatingMenu = new Menu();
        creatingMenu.setPrice(new BigDecimal(-1000));
        assertThatThrownBy(() -> {
            menuService.create(creatingMenu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 불가능 케이스 2 - 그룹 ID가 올바르지 않은 경우")
    @Test
    public void invalidCase2() {
        Menu creatingMenu = new Menu();
        creatingMenu.setPrice(new BigDecimal(1000));
        creatingMenu.setMenuGroupId(INVALID_MENU_GROUP_ID);
        assertThatThrownBy(() -> {
            menuService.create(creatingMenu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 불가능 케이스 3 - 상품의 ID가 올바르지 않은 경우")
    @Test
    public void invalidCase3() {
        Menu creatingMenu = new Menu();
        creatingMenu.setPrice(new BigDecimal(1000));
        creatingMenu.setMenuGroupId(MENU_GROUP_ID);
        creatingMenu.setMenuProducts(new ArrayList<MenuProduct>() {{
            add(존재하지않는_메뉴_상품());
        }});
        assertThatThrownBy(() -> {
            menuService.create(creatingMenu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 불가능 케이스 4 - 메뉴의 가격이 상품의 가격의 합보다 큰 경우")
    @Test
    public void invalidCase4() {
        메뉴_그룹_등록됨();
        치킨_등록됨();
        커피_등록됨();

        MenuProduct 치킨_메뉴_상품 = 치킨_메뉴_상품();
        MenuProduct 커피_메뉴_상품 = 커피_메뉴_상품();

        Menu creatingMenu = new Menu();
        creatingMenu.setMenuGroupId(MENU_GROUP_ID);
        creatingMenu.setPrice(new BigDecimal(100000));
        creatingMenu.setMenuProducts(new ArrayList<MenuProduct>() {{
            add(치킨_메뉴_상품);
            add(커피_메뉴_상품);
        }});
        assertThatThrownBy(() -> {
            menuService.create(creatingMenu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조회 기능 테스트")
    @Test
    public void listCase() {
        List<Menu> mockMenus = new ArrayList<>();
        Menu mockMenu1 = mock(Menu.class);
        Menu mockMenu2 = mock(Menu.class);
        given(menuDao.findAll())
                .willReturn(mockMenus);
        given(mockMenu1.getId())
                .willReturn(1l);
        given(mockMenu2.getId())
                .willReturn(1l);
        mockMenus.add(mockMenu1);
        mockMenus.add(mockMenu2);

        given(menuProductDao.findAllByMenuId(any()))
                .willReturn(new ArrayList<>());

        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(2);
        assertThat(menus.get(0).getMenuProducts()).isNotNull();
        assertThat(menus.get(1).getMenuProducts()).isNotNull();
    }

    private Menu 메뉴_모킹됨() {
        Menu mockMenu = mock(Menu.class);
        when(mockMenu.getId()).thenReturn(MENU_ID);
        given(menuDao.save(any()))
                .willReturn(mockMenu);
        return mockMenu;
    }

    private void 메뉴_상품_모킹됨() {
        MenuProduct mockMenuProduct = mock(MenuProduct.class);
        given(menuProductDao.save(any()))
                .willReturn(mockMenuProduct);
    }

    private void 메뉴_그룹_등록됨() {
        given(menuGroupDao.existsById(MENU_GROUP_ID))
                .willReturn(true);
    }

    private Product 치킨_등록됨() {
        Product chicken = new Product("치킨", new BigDecimal(1000));
        given(productDao.findById(PRODUC_CHICKEN_ID))
                .willReturn(Optional.of(chicken));
        return chicken;
    }

    private Product 커피_등록됨() {
        Product coffee = new Product("커피", new BigDecimal(2000));
        given(productDao.findById(PRODUCT_COFFE_ID))
                .willReturn(Optional.of(coffee));
        return coffee;
    }

    private MenuProduct 치킨_메뉴_상품() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(PRODUC_CHICKEN_ID);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    private MenuProduct 커피_메뉴_상품() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(PRODUCT_COFFE_ID);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    private MenuProduct 존재하지않는_메뉴_상품() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(999l);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

}
