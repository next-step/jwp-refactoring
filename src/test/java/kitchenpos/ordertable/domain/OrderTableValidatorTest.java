package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.ordertable.testfixtures.TableTestFixtures;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @DisplayName("테이블이 주문종료 상태이면 예외")
    @Test
    void validateNotOrderClosedTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        TableTestFixtures.특정_주문테이블_조회_모킹(orderTableRepository, orderTable);

        //when, then
        assertThatThrownBy(
            () -> orderTableValidator.validateNotOrderClosedTable(orderTable.getId()))
            .isInstanceOf(ClosedTableOrderException.class);
    }
}
