package kitchenpos.validator;

import kitchenpos.common.exception.NotFoundMenuException;
import kitchenpos.common.exception.NotFoundOrderTableException;
import kitchenpos.common.exception.NotOrderedEmptyTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;

import static kitchenpos.order.domain.OrderTest.주문통합;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderValidatorTest {
    @Autowired
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 검증")
    void validate() {
        assertDoesNotThrow(() -> orderValidator.validate(주문통합));
    }

    @Test
    @DisplayName("등록이 안된 주문 테이블에서는 주문을 할 수 없다.")
    void notFoundTableException() {
        assertThrows(NotFoundOrderTableException.class, () -> orderValidator.validateEmptyTableByTableId(100L));
    }

    @Test
    @DisplayName("빈 테이블에서는 주문을 등록 할 수 없다.")
    void emptyTableException() {
        assertThrows(NotOrderedEmptyTableException.class, () -> orderValidator.validateEmptyTableByTableId(1L));
    }

    @Test
    @DisplayName("등록이 안된 메뉴는 주문 할 수 없다.")
    void notFoundMenuException() {
        // given
        HashSet<Long> menuIds = new HashSet<>(Arrays.asList(1L, 100L));
        // when
        // then
        assertThrows(NotFoundMenuException.class, () -> orderValidator.validateOrderLineItemByMenuIds(menuIds));
    }

}