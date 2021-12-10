package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.일인_세트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 관리")
class MenuServiceTest {

//    private MenuDao menuDao;
//    private MenuGroupDao menuGroupDao;
//    private MenuProductDao menuProductDao;
//    private ProductDao productDao;
//    private MenuService menuService;
//
//    public static Menu menu(Long id, String name, BigDecimal price, Long menuGroupId) {
//        Menu menu = new Menu();
//        menu.setId(id);
//        menu.setName(name);
//        menu.setPrice(price);
//        menu.setMenuGroupId(menuGroupId);
//        return menu;
//    }
//
//    public static MenuProduct menuProduct(Long seq, Menu menu, Product product, long quantity) {
//        MenuProduct menuProduct = new MenuProduct();
//        menuProduct.setSeq(seq);
//        menuProduct.setMenuId(menu.getId());
//        menuProduct.setProductId(product.getId());
//        menuProduct.setQuantity(quantity);
//        return menuProduct;
//    }
//
//    @BeforeEach
//    void setUp() {
//        menuDao = mock(MenuDao.class);
//        menuGroupDao = mock(MenuGroupDao.class);
//        menuProductDao = mock(MenuProductDao.class);
//        productDao = mock(ProductDao.class);
//        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
//    }
//
//    Menu setMockData(boolean groupStatus) {
//        final Menu menu = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
//        final MenuGroup menuGroup = 일인_세트;
//        final Product product = product(1L, "후라이드", BigDecimal.valueOf(15000));
//        final MenuProduct menuProduct = menuProduct(1L, menu, product, 1L);
//        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));
//
//        when(menuGroupDao.existsById(anyLong())).thenReturn(groupStatus);
//        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
//        when(menuDao.save(any(Menu.class))).thenReturn(menu);
//        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);
//
//        return menu;
//    }
//
//    @Test
//    void findAllMenu() {
//        // given
//        final Menu menu = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
//        final MenuGroup menuGroup = 일인_세트;
//        final Product product = product(1L, "후라이드", BigDecimal.valueOf(1000));
//        final MenuProduct menuProduct = menuProduct(1L, menu, product, 1L);
//        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));
//        when(menuDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menu)));
//
//        List<Menu> actual = menuService.list();
//
//        assertAll(
//                () -> assertThat(actual).contains(menu),
//                () -> assertThat(actual).hasSize(1)
//        );
//    }
//
//    @Nested
//    @DisplayName("메뉴 생성")
//    class CreateMenu {
//        @Test
//        @DisplayName("성공")
//        void createSuccess() {
//            // given
//            boolean isExistsMenuGroup = true;
//            Menu menu = setMockData(isExistsMenuGroup);
//
//            // when
//            Menu actual = menuService.create(menu);
//
//            // then
//            assertAll(
//                    () -> assertThat(actual).isEqualTo(menu),
//                    () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
//                    () -> assertThat(actual.getMenuProducts()).containsAll(menu.getMenuProducts())
//            );
//        }
//
//        @Test
//        @DisplayName("실패 - 메뉴 그룹이 존재 하지 않음.")
//        void createFailNotExistsMenuGroup() {
//            // given
//            boolean isExistsMenuGroup = false;
//            Menu menu = setMockData(isExistsMenuGroup);
//
//            // when
//            assertThatThrownBy(() -> {
//                Menu actual = menuService.create(menu);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        @DisplayName("실패 - 상품이 존재 하지 않음.")
//        void createFailNotExistsProduct() {
//            // given
//            boolean isExistsMenuGroup = true;
//            Menu menu = setMockData(isExistsMenuGroup);
//            when(productDao.findById(anyLong())).thenReturn(Optional.empty());
//
//            // when
//            assertThatThrownBy(() -> {
//                Menu actual = menuService.create(menu);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        @DisplayName("실패 - 상품의 잘못된 금액")
//        void createFailIllegalPrice() {
//            // given
//            boolean isExistsMenuGroup = true;
//            Menu menu = setMockData(isExistsMenuGroup);
//            final Product product = product(1L, "후라이드", BigDecimal.valueOf(1000));
//            when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
//
//            // when
//            assertThatThrownBy(() -> {
//                Menu actual = menuService.create(menu);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//    }

}
