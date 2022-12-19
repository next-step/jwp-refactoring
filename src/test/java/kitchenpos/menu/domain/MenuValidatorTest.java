package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.message.MenuMessage;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private MenuCreateRequest menuCreateRequest;

    @BeforeEach
    void setUp() {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                new MenuProductRequest(1L, 1L),
                new MenuProductRequest(2L, 1L)
        );
        menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(19_000L), 1L, menuProductRequests);
    }

    @Test
    @DisplayName("메뉴 생성 검사시 메뉴 그룹이 누락 된 경우 예외처리한다")
    void validateMenuThrownByEmptyMenuGroupTest() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(false);

        // when
        assertThatThrownBy(() -> menuValidator.validate(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.CREATE_MENU_ERROR_MENU_GROUP_MUST_BE_NON_NULL.message());

        // then
        then(menuGroupRepository).should(times(1)).existsById(any());
    }

    @Test
    @DisplayName("메뉴 생성 검사시 메뉴 가격이 등록된 상품 요금의 합산된 금액보다 클경우 예외처리한다")
    void validateMenuThrownByInValidPriceTest() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(1L)).willReturn(Optional.of(Product.of("후라이드", 16_000L)));
        given(productRepository.findById(2L)).willReturn(Optional.of(Product.of("콜라", 2_000L)));

        // when
        // 메뉴 가격 - 19,000, 등록 된 상품 요금합 - 18,000
        assertThatThrownBy(() -> menuValidator.validate(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuMessage.ADD_PRODUCT_ERROR_IN_VALID_PRICE.message());

        // then
        then(menuGroupRepository).should(times(1)).existsById(any());
        then(productRepository).should(times(2)).findById(any());
    }

    @Test
    @DisplayName("메뉴 생성 검사시 상품이 등록 안된 경우 예외처리한다")
    void validateMenuThrownByNotFoundProductTest() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> menuValidator.validate(menuCreateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(menuGroupRepository).should(times(1)).existsById(any());
        then(productRepository).should(times(1)).findById(any());
    }
}
