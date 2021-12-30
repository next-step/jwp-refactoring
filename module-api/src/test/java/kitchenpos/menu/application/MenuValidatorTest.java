package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.MenuGroupNotFoundException;
import kitchenpos.menu.application.exception.ProductNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.exeption.InvalidPrice;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 등록 조건 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    private Product 매콤치킨;
    private MenuProduct 매콤치킨구성;
    private Menu 매콤치킨단품;

    @BeforeEach
    void setUp() {
        매콤치킨 = new Product("매콤치킨", BigDecimal.valueOf(13000));
        매콤치킨구성 = new MenuProduct(1L, 1L);
        매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(14000), 1L, Collections.singletonList(매콤치킨구성));
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않는 메뉴인 경우 예외가 발생한다.")
    void validateMenuGroup() {
        when(menuGroupRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> menuValidator.validate(매콤치킨단품))
                .isInstanceOf(MenuGroupNotFoundException.class);
        verify(menuGroupRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("메뉴에 속한 상품이 존재하지 않는 경우 예외가 발생한다.")
    void validateProduct() {
        when(menuGroupRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuValidator.validate(매콤치킨단품))
                .isInstanceOf(ProductNotFoundException.class);
        verify(menuGroupRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("메뉴의 가격이 상품 가격의 합보다 큰 경우 예외가 발생한다.")
    void validateProductsSum() {
        when(menuGroupRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨));

        assertThatThrownBy(() -> menuValidator.validate(매콤치킨단품))
                .isInstanceOf(InvalidPrice.class);
        verify(menuGroupRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
    }
}
