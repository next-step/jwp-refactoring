package kitchenpos.menu;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.CreateMenuValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관련 기능")
public class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    MenuService menuService;

    @Mock
    CreateMenuValidator menuValidator;

    private Product 짜장면;
    private Product 탕수육;
    private MenuGroup 중국음식;
    private Menu 짜장면메뉴;
    private MenuRequest 짜장면메뉴등록요청;

    @BeforeEach
    void setUp() {
        짜장면 = mock(Product.class);
        when(짜장면.getId()).thenReturn(1L);
        when(짜장면.getPrice()).thenReturn(Price.of(new BigDecimal(5000)));

        탕수육 = mock(Product.class);
        when(탕수육.getId()).thenReturn(2L);
        when(탕수육.getPrice()).thenReturn(Price.of(new BigDecimal(15000)));

        중국음식 = mock(MenuGroup.class);
        when(중국음식.getId()).thenReturn(1L);
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        짜장면메뉴 = 메뉴_등록(1L, "짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()).getPrice(), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(짜장면, 1L), 메뉴_상품_등록(탕수육, 1L)));
        짜장면메뉴등록요청 = new MenuRequest("짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()).getPrice(), 중국음식.getId(), Arrays.asList(new MenuProductRequest(짜장면.getId(), 1l), new MenuProductRequest(탕수육.getId(), 1l)));
        // given
        given(menuRepository.save(any())).willReturn(짜장면메뉴);

        // when
        MenuResponse createMenu = menuService.create(짜장면메뉴등록요청);

        // then
        assertThat(createMenu).isNotNull();
        assertThat(createMenu.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격의 총 합보다 크면 등록에 실패한다.")
    void createMenuOfNotSamePrice() {
        짜장면메뉴 = 메뉴_등록(1L, "짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()).getPrice(), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(짜장면, 1L), 메뉴_상품_등록(탕수육, 1L)));
        짜장면메뉴등록요청 = new MenuRequest("짜장면탕수육세트", BigDecimal.valueOf(10000), 중국음식.getId(), Arrays.asList(new MenuProductRequest(짜장면.getId(), 1l), new MenuProductRequest(탕수육.getId(), 1l)));

        List<MenuProduct> menuProducts = 짜장면메뉴등록요청.getMenuProductRequests()
                .stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct())
                .collect(Collectors.toList());

        doThrow(IllegalArgumentException.class).when(menuValidator).validateCreateMenu(짜장면메뉴등록요청.getMenuGroupId(), 짜장면메뉴등록요청.getPrice(), menuProducts);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuService.create(짜장면메뉴등록요청);
        });
    }

    public static Menu 메뉴_등록(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(id, name, price, menuGroupId);
        menu.organizeMenu(menuProducts);
        return menu;
    }

    public static MenuProduct 메뉴_상품_등록(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }
}
