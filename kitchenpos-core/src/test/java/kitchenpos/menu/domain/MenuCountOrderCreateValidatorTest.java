package kitchenpos.menu.domain;

import kitchenpos.menu.domain.validator.MenuCountOrderCreateValidator;
import kitchenpos.order.domain.Order;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.order.application.OrderServiceFixture.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 생성시 메뉴 갯수 유효성 테스트")
@ExtendWith(MockitoExtension.class)
class MenuCountOrderCreateValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuCountOrderCreateValidator validator;

    @DisplayName("요청한 메뉴 아이디의 갯수가 실제 메뉴의 갯수와 다르다면 유효하지 못하다.")
    @Test
    void validateFailNotFoundOrderTable() {
        // given
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3),
                getOrderLineItem(2L, 4))
        );
        given(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).willReturn(1);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> validator.validate(order);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

    @DisplayName("요청한 메뉴 아이디가 모두 존재하면 유효하다.")
    @Test
    void validate() {
        // given
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3),
                getOrderLineItem(2L, 4))
        );
        given(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2);
        // when
        final Executable executable = () -> validator.validate(order);
        // then
        assertDoesNotThrow(executable);
    }
}

