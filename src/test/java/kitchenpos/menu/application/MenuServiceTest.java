package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록한다.")
    void createMenu() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, 1L, Lists.list(menuProductRequest));
        when(menuRepository.save(any())).thenReturn(menuRequest.toEntity());

        MenuResponse actual = menuService.create(menuRequest);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(menuRequest.getName());
    }

    @Test
    @DisplayName("가격이 0원 미만인 경우, 예외를 반환한다.")
    void createWithInvalidPrice() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("허니콤보", -1L, 1L, Lists.list(menuProductRequest));
        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        Product product = Product.of("허니콤보", 19_000L);
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product.getId(), 1L);
        MenuProducts menuProducts = MenuProducts.createMenuProducts(Lists.list(menuProduct));
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        Menu menu = Menu.createMenu("허니콤보", 19_000L, menuGroup.getId(), menuProducts);
        when(menuRepository.findAll()).thenReturn(Lists.list(menu));

        List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
