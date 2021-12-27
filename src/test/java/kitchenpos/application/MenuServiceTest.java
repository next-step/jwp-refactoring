package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 테스트")
class MenuServiceTest {
    private final MenuDao menuDao =  new FakeMenuDao();
    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuProductDao menuProductDao = new FakeMenuProductDao();
    private final ProductDao productDao = new FakeProductDao();
    private final MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    @DisplayName("가격이 음수면 예외가 발생한다.")
    @Test
    void priceIsNegative() {
        Menu menu = Menu.of("소고기세트", -1, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 2),
                        MenuProduct.of(2L, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy( () -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsMenuGroup() {
        Menu menu = Menu.of("소고기세트", 50000, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 2),
                        MenuProduct.of(2L, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy( () -> menuService.create(menu));
    }

    @DisplayName("상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsProduct() {
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.of("추천메뉴"));
        Menu menu = Menu.of("소고기세트", 50000, savedMenuGroup.getId(),
                Arrays.asList(
                        MenuProduct.of(1L, 2),
                        MenuProduct.of(2L, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy( () -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격이 메뉴 상품들 가격의 합보다 크면 예외 발생한다.")
    @Test
    void priceCheaperThanSumMenuProducts() {
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.of("추천메뉴"));
        Product product1 =  productDao.save(Product.of("소고기", 30000));
        Product product2 =  productDao.save(Product.of("쌈채소", 10000));
        Menu menu = Menu.of("소고기세트", 100000, savedMenuGroup.getId(),
                Arrays.asList(
                        MenuProduct.of(product1.getId(), 2),
                        MenuProduct.of(product2.getId(), 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy( () -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void success() {
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.of("추천메뉴"));
        Product product1 =  productDao.save(Product.of("소고기", 30000));
        Product product2 =  productDao.save(Product.of("쌈채소", 10000));
        Menu menu = Menu.of("소고기세트", 70000, savedMenuGroup.getId(),
                Arrays.asList(
                        MenuProduct.of(product1.getId(), 2),
                        MenuProduct.of(product2.getId(), 1)
                )
        );

        Menu resultMenu = menuService.create(menu);
        equalsMenu(resultMenu, savedMenuGroup.getId(), menu);
        equalsMenuProduct(resultMenu, menu);
    }

    @DisplayName("모든 메뉴 조회")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.of("추천메뉴"));
        Product product1 =  productDao.save(Product.of("소고기", 30000));
        Product product2 =  productDao.save(Product.of("쌈채소", 10000));
        Product product3 =  productDao.save(Product.of("소주", 5000));
        Menu menu1 = Menu.of("소고기세트", 70000, savedMenuGroup.getId(),
                Arrays.asList(
                        MenuProduct.of(product1.getId(), 2),
                        MenuProduct.of(product2.getId(), 1)
                )
        );
        Menu menu2 = Menu.of("소고기세트", 105000, savedMenuGroup.getId(),
                Arrays.asList(
                        MenuProduct.of(product1.getId(), 3),
                        MenuProduct.of(product2.getId(), 1),
                        MenuProduct.of(product3.getId(), 1)
                )
        );

        Menu resultMenu1 = menuService.create(menu1);
        Menu resultMenu2 = menuService.create(menu2);

        List<Menu> list = menuService.list();
        assertThat(list.size()).isEqualTo(2);
        long count = list.stream()
                .map(menu -> menu.getMenuProducts())
                .flatMap(menuProducts -> menuProducts.stream())
                .count();
        assertThat(count).isEqualTo(resultMenu1.getMenuProducts().size() + resultMenu2.getMenuProducts().size());

    }

    private void equalsMenuProduct(Menu resultMenu, Menu menu) {
        List<MenuProduct> savedMenuProducts = resultMenu.getMenuProducts();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (int i=0 ; i < savedMenuProducts.size() ; i++) {
            MenuProduct resultMenuProduct = savedMenuProducts.get(i);
            MenuProduct menuProduct = menuProducts.get(i);
            assertThat(resultMenuProduct.getSeq()).isNotNull();
            assertThat(resultMenuProduct.getProductId()).isEqualTo(menuProduct.getProductId());
            assertThat(resultMenuProduct.getQuantity()).isEqualTo(menuProduct.getQuantity());
            assertThat(resultMenuProduct.getMenuId()).isEqualTo(resultMenu.getId());
        }
    }

    private void equalsMenu(Menu resultMenu, Long menuGroupId, Menu menu) {
        assertThat(resultMenu.getMenuGroupId()).isEqualTo(menuGroupId);
        assertThat(resultMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(resultMenu.getName()).isEqualTo(menu.getName());
    }

}
