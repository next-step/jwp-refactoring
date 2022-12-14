package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 비즈니스 테스트")
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

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 양식;
    private Menu 양념치킨_두마리_세트;
    private Menu 스파게티_이인분_세트;
    private Product 양념치킨;
    private Product 스파게티;
    private MenuProduct 치킨_두마리;
    private MenuProduct 스파게티_이인분;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup(1L, "양식");
        양념치킨 = new Product(1L, "양념치킨", BigDecimal.valueOf(20_000));
        스파게티 = new Product(2L, "스파게티", BigDecimal.valueOf(10_000));
        양념치킨_두마리_세트 = new Menu(1L, "양념치킨_두마리_세트", BigDecimal.valueOf(40_000), 양식);
        스파게티_이인분_세트 = new Menu(2L, "양념치킨_두마리_세트", BigDecimal.valueOf(40_000), 양식);

        치킨_두마리 = new MenuProduct(1L, 2, 양념치킨);
        스파게티_이인분 = new MenuProduct(2L, 2, 스파게티);

        양념치킨_두마리_세트.setMenuProducts(Arrays.asList(치킨_두마리));
        스파게티_이인분_세트.setMenuProducts(Arrays.asList(스파게티_이인분));
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(치킨_두마리, 스파게티_이인분)
                .stream()
                .map(MenuProductRequest::from)
                .collect(Collectors.toList());
        MenuRequest 치킨_스파게티_더블세트_메뉴 = MenuRequest.of(양념치킨.getName(), 양념치킨.getPrice(), 양식.getId(), menuProductRequests);

        given(menuGroupRepository.findById(치킨_스파게티_더블세트_메뉴.getMenuGroupId())).willReturn(Optional.of(양식));
        given(productRepository.findById(치킨_두마리.getProduct().getId())).willReturn(Optional.of(양념치킨));
        given(productRepository.findById(스파게티_이인분.getProduct().getId())).willReturn(Optional.of(스파게티));
        given(menuRepository.save(any())).willReturn(양념치킨_두마리_세트);

        MenuResponse savedMenu = menuService.create(치킨_스파게티_더블세트_메뉴);

        assertAll(
                () -> assertThat(savedMenu.getName()).isEqualTo(양념치킨_두마리_세트.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(양념치킨_두마리_세트.getPrice())
        );
    }

    @Test
    void 가격이_존재하지_않는_메뉴는_등록할_수_없다() {
        MenuRequest 치킨_스파게티_더블세트_메뉴 = MenuRequest.of("치킨 스파게티 더블세트 메뉴", null, 양식.getId(), new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(치킨_스파게티_더블세트_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_0원_미만인_메뉴는_등록할_수_없다() {
        MenuRequest 치킨_스파게티_더블세트_메뉴 = MenuRequest.of("치킨 스파게티 더블세트 메뉴", new BigDecimal(-1), 양식.getId(), new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(치킨_스파게티_더블세트_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격은_모든_메뉴_상품의_가격_곱하기_수량의_합보다_작거나_같아야_한다() {
        MenuRequest 치킨_스파게티_더블세트_메뉴 = MenuRequest.of("치킨 스파게티 더블세트 메뉴", new BigDecimal(60_000), 양식.getId(), new ArrayList<>());
        assertThatThrownBy(() -> menuService.create(치킨_스파게티_더블세트_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(양념치킨_두마리_세트, 스파게티_이인분_세트));

        List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(2);
        assertThat(menus.stream().map(MenuResponse::getId))
                .contains(양념치킨_두마리_세트.getId(), 스파게티_이인분_세트.getId());
    }
}
