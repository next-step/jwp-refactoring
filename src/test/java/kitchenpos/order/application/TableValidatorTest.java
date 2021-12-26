package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;
import kitchenpos.order.domain.TableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 등록 조건 테스트")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @InjectMocks
    private TableValidator tableValidator;

    private OrderTable 테이블;
    private OrderTable 빈테이블;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable(1, new TableState(false));
        빈테이블 = new OrderTable(0, new TableState(true));
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTable() {
        assertThatThrownBy(() -> tableValidator.validate(빈테이블))
                .isInstanceOf(InvalidTableState.class);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 완료가 아닌 경우 예외가 발생한다.")
    void validateOrderStatus() {
        테이블.changeStatus(MEAL);

        assertThatThrownBy(() -> tableValidator.validate(테이블))
                .isInstanceOf(InvalidOrderState.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록되어 있는 경우 예외가 발생한다.")
    void validateExistTableGroup() {
        테이블.changeStatus(COMPLETION);
        테이블.group(1L);

        assertThatThrownBy(() -> tableValidator.validate(테이블))
                .isInstanceOf(InvalidTableState.class);
    }
}
