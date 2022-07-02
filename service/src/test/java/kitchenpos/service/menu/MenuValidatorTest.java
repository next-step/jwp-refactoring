package kitchenpos.service.menu;

import kitchenpos.domain.menu.InvalidPriceException;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.service.menu.application.MenuValidator;
import kitchenpos.service.menu.dto.MenuProductRequest;
import kitchenpos.service.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴 그룹에 속하지 않으면 메뉴를 추가할 수 없다")
    void create_failed_1() {
        //given
        given(menuGroupRepository.existsById(any())).willReturn(false);

        //then
        assertThatThrownBy(() -> menuValidator.validate(
                new MenuRequest("name", 10L, 0L, Collections.emptyList()))).isExactlyInstanceOf(
                NoSuchElementException.class);
    }

    @Test
    @DisplayName("메뉴 상품의 금액보다 메뉴 가격이 크거나 같으면 메뉴로 추가할 수 없다")
    void create_failed_2() {
        //given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(1L)).willReturn(Optional.of(new Product(1L, "product1", 200)));
        given(productRepository.findById(2L)).willReturn(Optional.of(new Product(2L, "product2", 500)));

        //then
        assertThatThrownBy(() -> menuValidator.validate(new MenuRequest("name", 10000L, 0L,
                Arrays.asList(new MenuProductRequest(1L, 5), new MenuProductRequest(2L, 3))))).isExactlyInstanceOf(
                InvalidPriceException.class);
    }

    @Test
    @DisplayName("메뉴 상품 중 조회 되지 않는 경우가 있으면 메뉴로 추가할 수 없다")
    void create_failed_3() {
        //given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> menuValidator.validate(new MenuRequest("name", 10000L, 0L,
                Arrays.asList(new MenuProductRequest(3L, 5), new MenuProductRequest(2L, 3))))).isExactlyInstanceOf(
                NoSuchElementException.class);
    }

}
