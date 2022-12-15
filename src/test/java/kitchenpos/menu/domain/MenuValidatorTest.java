package kitchenpos.menu.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuPriceGreaterThanAmountException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("메뉴 유효성에 대한 테스트")
@DataJpaTest
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private MenuRequest request = MenuRequest.of(
            "후라이드치킨", BigDecimal.valueOf(16_000), 1L,
            Arrays.asList(MenuProductRequest.of(1L, 1))
    );

    @DisplayName("메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void checkMenuGroupExist() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.MENU_GROUP_NOT_FOUND);
    }

    @DisplayName("메뉴가격이 금액(상품가격 * 수량 의 총합)보다 크면 예외가 발생한다.")
    @Test
    void checkMenuPriceLessOrEqualTotalAmount() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.from("후라이드치킨")));
        when(productRepository.findById(any())).thenReturn(
                Optional.of(Product.of(1L, "후라이드치킨", BigDecimal.valueOf(14_000))));

        Assertions.assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(MenuPriceGreaterThanAmountException.class)
                .hasMessageStartingWith(ExceptionMessage.MENU_PRICE_GREATER_THAN_AMOUNT);
    }
}
