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
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 테스트")
class MenuServiceTest {
    private final MenuRepository menuRepository = new FakeMenuRepository();
    private final MenuGroupRepository menuGroupRepository = new FakeMenuGroupRepository();
    private final MenuProductRepository menuProductRepository = new FakeMenuProductRepository();
    private final ProductRepository productRepository = new FakeProductRepository();
    private final MenuService menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);

    @DisplayName("가격이 음수면 예외가 발생한다.")
    @Test
    void priceIsNegative() {
        Product 살치살 = Product.of(1L,"살치살",10000);
        Product 부채살 = Product.of(2L,"부채살",10000);
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Menu menu = Menu.of("소고기세트", -1, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(살치살, 2),
                        MenuProduct.of(부채살, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsMenuGroup() {
        Product 살치살 = Product.of(1L,"살치살",10000);
        Product 부채살 = Product.of(2L,"부채살",10000);
        Menu menu = Menu.of("소고기세트", 50000, null,
                Arrays.asList(
                        MenuProduct.of(살치살, 2),
                        MenuProduct.of(부채살, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsProduct() {
        Product 살치살 = Product.of(1L,"살치살",10000);
        Product 부채살 = Product.of(2L,"부채살",10000);
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Menu menu = Menu.of("소고기세트", 50000, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(살치살, 2),
                        MenuProduct.of(부채살, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격이 메뉴 상품들 가격의 합보다 크면 예외 발생한다.")
    @Test
    void priceCheaperThanSumMenuProducts() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        Menu menu = Menu.of("소고기세트", 100000, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(소고기, 2),
                        MenuProduct.of(쌈채소, 1)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void success() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        Menu menu = Menu.of("소고기세트", 70000, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(소고기, 2),
                        MenuProduct.of(쌈채소, 1)
                )
        );

        Menu resultMenu = menuService.create(menu);
        assertAll(
                () -> equalsMenu(resultMenu, savedMenuGroup.getId(), menu),
                () -> equalsMenuProduct(resultMenu, menu)
        );
    }

    @DisplayName("모든 메뉴 조회")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        Product 소주 = productRepository.save(Product.of("소주", 5000));
        Menu menu1 = Menu.of("소고기세트", 70000, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(소고기, 2),
                        MenuProduct.of(쌈채소, 1)
                )
        );
        Menu menu2 = Menu.of("소고기세트", 105000, savedMenuGroup,
                Arrays.asList(
                        MenuProduct.of(소고기, 3),
                        MenuProduct.of(쌈채소, 1),
                        MenuProduct.of(소주, 1)
                )
        );

        Menu resultMenu1 = menuService.create(menu1);
        Menu resultMenu2 = menuService.create(menu2);

        List<Menu> list = menuService.list();
        long count = getMenuProductCount(list);
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(count).isEqualTo(resultMenu1.getMenuProducts().size() + resultMenu2.getMenuProducts().size())
        );
    }

    private long getMenuProductCount(List<Menu> list) {
        return list.stream()
                .map(menu -> menu.getMenuProducts())
                .flatMap(menuProducts -> menuProducts.stream())
                .count();
    }

    private void equalsMenuProduct(Menu resultMenu, Menu menu) {
        List<MenuProduct> savedMenuProducts = resultMenu.getMenuProducts();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (int i = 0; i < savedMenuProducts.size(); i++) {
            MenuProduct resultMenuProduct = savedMenuProducts.get(i);
            MenuProduct menuProduct = menuProducts.get(i);
            assertThat(resultMenuProduct.getSeq()).isNotNull();
            assertThat(resultMenuProduct.getProduct()).isEqualTo(menuProduct.getProduct());
            assertThat(resultMenuProduct.getQuantity()).isEqualTo(menuProduct.getQuantity());
            assertThat(resultMenuProduct.getMenu()).isEqualTo(resultMenu.getId());
        }
    }

    private void equalsMenu(Menu resultMenu, Long menuGroupId, Menu menu) {
        assertThat(resultMenu.getMenuGroupId()).isEqualTo(menuGroupId);
        assertThat(resultMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(resultMenu.getName()).isEqualTo(menu.getName());
    }

}
