package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        MenuGroup menuGroup = MenuGroup.from("치킨류");
        Product product = Product.of("후라이드치킨", 10000);
        MenuProduct menuProduct = MenuProduct.of(product, 1L);

        Menu menu = Menu.of("후라이드치킨", 10000, menuGroup);
        menu.addMenuProducts(Arrays.asList(menuProduct));

        ReflectionTestUtils.setField(menuProduct, "seq", 1L);
        when(menuGroupRepository.findById(anyLong()))
            .thenReturn(Optional.of(menuGroup));
        when(productRepository.findById(anyLong()))
            .thenReturn(Optional.of(product));
        when(menuRepository.save(any(Menu.class)))
            .thenReturn(menu);

        MenuRequest menuRequest = new MenuRequest("후라이드치킨", 10000, 1L,
            Arrays.asList(new MenuProductRequest(1L, 1L)));

        MenuResponse saved = menuService.create(menuRequest);

        assertAll(
            () -> assertThat(saved.getName()).isEqualTo("후라이드치킨"),
            () -> assertThat(saved.getMenuProducts())
            .extracting(MenuProductResponse::getQuantity)
            .containsExactly(1L)
        );
    }

    @Test
    @DisplayName("메뉴 등록 시 메뉴 그룹이 없는 경우 실패")
    void createValidateMenuGroup() {
        assertThatThrownBy(() -> menuService.create(new MenuRequest("메뉴", 10000, null, null)))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("해당하는 메뉴그룹을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨류"));
        menu.addMenuProducts(Arrays.asList(MenuProduct.of(Product.of("후라이드치킨", 10000), 1L)));

        when(menuRepository.findAll())
            .thenReturn(Arrays.asList(menu));

        List<MenuResponse> menus = menuService.list();

        assertAll(
            () -> assertThat(menus.size()).isEqualTo(1),
            () -> assertThat(menus).extracting(MenuResponse::getName).containsExactly("후라이드치킨"),
            () -> assertThat(menus.get(0).getMenuProducts().size()).isEqualTo(1)
        );
    }
}
