package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    void given_Menu_when_Create_then_SaveExecuted() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.ZERO);
        menu.setMenuProducts(new ArrayList<>());
        Menu savedMenu = new Menu();
        savedMenu.setId(1L);
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(menuDao.save(menu)).thenReturn(savedMenu);

        // when
        menuService.create(menu);

        // then
        verify(menuDao).save(menu);
    }

    @Test
    void given_InvalidMenu_when_Create_then_ThrownException() {
        // given
        Menu minusPrice = new Menu();
        minusPrice.setPrice(new BigDecimal(-1));
        // when
        final Throwable minusPriceException = catchThrowable(() -> menuService.create(minusPrice));
        // then
        assertThat(minusPriceException).isInstanceOf(IllegalArgumentException.class);

        // given
        Menu nullPrice = new Menu();
        nullPrice.setPrice(null);
        // when
        final Throwable nullPriceException = catchThrowable(() -> menuService.create(nullPrice));
        // then
        assertThat(nullPriceException).isInstanceOf(IllegalArgumentException.class);

        // given
        Menu notExistGroupId = new Menu();
        notExistGroupId.setPrice(BigDecimal.ZERO);
        when(menuGroupDao.existsById(notExistGroupId.getMenuGroupId())).thenReturn(false);
        // when
        final Throwable notExistGroupIdException = catchThrowable(() -> menuService.create(notExistGroupId));
        // then
        assertThat(notExistGroupIdException).isInstanceOf(IllegalArgumentException.class);

        // given
        Menu invalidPrice = new Menu();
        invalidPrice.setPrice(new BigDecimal(100));
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1L);
        invalidPrice.setMenuProducts(Collections.singletonList(menuProduct));
        final Product product = new Product();
        product.setPrice(new BigDecimal(1));
        when(menuGroupDao.existsById(invalidPrice.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProduct.getProductId())).thenReturn(Optional.of(product));
        // when
        final Throwable invalidPriceException = catchThrowable(() -> menuService.create(invalidPrice));
        // then
        assertThat(invalidPriceException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // when
        menuService.list();

        // then
        verify(menuDao).findAll();
    }
}
