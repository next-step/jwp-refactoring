package kitchenpos.Menu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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
    private ApplicationEventPublisher publisher;

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

        menuService =  new MenuService(menuRepository, menuGroupRepository, publisher);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        given(menuGroupRepository.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));
        given(menuRepository.save(any())).willReturn(menu);

        MenuResponse menuResponse = 메뉴_생성_요청(menuRequest);

        메뉴_생성됨(menuResponse);
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Test
    void searchList() {
        Menu menu2 = new Menu("후라이드치즈볼", new BigDecimal(20000), menuGroup);
        given(menuRepository.findAll()).willReturn(Arrays.asList(menu, menu2));

        List<MenuResponse> menus = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(menus, Arrays.asList(menu, menu2));
    }

    private void 상품_생성() {
        뿌링클 = new Product(1L, "뿌링클", new BigDecimal(18000));
        치즈볼 = new Product(1L, "치즈볼", new BigDecimal(18000));
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
    }

    public static Menu 메뉴_생성(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup);
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
