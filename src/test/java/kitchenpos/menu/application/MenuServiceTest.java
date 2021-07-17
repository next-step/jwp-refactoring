package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
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

    @Test
    void given_Menu_when_Create_then_SaveExecuted() {
        // given
        final List<MenuProductRequest> menuProductRequests = Collections.singletonList(new MenuProductRequest(1L, 1));
        final MenuRequest menuRequest = new MenuRequest("name", BigDecimal.ZERO, 1L, menuProductRequests);
        final MenuGroup menuGroup = new MenuGroup();
        final Menu savedMenu = new Menu("name", menuRequest.getPrice(), menuGroup.getId());
        given(menuRepository.save(any(Menu.class))).willReturn(savedMenu);
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(new Product("name", new BigDecimal(100))));

        // when
        menuService.create(menuRequest);

        // then
        verify(menuRepository).save(any(Menu.class));
    }

    @DisplayName("금액이 null, -1, 0 인 경우 예외 발생 테스트")
    @ParameterizedTest
    @MethodSource("providePrice")
    void given_InvalidPrice_when_Create_then_ThrownException(final BigDecimal price) {
        // given
        final MenuRequest minusPrice = new MenuRequest("name", price, 1L, new ArrayList<>());

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
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        final MenuRequest invalidPrice = new MenuRequest("name", new BigDecimal(100), 1L,
            Collections.singletonList(menuProductRequest));
        final Product product = new Product("name", new BigDecimal(1));

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
        verify(menuRepository).findAll();
    }
}
