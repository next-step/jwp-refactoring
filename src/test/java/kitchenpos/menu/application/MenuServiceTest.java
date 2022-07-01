package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품_생성;
import static kitchenpos.menu.domain.MenuTest.메뉴_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        Product product = 상품_생성(1L, "상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product, 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 1_000, 1L, menuProducts);

        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuRepository.save(menu)).thenReturn(메뉴_생성(1L, "매뉴", 1_000, 1L, menuProducts));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId())
        );
    }

    @DisplayName("메뉴의 가격은 0이상이여야 한다")
    @Test
    void createMenu1() {
        // given
        Menu menu = 메뉴_생성("매뉴", -1, null, null);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴그룹에 포함되어야 한다")
    @Test
    void createMenu2() {
        // given
        Menu menu = 메뉴_생성("매뉴", 1_000, null, null);

        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 정보가 존재하지 않으면 안된다.")
    @Test
    void createMenu3() {
        // given
        Product product = 상품_생성("상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product, 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 2_000, 1L, menuProducts);

        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품의 가격의 합보다 크면 안된다.")
    @Test
    void createMenu4() {
        // given
        Product product = 상품_생성("상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product, 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 2_000, 1L, menuProducts);

        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void listMenu() {
        // given
        Product product1 = 상품_생성(1L, "상품1", 1_000);
        Product product2 = 상품_생성(2L, "상품2", 1_000);
        Menu menu = 메뉴_생성(1L, "매뉴", 1000, null, null);
        List<Menu> menus = Collections.singletonList(menu);
        MenuProduct menuProduct1 = 메뉴_상품_생성(menu, product1, 1);
        MenuProduct menuProduct2 = 메뉴_상품_생성(menu, product2, 1);

        when(menuRepository.findAll()).thenReturn(menus);
        when(menuProductRepository.findAllByMenuId(any())).thenReturn(Arrays.asList(menuProduct1, menuProduct2));

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list).containsExactly(menu);
    }
}
