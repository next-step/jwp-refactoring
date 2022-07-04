package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequestDto;
import kitchenpos.menu.dto.MenuProductResponseDto;
import kitchenpos.menu.dto.MenuRequestDto;
import kitchenpos.menu.dto.MenuResponseDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.fixture.MenuFixture.메뉴_데이터_생성;
import static kitchenpos.common.fixture.MenuFixture.메뉴_요청_데이터_생성;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴묶음_데이터_생성;
import static kitchenpos.common.fixture.MenuProductFixture.메뉴상품_데이터_생성;
import static kitchenpos.common.fixture.MenuProductFixture.메뉴상품_요청_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuValidator);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //given
        String name = "menu";
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        List<MenuProductRequestDto> menuProductRequests = Arrays.asList(
                메뉴상품_요청_데이터_생성(1L, 2),
                메뉴상품_요청_데이터_생성(2L, 2));
        MenuRequestDto request = 메뉴_요청_데이터_생성(name, menuPrice, menuGroupId, menuProductRequests);

        MenuGroup menuGroup = 메뉴묶음_데이터_생성(menuGroupId, "name");
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(menuGroup));

        Long menuId = 1L;
        List<MenuProduct> menuProducts = Arrays.asList(
                메뉴상품_데이터_생성(1L, 1L, 2),
                메뉴상품_데이터_생성(2L, 2L, 2));
        given(menuRepository.save(any())).willReturn(메뉴_데이터_생성(menuId, name, menuPrice, menuGroup, menuProducts));

        //when
        MenuResponseDto response = menuService.create(request);

        //then
        메뉴_데이터_확인(response, menuId, name, menuGroupId, menuPrice);
        메뉴상품_데이터_확인(response.getMenuProducts().get(0), 1L, 1L, 1L, 2);
        메뉴상품_데이터_확인(response.getMenuProducts().get(1), 2L, 1L, 2L, 2);
    }

    @DisplayName("메뉴묶음이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_menuGroupNotExists() {
        //given
        MenuRequestDto failRequest = createFailRequest(BigDecimal.valueOf(200));

        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(failRequest));
    }

    @DisplayName("메뉴와 메뉴상품을 전체 조회한다.")
    @Test
    void list() {
        //given
        Long menuId = 1L;
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(200);

        Long menuGroupId = 1L;
        MenuGroup menuGroup = 메뉴묶음_데이터_생성(menuGroupId, "name");

        List<MenuProduct> menuProducts = Arrays.asList(메뉴상품_데이터_생성(1L, 1L, 1));

        given(menuRepository.findAll()).willReturn(Arrays.asList(메뉴_데이터_생성(menuId, name, price, menuGroup, menuProducts)));

        //when
        List<MenuResponseDto> response = menuService.list();

        //then
        assertEquals(1, response.size());
        메뉴_데이터_확인(response.get(0), menuId, name, menuGroupId, price);
    }

    private MenuRequestDto createFailRequest(BigDecimal menuPrice) {
        String name = "menu";
        Long menuGroupId = 1L;
        List<MenuProductRequestDto> menuProductRequests = Arrays.asList(
                메뉴상품_요청_데이터_생성(1L, 2),
                메뉴상품_요청_데이터_생성(2L, 2));
        return 메뉴_요청_데이터_생성(name, menuPrice, menuGroupId, menuProductRequests);
    }

    private void 메뉴_데이터_확인(MenuResponseDto menu, Long id, String name, Long menuGroupId, BigDecimal menuPrice) {
        assertAll(
                () -> assertEquals(id, menu.getId()),
                () -> assertEquals(name, menu.getName()),
                () -> assertEquals(menuPrice, menu.getPrice()),
                () -> assertEquals(menuGroupId, menu.getMenuGroupId()),
                () -> assertThat(menu.getMenuProducts()).isNotEmpty()
        );
    }

    private void 메뉴상품_데이터_확인(MenuProductResponseDto menuProduct, Long seq, Long menuId, Long productId, int quantity) {
        assertAll(
                () -> assertEquals(seq, menuProduct.getSeq()),
                () -> assertEquals(menuId, menuProduct.getMenuId()),
                () -> assertEquals(productId, menuProduct.getProductId()),
                () -> assertEquals(quantity, menuProduct.getQuantity())
        );
    }

}