package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.publisher.MenuEventPublisher;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuEventPublisher eventPublisher;

    @InjectMocks
    private MenuService menuService;

    private Product 짜장면;
    private Product 탕수육;

    private MenuProduct 짜장면_메뉴;
    private MenuProduct 탕수육_메뉴;

    private Menu 메뉴;

    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        메뉴 = new Menu(1L, "짜장면 탕수육 메뉴", new BigDecimal(19000), 1L);
        짜장면 = new Product(1L, "짜장면", new BigDecimal(7000));
        탕수육 = new Product(2L, "탕수육", new BigDecimal(12000));
        짜장면_메뉴 = new MenuProduct(1L, 메뉴, 1L, new Quantity(1));
        탕수육_메뉴 = new MenuProduct(2L, 메뉴, 2L, new Quantity(1));

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                new MenuProductRequest(1L, 1),
                new MenuProductRequest(2L, 1));
        menuRequest = new MenuRequest("짜장면 탕수육 메뉴", new BigDecimal(19000), 1L, menuProductRequests);
    }

    @Test
    void 메뉴_등록_기능() {
        List<Product> products = Arrays.asList(짜장면, 탕수육);

        List<MenuProduct> menuProducts = Arrays.asList(짜장면_메뉴, 탕수육_메뉴);
        메뉴.addMenuProducts(menuProducts);

        when(menuRepository.save(any(Menu.class))).thenReturn(메뉴);

        MenuResponse expected = menuService.create(menuRequest);

        assertThat(expected.getId()).isEqualTo(메뉴.getId());
        assertThat(expected.getName()).isEqualTo(메뉴.getName());
        assertThat(expected.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
        assertThat(expected.getPrice()).isEqualTo(메뉴.getPrice().price());

        assertThat(expected.getMenuProductResponses().size()).isEqualTo(2);
    }

    @Test
    void 메뉴_조회() {
        메뉴.addMenuProduct(짜장면_메뉴);
        메뉴.addMenuProduct(탕수육_메뉴);
        when(menuRepository.findAll()).thenReturn(Arrays.asList(메뉴));

        List<MenuResponse> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(1);
        MenuResponse expected = menus.get(0);
        assertThat(expected.getId()).isEqualTo(메뉴.getId());
        assertThat(expected.getName()).isEqualTo(메뉴.getName());
        assertThat(expected.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
        assertThat(expected.getPrice()).isEqualTo(메뉴.getPrice().price());
        assertThat(expected.getMenuProductResponses().size()).isEqualTo(2);
    }
}
