package kitchenpos.application;

import static kitchenpos.application.sample.MenuSample.짜장면_세트;
import static kitchenpos.application.sample.ProductSample.짜장면;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 세트", BigDecimal.ONE, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupDao.existsById(menuRequest.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProductRequest.getProductId())).thenReturn(Optional.of(짜장면()));
        when(menuDao.save(any())).thenReturn(짜장면_세트());

        //when
        menuService.create(menuRequest);

        //then
        assertAll(
            () -> requestedMenuSave(menuRequest),
            () -> requestedMenuProductSave(menuProductRequest)
        );
    }

    @Test
    @DisplayName("메뉴의 가격은 반드시 존재해야 한다.")
    void create_nullPrice_thrownException() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 메뉴", null, 1L,
            Collections.singletonList(menuProductRequest));

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("가격은 0원 이상이어야 한다.")
    void create_priceLessThanZero_thrownException() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 메뉴", BigDecimal.valueOf(-1), 1L,
            Collections.singletonList(menuProductRequest));

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("메뉴 그룹은 반드시 존재해야 한다.")
    void create_notExistMenuGroup_thrownException() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 메뉴", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupDao.existsById(anyLong())).thenReturn(false);

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 상품은 반드시 존재해야 한다.")
    void create_notExistProduct_thrownException() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 메뉴", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowingCallable createCallable = () -> menuService.create(menuRequest);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 메뉴의 가격은 메뉴 상품들의 수량 * 가격을 모두 합친 금액보다 작거나 같아야 한다.")
    void create_priceLessThanMenuProductPrice_thrownException() {
        //given
        MenuProduct menuProductRequest = menuProductCreateRequest(1L, 1L);
        Menu menuRequest = menuCreateRequest("짜장면 메뉴", BigDecimal.TEN, 1L,
            Collections.singletonList(menuProductRequest));

        when(menuGroupDao.existsById(menuRequest.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProductRequest.getProductId())).thenReturn(Optional.of(짜장면()));

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
        Menu 짜장면_세트 = 짜장면_세트();
        when(menuDao.findAll()).thenReturn(Collections.singletonList(짜장면_세트));

        //when
        menuService.list();

        //then
        verify(menuDao, only()).findAll();
        verify(menuProductDao, only()).findAllByMenuId(짜장면_세트.getId());
    }

    private void requestedMenuSave(Menu menuRequest) {
        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuDao, only()).save(menuCaptor.capture());
        assertThat(menuCaptor.getValue())
            .extracting(Menu::getName, Menu::getPrice)
            .containsExactly(menuRequest.getName(), menuRequest.getPrice());
    }

    private void requestedMenuProductSave(MenuProduct menuProductRequest) {
        ArgumentCaptor<MenuProduct> menuProductCaptor = ArgumentCaptor.forClass(MenuProduct.class);
        verify(menuProductDao, only()).save(menuProductCaptor.capture());
        assertThat(menuProductCaptor.getValue())
            .extracting(MenuProduct::getQuantity)
            .isEqualTo(menuProductRequest.getQuantity());
    }

    private Menu menuCreateRequest(String name, BigDecimal price, long menuGroupId,
        List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    private MenuProduct menuProductCreateRequest(long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
