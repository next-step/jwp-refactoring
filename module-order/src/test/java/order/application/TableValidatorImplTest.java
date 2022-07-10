package order.application;

import order.repository.OrderRepository;
import table.domain.OrderTable;
import table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableValidatorImplTest {

    @InjectMocks
    private TableValidatorImpl tableValidator;

    @Mock
    private OrderRepository orderRepository;

    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = new OrderTable(1L, null, 0, true);
    }

    @Test
    void 단체_지정된_주문_테이블의_상태를_변경할_경우() {
        // given
        주문_테이블 = new OrderTable(1L, new TableGroup(1L), 3, false);

        // when & then
        assertThatThrownBy(() -> tableValidator.validateOrderTable(주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조리_또는_식사중인_상태의_테이블을_변경하는_경우() {
        // given
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableValidator.validateOrderTable(주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

}