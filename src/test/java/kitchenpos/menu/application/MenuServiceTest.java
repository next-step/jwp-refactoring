package kitchenpos.menu.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Long menuGroupId = 1L;
    private Long productId1 = 1L;
    private List<MenuProductRequest> menuProductRequests;
    private MenuGroup menuGroup;
    private Menu menu;
    private Menu menu2;
    private List<Menu> menus;

    @BeforeEach
    void setUp() {
        menuProductRequests = Arrays.asList(new MenuProductRequest(productId1, 1L));
        menuGroup = new MenuGroup("한마리메뉴");

        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(Product.of("후라이드", 16_000L), 1L));
        menu = Menu.of("후라이드치킨", 16_000L, menuGroup, menuProducts);

        List<MenuProduct> menuProducts2 = Arrays.asList(MenuProduct.of(Product.of("간장치킨", 17_000L), 1L));
        menu2 = Menu.of("간장치킨", 17_000L, menuGroup, menuProducts2);

        menus = Arrays.asList(menu, menu2);
    }

    @Test
    @DisplayName("메뉴 등록시 등록에 성공하고 메뉴 정보를 반환한다")
    void createMenuThenReturnMenuInfoResponseTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId, menuProductRequests);
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(any())).willReturn(Optional.of(ProductFixture.후라이드));
        given(menuRepository.save(any())).willReturn(menu);

        // when
        MenuResponse response = menuService.createMenu(menuCreateRequest);

        // then
        then(menuGroupRepository).should(times(1)).findById(menuGroupId);
        then(productRepository).should(times(1)).findById(productId1);
        then(menuRepository).should(times(1)).save(any());
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("메뉴 등록시 가격이 누락되어 있으면 예외처리되어 등록에 실패한다")
    void createMenuThrownByEmptyPriceTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", null, menuGroupId, menuProductRequests);
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(any())).willReturn(Optional.of(ProductFixture.후라이드));

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuGroupRepository).should(times(1)).findById(any());
        then(productRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("메뉴 등록시 가격이 0원 미만인경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByInValidPriceTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(-1L), menuGroupId, menuProductRequests);
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(any())).willReturn(Optional.of(ProductFixture.후라이드));

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuGroupRepository).should(times(1)).findById(any());
        then(productRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴 그룹에 속해있지않은경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByEmptyMenuGroupTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), null, menuProductRequests);

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴에 있는 상품이 미등록인경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByUnEnrolledProductTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId, menuProductRequests);
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(menuGroupRepository).should(times(1)).findById(any());
        then(productRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴 가격이 등록된 상품 요금의 합산된 금액보다 클경우 예외처리되어 등록에 실패한다")
    void addProductToMenuThrownByNotValidPriceTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(20_000L), menuGroupId, menuProductRequests);
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(productId1)).willReturn(Optional.of(ProductFixture.후라이드));

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuGroupRepository).should(times(1)).findById(menuGroupId);
        then(productRepository).should(times(1)).findById(productId1);
    }

    @Test
    @DisplayName("메뉴 목록 조회시 등록된 메뉴 목록을 반환한다")
    void findAllMenusThenReturnMenuResponsesTest() {
        // given
        given(menuRepository.findAllWithGroupAndProducts()).willReturn(menus);

        // when
        List<MenuResponse> menuResponses = menuService.findAllMenus();

        // then
        List<String> menuNames = menuResponses.stream().map(MenuResponse::getName).collect(Collectors.toList());
        List<String> expectedMenuNames = menus.stream().map(Menu::getName).collect(Collectors.toList());
        assertThat(menuNames).containsAll(expectedMenuNames);
    }
}
