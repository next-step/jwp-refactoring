package kitchenpos.menu.domain;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴 가격이 null이나 음수가 들어오면 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    void validateInvalidPrice(final BigDecimal invalidPrice) {
        // given
        final Menu menu = new Menu("후라이드치킨", invalidPrice, 1L, singletonList(new MenuProduct(1L, 1L)));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuValidator.validate(menu))
            .withMessageContaining("가격");
    }

    private static Stream<BigDecimal> provideInvalidPrice() {
        return Stream.of(null, new BigDecimal(-1));
    }

    @DisplayName("메뉴 가격이 상품의 가격의 총합보다 크면 예외 발생")
    @Test
    void validateOverPrice() {
        // given
        final Menu menu = new Menu("후라이드치킨", new BigDecimal(16001), 1L,
            singletonList(new MenuProduct(1L, 1L)));
        when(productRepository.findById(anyLong()))
            .thenReturn(Optional.of(new Product("치킨", new BigDecimal(16000))));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuValidator.validate(menu))
            .withMessageContaining("메뉴의 가격이 상품 가격 전체의 총합보다 클 수 없습니다");
    }

    @DisplayName("메뉴 그룹을 찾을 수 없을 때 예외 발생")
    @Test
    void validateMenuGroup() {
        // given
        final Menu menu = new Menu("후라이드치킨", new BigDecimal(16000), 1L, singletonList(new MenuProduct(1L, 1L)));
        when(productRepository.findById(anyLong()))
            .thenReturn(Optional.of(new Product("치킨", new BigDecimal(16000))));
        when(menuGroupRepository.existsById(anyLong()))
            .thenReturn(false);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuValidator.validate(menu))
            .withMessageContaining("메뉴 그룹을 찾을 수 없습니다");
    }
}
