package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.message.MenuMessage;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private final Long menuGroupId = 1L;
    private List<MenuProductRequest> menuProductRequests;
    private Menu menu;
    private List<Menu> menus;

    @BeforeEach
    void setUp() {
        menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 1L));

        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(1L, 1L));
        menu = Menu.of("후라이드치킨", 16_000L, 1L, menuProducts);

        List<MenuProduct> menuProducts2 = Arrays.asList(MenuProduct.of(2L, 1L));
        Menu menu2 = Menu.of("간장치킨", 17_000L, 1L, menuProducts2);

        menus = Arrays.asList(menu, menu2);
    }

    @Test
    @DisplayName("메뉴 등록시 등록에 성공하고 메뉴 정보를 반환한다")
    void createMenuThenReturnMenuInfoResponseTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId, menuProductRequests);
        willDoNothing().given(menuValidator).validate(any());
        given(menuRepository.save(any())).willReturn(menu);

        // when
        MenuResponse response = menuService.createMenu(menuCreateRequest);

        // then
        then(menuValidator).should(times(1)).validate(any());
        then(menuRepository).should(times(1)).save(any());
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("메뉴 등록시 가격이 누락되어 있으면 예외처리되어 등록에 실패한다")
    void createMenuThrownByEmptyPriceTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", null, menuGroupId, menuProductRequests);
        willDoNothing().given(menuValidator).validate(any());

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuValidator).should(times(1)).validate(any());
    }

    @Test
    @DisplayName("메뉴 등록시 가격이 0원 미만인경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByInValidPriceTest() {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(-1L), menuGroupId, menuProductRequests);
        willDoNothing().given(menuValidator).validate(any());

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuValidator).should(times(1)).validate(any());
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴 그룹에 속해있지않은경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByEmptyMenuGroupTest() {
        // given
        willThrow(new IllegalArgumentException(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message()))
                .given(menuValidator).validate((any()));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), null, menuProductRequests);

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message());

        // then
        then(menuValidator).should(times(1)).validate(any());
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴에 있는 상품이 미등록인경우 예외처리되어 등록에 실패한다")
    void createMenuThrownByUnEnrolledProductTest() {
        // given
        willThrow(new EntityNotFoundException()).given(menuValidator).validate((any()));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(16_000L), menuGroupId, menuProductRequests);

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(menuValidator).should(times(1)).validate(any());
    }

    @Test
    @DisplayName("메뉴 등록시 메뉴 가격이 등록된 상품 요금의 합산된 금액보다 클경우 예외처리되어 등록에 실패한다")
    void addProductToMenuThrownByNotValidPriceTest() {
        // given
        willThrow(new IllegalArgumentException(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message()))
                .given(menuValidator).validate((any()));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(20_000L), menuGroupId, menuProductRequests);

        // when
        assertThatThrownBy(() -> menuService.createMenu(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message());

        // then
        then(menuValidator).should(times(1)).validate(any());
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
