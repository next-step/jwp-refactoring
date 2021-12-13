package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기.getId(), 돼지고기.getId(), 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기.getId(), 공기밥.getId(), 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));
    }

    @DisplayName("Menu 를 등록한다.")
    @Test
    void create1() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        given(menuGroupRepository.existsById(고기_메뉴그룹.getId())).willReturn(true);
        given(productRepository.findById(불고기_돼지고기.getProduct().getId())).willReturn(Optional.ofNullable(돼지고기));
        given(productRepository.findById(불고기_공기밥.getProduct().getId())).willReturn(Optional.ofNullable(공기밥));
        given(menuRepository.save(any(Menu.class))).willReturn(불고기);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertThat(menuResponse).isEqualTo(MenuResponse.from(불고기));
    }

    @DisplayName("Menu 가격은 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  null,
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 가격은 음수(0원 미만)이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(wrongPrice),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 는 자신이 속할 MenuGroup 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        given(menuGroupRepository.existsById(고기_메뉴그룹.getId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 는 메뉴를 구성하는 Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(10_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        given(menuGroupRepository.existsById(고기_메뉴그룹.getId())).willReturn(true);
        given(productRepository.findById(불고기_돼지고기.getProduct().getId())).willReturn(Optional.ofNullable(돼지고기));
        given(productRepository.findById(돼지고기.getId())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 의 총 가격이 메뉴를 구성하는 각 상품의 (가격 * 수량) 총합보다 크면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(MenuProductRequest.of(불고기_돼지고기.getProduct().getId(), 불고기_돼지고기.getQuantity()),
                          MenuProductRequest.of(불고기_공기밥.getProduct().getId(), 불고기_공기밥.getQuantity()));

        MenuRequest menuRequest = new MenuRequest("불고기",
                                                  BigDecimal.valueOf(1000_000),
                                                  고기_메뉴그룹.getId(),
                                                  menuProductRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menuRequest));
    }

    @DisplayName("Menu 목록을 조회한다.")
    @Test
    void findList() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(불고기));

        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses).containsExactly(MenuResponse.from(불고기));
    }
}