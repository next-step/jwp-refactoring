package kitchenpos.menu.application;

import static kitchenpos.menu.application.sample.MenuSample.후라이드치킨세트;
import static kitchenpos.product.application.sample.ProductSample.후라이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.product.application.ProductService;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.ONE, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupService.existsById(menuRequest.getMenuGroupId())).thenReturn(true);
        when(productService.findById(menuProductRequest.getProductId()))
            .thenReturn(Optional.of(후라이드치킨()));
        when(menuRepository.save(any())).thenReturn(후라이드치킨세트());

        //when
        menuService.create(menuRequest);

        //then
        requestedMenuSave(menuRequest);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 가격은 반드시 존재해야 한다.")
    void create_nullPrice_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", null, 1L,
            Collections.singletonList(menuProductRequest));

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 가격은 0원 이상이어야 한다.")
    void create_priceLessThanZero_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.valueOf(-1), 1L,
            Collections.singletonList(menuProductRequest));

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 메뉴 그룹은 반드시 존재해야 한다.")
    void create_notExistMenuGroup_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupService.existsById(anyLong())).thenReturn(false);

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 상품은 반드시 존재해야 한다.")
    void create_notExistProduct_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupService.existsById(anyLong())).thenReturn(true);
        when(productService.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 가격은 상품들의 수량 * 가격을 모두 합친 금액보다 작거나 같아야 한다.")
    void create_priceLessThanMenuProductPrice_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupService.existsById(menuRequest.getMenuGroupId())).thenReturn(true);
        when(productService.findById(menuProductRequest.getProductId()))
            .thenReturn(Optional.of(후라이드치킨()));

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("메뉴들을 조회할 수 있다.")
    void list() {
        //given
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(후라이드치킨세트()));

        //when
        menuService.list();

        //then
        verify(menuRepository, only()).findAll();
    }

    private void requestedMenuSave(MenuRequest menuRequest) {
        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository, only()).save(menuCaptor.capture());
        Menu savedMenu = menuCaptor.getValue();
        assertThat(savedMenu)
            .extracting(Menu::getName, Menu::getPrice)
            .containsExactly(menuRequest.getName(), menuRequest.getPrice());

        assertThat(savedMenu.getMenuProducts().list())
            .extracting(MenuProduct::getQuantity)
            .containsExactly(menuRequest.getMenuProducts()
                .stream()
                .map(MenuProductRequest::quantity)
                .toArray(Quantity[]::new));
    }
}
