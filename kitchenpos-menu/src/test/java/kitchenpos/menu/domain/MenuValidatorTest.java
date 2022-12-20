package kitchenpos.menu.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import product.domain.Product;
import product.domain.ProductRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.utils.Message.INVALID_MENU_PRICE;
import static kitchenpos.utils.Message.NOT_EXISTS_MENU;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
            "honeycomboChicken", BigDecimal.valueOf(18000), 1L,
            Arrays.asList(MenuProductRequest.of(1L, 1))
    );

    @DisplayName("메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void checkMenuGroupExist() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_MENU);
    }

    @DisplayName("메뉴가격이 금액(상품가격 * 수량 의 총합)보다 크면 예외가 발생한다.")
    @Test
    void checkMenuPriceLessOrEqualTotalAmount() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.from("honeycomboChicken")));
        when(productRepository.findById(any())).thenReturn(
                Optional.of(Product.of(1L, "honeycomboChicken", BigDecimal.valueOf(14_000))));

        Assertions.assertThatThrownBy(() -> menuValidator.validate(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_MENU_PRICE);
    }
}
