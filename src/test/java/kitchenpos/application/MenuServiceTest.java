package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(menuDao.save(menu)).willReturn(savedMenu);

        // when
        menuService.create(menu);

        // then
        verify(menuDao).save(menu);
    }

    @DisplayName("금액이 null, -1, 0 인 경우 예외 발생 테스트")
    @ParameterizedTest
    @MethodSource("providePrice")
    void given_InvalidPrice_when_Create_then_ThrownException(BigDecimal price) {
        // given
        Menu minusPrice = new Menu();
        minusPrice.setPrice(price);

        // when
        final Throwable minusPriceException = catchThrowable(() -> menuService.create(minusPrice));

        // then
        assertThat(minusPriceException).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> providePrice() {
        return Stream.of(
            Arguments.of((Object)null),
            Arguments.of(new BigDecimal(-1)),
            Arguments.of(new BigDecimal(0))
        );
    }

    @DisplayName("메뉴 금액이 상품 금액보다 큰 경우 예외가 발생하는지 테스트")
    @Test
    void given_WrongPrice_when_Create_then_ThrownException() {
        // given
        Menu invalidPrice = new Menu();
        invalidPrice.setPrice(new BigDecimal(100));
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1L);
        invalidPrice.setMenuProducts(Collections.singletonList(menuProduct));
        final Product product = new Product();
        product.setPrice(new BigDecimal(1));
        given(menuGroupDao.existsById(invalidPrice.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

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
