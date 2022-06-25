package kitchenpos.menu.application;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static kitchenpos.helper.MenuFixtures.통반세트_요청_만들기;
import static kitchenpos.helper.MenuGroupFixtures.두마리메뉴_그룹;
import static kitchenpos.helper.MenuProductFixtures.반반치킨_메뉴상품;
import static kitchenpos.helper.MenuProductFixtures.통구이_메뉴상품;
import static kitchenpos.helper.ProductFixtures.반반치킨_상품;
import static kitchenpos.helper.ProductFixtures.통구이_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class MenuServiceUnitTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, productService, menuGroupService);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        long generateMenuId = 1;
        MenuRequest request = 통반세트_요청_만들기(30000);

        given(menuGroupService.findMenuGroup(request.getMenuGroupId())).willReturn(두마리메뉴_그룹);
        given(productService.findProduct(any())).willReturn(통구이_상품).willReturn(반반치킨_상품);
        doAnswer(invocation -> {
            Menu savedMenu = 메뉴_만들기(generateMenuId, request.getName(), request.getPrice());
            savedMenu.setMenuGroup(두마리메뉴_그룹);
            savedMenu.addMenuProduct(통구이_메뉴상품);
            savedMenu.addMenuProduct(반반치킨_메뉴상품);
            return savedMenu;
        }).when(menuRepository).save(any());

        //when
        MenuResponse result = menuService.create(request);

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(generateMenuId),
                () -> assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(generateMenuId),
                () -> assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(generateMenuId)
        );

    }

    @DisplayName("메뉴 그룹이 등록 되어있지 않은 경우 메뉴를 등록 할 수 없다.")
    @Test
    void create_empty_menu_group_id() {
        //given
        MenuRequest request = 통반세트_요청_만들기(30000);
        given(menuGroupService.findMenuGroup(request.getMenuGroupId())).willThrow(IllegalArgumentException.class);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(request));

    }

    @DisplayName("메뉴 가격이 금액(가격 * 수량)보다 큰 경우 메뉴를 등록할 수 없다.")
    @Test
    void create_price_greater_than_amount() {
        //given
        MenuRequest request = 통반세트_요청_만들기(36000);

        given(menuGroupService.findMenuGroup(request.getMenuGroupId())).willReturn(두마리메뉴_그룹);
        given(productService.findProduct(any())).willReturn(통구이_상품).willReturn(반반치킨_상품);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(request));

    }

    @DisplayName("메뉴 가격이 null 이거나 0원 미만이면 메뉴를 등록할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        MenuRequest request_price_less_then_zero = 통반세트_요청_만들기(-1);
        MenuRequest request_price_null = 통반세트_요청_만들기(null);

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> menuService.create(request_price_less_then_zero)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request_price_null))
        );

    }

}
