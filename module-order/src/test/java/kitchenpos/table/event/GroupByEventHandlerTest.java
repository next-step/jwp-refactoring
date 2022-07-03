package kitchenpos.table.event;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.table.application.TableServiceTest.테이블_등록;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 관련 기능")
public class GroupByEventHandlerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private GroupByEventHandler groupByEventHandler;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 테이블_등록(2, true);
        orderTable2 = 테이블_등록(4, true);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 등록되어 있지 않으면 실패한다.")
    void createWithDifferentOrderTable() {
        // given
        GroupByEvent groupByEvent = GroupByEvent.of(1L, Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1));

        // when-then
        assertThatThrownBy(() -> groupByEventHandler.groupByEvent(groupByEvent))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 비어있지 않거나 다른 그룹에 등록되어 있으면 실패한다.")
    void createWithNotEmptyOrderTableOrNonNullTableGroupId() {
        // given
        orderTable1.changeEmpty(false);
        GroupByEvent groupByEvent = GroupByEvent.of(1L, Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when-then
        assertThatThrownBy(() -> groupByEventHandler.groupByEvent(groupByEvent)).isInstanceOf(IllegalArgumentException.class);
    }

}
