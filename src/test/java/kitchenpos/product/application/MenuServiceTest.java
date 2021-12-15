package kitchenpos.product.application;

import static kitchenpos.product.sample.MenuGroupSample.두마리메뉴;
import static kitchenpos.product.sample.ProductSample.십원치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuGroup;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuRepository;
import kitchenpos.product.ui.request.MenuProductRequest;
import kitchenpos.product.ui.request.MenuRequest;
import kitchenpos.product.domain.Product;
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

    public static MenuProduct 십원치킨두마리() {
        MenuProduct menuProduct = spy(MenuProduct.of(십원치킨(), Quantity.from(2L)));
        when(menuProduct.seq()).thenReturn(1L);
        return menuProduct;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨세트", BigDecimal.ONE, 1L,
            Collections.singletonList(menuProductRequest));

        MenuGroup 두마리메뉴 = 두마리메뉴();
        when(menuGroupService.findById(menuRequest.getMenuGroupId())).thenReturn(두마리메뉴);

        Product 십원치킨 = 십원치킨();
        when(productService.findById(menuProductRequest.getProductId()))
            .thenReturn(십원치킨);

        Menu 후라이드치킨세트 = 후라이드치킨세트();
        when(menuRepository.save(any())).thenReturn(후라이드치킨세트);

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
    @DisplayName("등록하려는 메뉴의 가격은 상품들의 수량 * 가격을 모두 합친 금액보다 작거나 같아야 한다.")
    void create_priceLessThanMenuProductPrice_thrownException() {
        //given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest(
            "후라이드치킨세트",
            BigDecimal.valueOf(20),
            1L,
            Collections.singletonList(menuProductRequest));

        MenuGroup 두마리메뉴 = 두마리메뉴();
        when(menuGroupService.findById(menuRequest.getMenuGroupId())).thenReturn(두마리메뉴);

        Product 십원치킨 = 십원치킨();
        when(productService.findById(menuProductRequest.getProductId()))
            .thenReturn(십원치킨);

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
        Menu 후라이드치킨세트 = 후라이드치킨세트();
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(후라이드치킨세트));

        //when
        menuService.list();

        //then
        verify(menuRepository, only()).findAll();
    }

    private void requestedMenuSave(MenuRequest menuRequest) {
        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository, only()).save(menuCaptor.capture());
        Menu savedMenu = menuCaptor.getValue();

        assertAll(
            () -> assertThat(savedMenu)
                .extracting(Menu::name, Menu::price)
                .containsExactly(menuRequest.name(), menuRequest.price()),
            () -> assertThat(savedMenu.menuProducts())
                .extracting(MenuProduct::quantity)
                .containsExactly(
                    menuRequest.getMenuProducts()
                        .stream()
                        .map(MenuProductRequest::quantity)
                        .toArray(Quantity[]::new)
                )
        );
    }

    private Menu 후라이드치킨세트() {
        Menu menu = spy(Menu.of(
            Name.from("후라이드치킨세트"),
            Price.from(BigDecimal.TEN),
            두마리메뉴(),
            Collections.singletonList(십원치킨두마리())
        ));
        when(menu.id()).thenReturn(1L);
        return menu;
    }
}
