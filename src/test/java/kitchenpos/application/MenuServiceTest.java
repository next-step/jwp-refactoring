package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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

    @DisplayName("메뉴를 생성한")
    @Test
    void testCreate() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        Menu expectedMenu = new Menu(1L, "대표메뉴", 16000, menuGroup.getId(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(볶음짜장면), Optional.of(삼선짬뽕));
        given(menuDao.save(any(Menu.class))).willReturn(expectedMenu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(new MenuProduct());
        // when
        Menu menu = menuService.create(expectedMenu);
        // then
        assertThat(menu).isEqualTo(expectedMenu);
    }

    @DisplayName("메뉴의 가격은 0원 이상 이어야 한다")
    @Test
    void givenZeroPriceThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        Menu expectedMenu = new Menu(1L, "대표메뉴", -1, menuGroup.getId(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));
        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(expectedMenu);
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 메뉴 그룹이 포함되어야 한다")
    @Test
    void givenNonMenuGroupThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        Menu expectedMenu = new Menu(1L, "대표메뉴", 16000, menuGroup.getId(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));
        given(menuGroupDao.existsById(anyLong())).willReturn(false);
        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(expectedMenu);
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 상품이 포함되어야 한다")
    @Test
    void givenNonProductThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        Menu expectedMenu = new Menu(1L, "대표메뉴", 16000, menuGroup.getId(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.empty(), Optional.of(삼선짬뽕));
        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(expectedMenu);
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 포함된 상품 가격의 합보다 작거나 같아야 한다")
    @Test
    void givenLessThanProductSumPriceThenThrowException() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuGroup menuGroup = new MenuGroup(1L, "식사류");
        Menu expectedMenu = new Menu(1L, "대표메뉴", 17000, menuGroup.getId(), menuProducts);
        menuProducts.add(new MenuProduct(1L, expectedMenu.getId(), 볶음짜장면.getId(), 1));
        menuProducts.add(new MenuProduct(2L, expectedMenu.getId(), 삼선짬뽕.getId(), 1));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(볶음짜장면), Optional.of(삼선짬뽕));
        // when
        ThrowableAssert.ThrowingCallable callable = () -> menuService.create(expectedMenu);
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void testList() {
        // given
        List<Menu> expectedMenus = Arrays.asList(new Menu(1L, "대표 메뉴", 16000, 1L, Collections.emptyList()));
        given(menuDao.findAll()).willReturn(expectedMenus);
        // when
        List<Menu> menus = menuService.list();
        // then
        assertThat(menus).isEqualTo(expectedMenus);
    }
}
