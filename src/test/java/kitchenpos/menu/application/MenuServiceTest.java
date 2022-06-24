package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infrastructure.MenuGroupRepository;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infrastructure.ProductRepository;
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
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록한다.")
    void createMenu() {
        Product product = Product.of("허니콤보", BigDecimal.valueOf(19_000L));
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product, 1L);
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        Menu menu = Menu.createMenu("허니콤보", Price.from(BigDecimal.valueOf(19_000L)), menuGroup, menuProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(menuGroup));
        when(menuRepository.save(any())).thenReturn(menu);

        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, menuGroup.getId(), product.getId(), 1L);
        MenuResponse actual = menuService.create(menuRequest);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(menu.getName());
    }

    @Test
    @DisplayName("가격이 0원 미만인 경우, 예외를 반환한다.")
    void createWithInvalidPrice() {
        MenuRequest menuRequest = new MenuRequest("허니콤보", -1L, 1L, 1L, 1L);
        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuProducts() {
        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, 1L, 1L, 1L);

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuGroup() {
        Product product = Product.of("허니콤보", BigDecimal.valueOf(19_000L));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, 1L, product.getId(), 1L);

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격 총합보다 크면 예외를 반환한다.")
    void createOverPrice() {
        Product product = Product.of("허니콤보", BigDecimal.valueOf(19_000L));
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product, 1L);
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(menuGroup));
        MenuRequest menuRequest = new MenuRequest("허니콤보", 20_000L, 1L, product.getId(), 1L);

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        Product product = Product.of("허니콤보", BigDecimal.valueOf(19_000L));
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product, 1L);
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        Menu menu = Menu.createMenu("허니콤보", Price.from(BigDecimal.valueOf(19_000L)), menuGroup, menuProduct);
        when(menuRepository.findAll()).thenReturn(Lists.list(menu));

        List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
