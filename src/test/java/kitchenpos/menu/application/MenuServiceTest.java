package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private MenuService menuService;


    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
    }

    @Test
    @DisplayName("주어진 메뉴를 저장하고, 저장된 객체를 리턴한다.")
    void create_with_valid_menu() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", BigDecimal.valueOf(32000), 1L, menuProducts);
        Menu givenMenu = Menu.of(1L, "반반치킨", BigDecimal.valueOf(32000), menuGroup);
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념통닭", BigDecimal.valueOf(16000));

        when(menuGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(menuGroup));
        when(productRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(product1, product2));
        when(menuRepository.save(any(Menu.class)))
                .thenReturn(givenMenu);
        MenuResponse actual = menuService.create(menuRequest);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("가격이 없는 메뉴를 저장시 예외를 던진다.")
    void create_menu_with_no_price() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", null, 1L, menuProducts);
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념통닭", BigDecimal.valueOf(16000));
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        when(menuGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(menuGroup));
        when(productRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(product1, product2));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수인 메뉴 저장시 예외를 던진다.")
    void create_menu_with_negative_price() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", BigDecimal.valueOf(-1000), 1L, menuProducts);
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념통닭", BigDecimal.valueOf(16000));
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        when(menuGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(menuGroup));
        when(productRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(product1, product2));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 포함되지 않은 메뉴 저장시 예외를 던진다")
    void create_menu_with_no_menu_group() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", BigDecimal.valueOf(16000), null, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("상품이 포함되지 않은 메뉴 저장시 예외를 던진다")
    void create_menu_with_no_products() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", BigDecimal.valueOf(16000), 1L, menuProducts);
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        when(menuGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(menuGroup));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴에 포함된 상품 합이 올바르지 않게 주어지면 예외를 던진다")
    void create_with_menu_and_invalid_price() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProductRequest1, menuProductRequest2);
        MenuRequest menuRequest = new MenuRequest("반반치킨", BigDecimal.valueOf(33000), 1L, menuProducts);

        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념통닭", BigDecimal.valueOf(16000));

        when(menuGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(menuGroup));
        when(productRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(product1, product2));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup(1L, "추천메뉴");
        MenuGroup menuGroup2 = new MenuGroup(2L, "오늘의메뉴");
        final Menu givenMenu1 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(16000), menuGroup1);
        final Menu givenMenu2 = new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), menuGroup2);

        when(menuRepository.findAll())
                .thenReturn(Arrays.asList(givenMenu1, givenMenu2));
        List<MenuResponse> menus = menuService.list();

        assertThat(menus).containsExactly(new MenuResponse(givenMenu1), new MenuResponse(givenMenu2));
    }
}
