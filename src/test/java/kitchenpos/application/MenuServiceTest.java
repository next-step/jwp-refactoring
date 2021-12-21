package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.*;
import kitchenpos.exception.NegativePriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private Menu menu;

    private Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product(1L, "후라이드치킨", Price.from(16_000));
        menu = new Menu(1L, "후라이드치킨", Price.from(16_000), new MenuGroup(), new MenuProducts(Collections.singletonList(new MenuProduct(1L, menu, 1L, 1))));
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createTest() {
        // given
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(후라이드치킨));
        when(menuRepository.save(menu)).thenReturn(menu);

        // when
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
        Menu returnedMenu = menuService.create(this.menu);

        // then
        assertThat(returnedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴의 가격이 0원 이상이어야 한다")
    @Test
    void minusMenuPriceTest() {
        // then
        assertThatThrownBy(() -> this.menu.setPrice(Price.from(-1000))).isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 총합보다 작아야 한다")
    @Test
    void menuPriceLimitTest() {
        // when
        this.menu.setPrice(Price.from(19_000));
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴 그룹에 속해있어야 한다")
    @Test
    void inMenuGroupTest() {
        // when
        this.menu.setMenuGroup(null);
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void list() {
        // given
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(후라이드치킨));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

        // when
        MenuService menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
        Menu returnedMenu = menuService.create(this.menu);
        List<Menu> list = menuService.list();

        // then
        assertThat(list).contains(returnedMenu);
    }
}
