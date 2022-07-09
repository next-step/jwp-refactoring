package kitchenpos.order.application;

import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorImplTest {

    @InjectMocks
    private TableGroupValidatorImpl tableGroupValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void 주문_상태가_조리_또는_식사중인_테이블을_해제하는_경우() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupValidator.validateUngroup(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

}