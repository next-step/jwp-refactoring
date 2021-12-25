package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 서비스 테스트")
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

    @Test
    @DisplayName("메뉴 조회한다.")
    void findMenusTest() {
        //given
        //when
        menuService.findAll();
        //then
        verify(menuRepository).findAll();
    }

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러처리한다.")
    void saveWrongPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu("치킨", new BigDecimal(-100), 1L, null);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("menuGroupId를 입력하지 않았을 때 에러처리한다.")
    void saveEmptyMenuGroupIdPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu("치킨", new BigDecimal(100), null, null);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID.errorMessage());
    }

    @Test
    @DisplayName("menuGroupId가 0보다 작을 경우 에러처리한다.")
    void saveWrongMenuGroupIdPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "치킨", new BigDecimal(100), -2L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("등록되지 않는 상품은 메뉴로 등록할수 없다.")
    void saveMenuNotRegisteredProduct() {
        //given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 1L));
        MenuRequest menuRequest = new MenuRequest("핫도그", new BigDecimal(100), 1L, menuProductRequests);

        when(menuGroupRepository.existsById(anyLong()))
                .thenReturn(true);
        //when
        when(productRepository.findAllById(anyList()))
                .thenReturn(new ArrayList<>());

        //then
        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_PRODUCT_IS_NOT_REGISTERED.errorMessage());
    }
}
