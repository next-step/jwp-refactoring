package kitchenpos.menu.application;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 서비스 테스트")
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

    @Test
    @DisplayName("메뉴를 등록한다.")
    void saveMenuTest(){
        Product newProduct = new Product(1L, "신양념치킨", new BigDecimal(50000));
        MenuProduct menuProduct = new MenuProduct(newProduct, 5);
        Menu menu = new Menu(1L, "스페셜치킨", new BigDecimal(10000), 1L, Arrays.asList(menuProduct));
        checkValidMenu(menu);
    }

    @Test
    @DisplayName("메뉴 조회한다.")
    void findMenusTest() {
        //given
        //when
        menuService.findAll();
        //then
        Mockito.verify(menuRepository).findAll();
    }

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러처리한다.")
    void saveWrongPriceMenuTest() {
        //given
        MenuRequest menu = Mockito.mock(MenuRequest.class);
        Mockito.when(menu.getName()).thenReturn("치킨세트");
        Mockito.when(menu.getPrice()).thenReturn(new BigDecimal(-1000));
        Mockito.when(menuGroupRepository.existsById(ArgumentMatchers.anyLong())).thenReturn(true);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    menuService.create(menu);
                }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("menuGroupId를 존재하지 않았을 때 에러처리한다.")
    void saveNotExistMenuGroupIdPriceMenuTest() {
        //given
        MenuRequest menu = Mockito.mock(MenuRequest.class);
        Mockito.when(menu.getMenuGroupId()).thenReturn(null);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    menuService.create(menu);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_GROUP_CAN_NOT_SEARCH.errorMessage());
    }

    @Test
    @DisplayName("등록되지 않는 상품은 메뉴로 등록할수 없다.")
    void saveMenuNotRegisteredProduct() {
        //given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 1L));
        MenuRequest menuRequest = new MenuRequest("핫도그", new BigDecimal(100), 1L, menuProductRequests);

        Mockito.when(menuGroupRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);
        //when
        Mockito.when(productRepository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(new ArrayList<>());

        //then
        Assertions.assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_PRODUCT_IS_NOT_REGISTERED.errorMessage());
    }

    private void checkValidMenu(Menu menu) {
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName().getName()).isEqualTo("스페셜치킨"),
                () -> assertThat(menu.getPrice().getPrice()).isEqualTo(new BigDecimal("10000")),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
