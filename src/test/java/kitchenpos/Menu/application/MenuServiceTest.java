package kitchenpos.Menu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Product 뿌링클;
    private Product 치즈볼;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private List<MenuProduct> menuProducts = new ArrayList<>();
    private List<MenuProductRequest> menuProductRequests = new ArrayList<>();
    private MenuProductRequest menuProductRequest1;
    private MenuProductRequest menuProductRequest2;
    private MenuGroup menuGroup;
    private MenuRequest menuRequest;
    private Menu menu;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        상품_생성();

        메뉴_상품_생성();

        메뉴_상품_요청_변수_생성();

        menuGroup = 그룹_생성("뿌링클시리즈");

        menuRequest = 메뉴_생성_요청_변수_생성("뿌링클치즈볼", new BigDecimal(21000), menuGroup.getId(), menuProductRequests);

        menu = 메뉴_생성("뿌링클치즈볼", new BigDecimal(21000), menuGroup, menuProducts);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        given(productRepository.findById(뿌링클.getId())).willReturn(Optional.of(뿌링클));
        given(productRepository.findById(치즈볼.getId())).willReturn(Optional.of(치즈볼));
        given(menuGroupRepository.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));
        given(menuRepository.save(any())).willReturn(menu);

        MenuResponse menuResponse = 메뉴_생성_요청(menuRequest);

        메뉴_생성됨(menuResponse);
    }

    @DisplayName("존재하지 않는 메뉴 그룹을 메뉴에 등록시 예외 발생한다.")
    @Test
    void create_메뉴그룹_예외() {
        given(productRepository.findById(뿌링클.getId())).willReturn(Optional.of(뿌링클));
        given(productRepository.findById(치즈볼.getId())).willReturn(Optional.of(치즈볼));

        존재하지_않는_메뉴그룹으로_메뉴_생성_요청시_예외_발생함(menuRequest);
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Test
    void searchList() {
        Menu menu2 = new Menu("후라이드치즈볼", new BigDecimal(20000), menuGroup, menuProducts);
        given(menuRepository.findAll()).willReturn(Arrays.asList(menu, menu2));

        List<MenuResponse> menus = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(menus, Arrays.asList(menu, menu2));
    }

    private void 상품_생성() {
        뿌링클 = new Product(1L, new Name("뿌링클"), new Price(new BigDecimal(18000)));
        치즈볼 = new Product(1L, new Name("치즈볼"), new Price(new BigDecimal(18000)));
    }

    private void 메뉴_상품_생성() {
        menuProduct1 = new MenuProduct(1L, 뿌링클, 1L);
        menuProduct2 = new MenuProduct(2L, 치즈볼, 1L);

        menuProducts.add(menuProduct1);
        menuProducts.add(menuProduct2);
    }

    private void 메뉴_상품_요청_변수_생성() {
        menuProductRequest1 = MenuProductRequest.of(menuProduct1);
        menuProductRequest2 = MenuProductRequest.of(menuProduct2);

        menuProductRequests.add(menuProductRequest1);
        menuProductRequests.add(menuProductRequest2);
    }

    public static MenuGroup 그룹_생성(String name) {
        return new MenuGroup(name);
    }

    private MenuRequest 메뉴_생성_요청_변수_생성(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }

    private MenuResponse 메뉴_생성_요청(MenuRequest menuRequest) {
        return menuService.create(menuRequest);
    }

    private void 메뉴_생성됨(MenuResponse menuResponse) {
        assertThat(menuResponse.getName()).isEqualTo(menu.getName());
        assertThat(menuResponse.getPrice()).isEqualTo(menu.getPrice());
        assertThat(menuResponse.getMenuProducts().get(0).getProduct().getName()).isEqualTo(뿌링클.getName());
    }

    public static Menu 메뉴_생성(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private void 존재하지_않는_메뉴그룹으로_메뉴_생성_요청시_예외_발생함(MenuRequest menuRequest) {
        assertThatThrownBy(() ->menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<MenuResponse> 메뉴_목록_조회_요청() {
        return menuService.list();
    }

    private void 메뉴_목록_조회됨(List<MenuResponse> menuResponses, List<Menu> menus) {
        assertThat(menuResponses).hasSize(menus.size());
        for (int i = 0; i < menus.size(); i++) {
            assertThat(menus.get(i).getId()).isEqualTo(menus.get(i).getId());
            assertThat(menus.get(i).getName()).isEqualTo(menus.get(i).getName());
            assertThat(menus.get(i).getPrice()).isEqualTo(menus.get(i).getPrice());
        }
    }
}
