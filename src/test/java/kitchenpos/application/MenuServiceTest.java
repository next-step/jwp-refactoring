package kitchenpos.application;

import static kitchenpos.domain.MenuFixture.*;
import static kitchenpos.domain.MenuProductFixture.*;
import static kitchenpos.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 등록 API")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = menuProductParam(1L, 2L);
        Menu menuParam = menuParam("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(menuParam.getMenuGroupId())).willReturn(true);
        List<Product> savedProducts = Collections.singletonList(savedProduct(1L, new BigDecimal(10000)));

        given(productRepository.findAllById(anyList())).willReturn(savedProducts);
        doNothing().when(menuValidator).validate(menuParam, savedProducts, false);

        MenuProduct savedMenuProduct = savedMenuProduct(1L, menuProduct);
        Menu savedMenu = savedMenu(1L, menuParam.getName(), menuParam.getPrice(), menuParam.getMenuGroupId(),
            Collections.singletonList(savedMenuProduct));
        given(menuRepository.save(menuParam)).willReturn(savedMenu);

        // when
        Menu actual = menuService.create(menuParam);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(savedMenu.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(savedMenu.getPrice()),
            () -> assertThat(actual.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId()),
            () -> assertThat(actual.getMenuProducts()).containsExactly(savedMenuProduct)
        );
    }

    @Test
    void list() {
        // given
        Long menuId = 1L;
        MenuProduct menuProduct1 = savedMenuProduct(1L, 1L, 2L);
        MenuProduct menuProduct2 = savedMenuProduct(2L, 2L, 3L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        Menu savedMenu = savedMenu(menuId, "메뉴", BigDecimal.valueOf(13000), 1L, menuProducts);
        given(menuRepository.findAll()).willReturn(Collections.singletonList(savedMenu));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getName()).isEqualTo(savedMenu.getName());
        assertThat(menus.get(0).getMenuProducts()).containsExactly(menuProduct1, menuProduct2);
    }
}
