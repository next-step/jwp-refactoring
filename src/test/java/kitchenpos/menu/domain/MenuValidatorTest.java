package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    MenuValidator menuValidator;

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    Product 후라이드;
    MenuRequest 상품;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));

        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(15000), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));
    }

    @Test
    @DisplayName("주문내역의 메뉴가 모두 존재하지 않으면 오류를 반환한다")
    void create_nonMenuError() {
        // given
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validateOrderLineItemsCheck(Arrays.asList(1L, 2L))
        ).withMessageContaining("존재하지 않는 메뉴입니다.");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 메뉴그룹 정보를 가지고 있다")
    void create_nonExistMenuGroupError() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validateMenuGroupCheck(1L)
        ).withMessageContaining("존재하지 않는 메뉴그룹입니다.");
    }


    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 상품 정보를 가져야 한다")
    void create_nonProductInfoError() {
        // given
        given(productRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validatePriceCheck(상품)
        ).withMessageContaining("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴상품에 속한 상품들의 금액 합보다 메뉴 가격이 작아야 한다")
    void create_totalPriceError() {
        // given
        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(20000), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(후라이드));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validatePriceCheck(상품)
        ).withMessageContaining("메뉴의 금액은 상품의 합 보다 작아야합니다.");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴의 금액은 0원 이상이다")
    void create_priceException() {
        // given
        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(-1), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validatePriceCheck(상품)
        );
    }
}
