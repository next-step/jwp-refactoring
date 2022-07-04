package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table_group.application.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorImplTest {

    private TableGroupValidator tableGroupValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidatorImpl(orderRepository);
    }

    @DisplayName("주문상태가 조리나 식사이면 그룹 해제할 수 없다.")
    @Test
    void checkValidUngroup_fail_invalidOrderStatus() {
        //given
        Long tableId1 = 1L;
        Long tableId2 = 2L;

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
            .isThrownBy(() -> tableGroupValidator.checkValidUngroup(Arrays.asList(tableId1, tableId2)));
    }

}