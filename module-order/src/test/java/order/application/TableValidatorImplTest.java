package order.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import common.exception.InvalidOrderStatusException;
import order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import table.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableValidatorImplTest {

    private TableValidatorImpl tableValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableValidator = new TableValidatorImpl(orderRepository);
    }

    @DisplayName("그룹되어 있으면 빈테이블로 변경할 수 없다.")
    @Test
    void changeEmpty_fail_group() {
        //given
        long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(1L, tableGroupId, 4, true);

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.checkValidChangeEmpty(orderTable));
    }

    @DisplayName("요리중, 식사중 주문이 있으면 변경할 수 없다.")
    @Test
    void changeEmpty_fail_invalidOrderStatus() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> tableValidator.checkValidChangeEmpty(orderTable));
    }

}