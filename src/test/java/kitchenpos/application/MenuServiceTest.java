package kitchenpos.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("MenuService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 불고기;
    private Product 김치;
    private Product 공기밥;
    private MenuGroup 한식;
    private MenuProduct 불고기상품;
    private MenuProduct 김치상품;
    private MenuProduct 공기밥상품;
    private Menu 불고기정식;

    @BeforeEach
    void setUp() {
        불고기 = new Product(1L, new Name("불고기"), new Price(BigDecimal.valueOf(10_000)));
        김치 = new Product(2L, new Name("김치"), new Price(BigDecimal.valueOf(1_000)));
        공기밥 = new Product(3L, new Name("공기밥"), new Price(BigDecimal.valueOf(1_000)));
        한식 = new MenuGroup(1L, new Name("한식"));
        불고기정식 = new Menu(1L, new Name("불고기정식"), new Price(BigDecimal.valueOf(12_000L)), 한식);
        불고기상품 = new MenuProduct(1L, new Quantity(1L), 불고기정식, 불고기);
        김치상품 = new MenuProduct(2L, new Quantity(1L), 불고기정식, 김치);
        공기밥상품 = new MenuProduct(3L, new Quantity(1L), 불고기정식, 공기밥);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(불고기상품, 김치상품, 공기밥상품)
                .stream()
                .map(MenuProductRequest::from)
                .collect(Collectors.toList());
        MenuRequest request = MenuRequest.of(불고기정식.getName().value(), 불고기정식.getPrice().value(), 한식.getId(), menuProductRequests);
        when(menuGroupRepository.findById(불고기정식.getMenuGroup().getId())).thenReturn(Optional.of(한식));
        when(productRepository.findById(불고기.getId())).thenReturn(Optional.of(불고기));
        when(productRepository.findById(김치.getId())).thenReturn(Optional.of(김치));
        when(productRepository.findById(공기밥.getId())).thenReturn(Optional.of(공기밥));
        when(menuRepository.save(any())).thenReturn(불고기정식);

        // when
        MenuResponse result = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(불고기정식.getId()),
                () -> assertThat(result.getName()).isEqualTo(불고기정식.getName())
        );
    }

    @DisplayName("메뉴 가격이 null이면 예외가 발생한다.")
    @Test
    void createNullPriceMenuException() {
        // given
        MenuRequest request = MenuRequest.of("불고기정식", null, 한식.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceMenuException(int input) {
        // given
        MenuRequest request = MenuRequest.of("불고기정식", BigDecimal.valueOf(input), 한식.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 없을 경우 예외가 발생한다.")
    @Test
    void notExistMenuGroupException() {
        // given
        MenuRequest request = MenuRequest.of("불고기정식", BigDecimal.valueOf(1_000), 한식.getId(), new ArrayList<>());
        when(menuGroupRepository.findById(불고기정식.getMenuGroup().getId())).thenReturn(Optional.of(한식));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품이 없을 경우 예외가 발생한다.")
    @Test
    void notExistProductException() {
        // given
        MenuRequest request = MenuRequest.of("불고기정식", BigDecimal.valueOf(1_000), 한식.getId(), new ArrayList<>());
        when(menuGroupRepository.findById(불고기정식.getMenuGroup().getId())).thenReturn(Optional.of(한식));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴에 포함된 상품 가격의 합보다 작아야 한다.")
    @Test
    void menuPriceException() {
        // given
        불고기정식.setPrice(new Price(BigDecimal.valueOf(200_000)));
        when(menuGroupRepository.findById(불고기정식.getMenuGroup().getId())).thenReturn(Optional.of(한식));
        when(productRepository.findById(불고기.getId())).thenReturn(Optional.of(불고기));
        when(productRepository.findById(김치.getId())).thenReturn(Optional.of(김치));
        when(productRepository.findById(공기밥.getId())).thenReturn(Optional.of(공기밥));
        List<MenuProductRequest> menuProductRequests = Arrays.asList(불고기상품, 김치상품, 공기밥상품)
                .stream()
                .map(MenuProductRequest::from)
                .collect(Collectors.toList());
        MenuRequest request = MenuRequest.of(불고기정식.getName().value(), 불고기정식.getPrice().value(), 한식.getId(), menuProductRequests);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findAllMenu() {
        // given
        List<Menu> menus = Arrays.asList(불고기정식);
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<MenuResponse> results = menuService.findAll();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(불고기정식.getId()),
                () -> assertThat(results.get(0).getName()).isEqualTo(불고기정식.getName())
        );
    }
}
