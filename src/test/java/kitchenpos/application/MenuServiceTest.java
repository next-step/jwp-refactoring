package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.*;
import kitchenpos.exception.IllegalMenuPriceException;
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
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(1L, null, 후라이드치킨, 1)));
        menu = new Menu(1L, "후라이드치킨", Price.from(16_000), new MenuGroup(), menuProducts);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createTest() {
        // given
        when(menuRepository.save(menu)).thenReturn(menu);

        // when
        MenuService menuService = new MenuService(menuRepository);
        Menu returnedMenu = menuService.create(this.menu);

        // then
        assertThat(returnedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void list() {
        // given
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

        // when
        MenuService menuService = new MenuService(menuRepository);
        Menu returnedMenu = menuService.create(this.menu);
        List<Menu> list = menuService.list();

        // then
        assertThat(list).contains(returnedMenu);
    }
}
