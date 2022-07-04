package kitchenpos.ordertable.event;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UngroupTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UngroupTableEventHandler ungroupTableEventHandler;

    private TableGroup 단체_테이블;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;

    @BeforeEach
    void setUp() {
        테이블_1 = createOrderTable(1L, null, 0, true);
        테이블_2 = createOrderTable(2L, null, 0, true);
        단체_테이블 = createTableGroup(1L, Arrays.asList(테이블_1, 테이블_2));
    }

    @DisplayName("테이블그룹 해제 Event 성공")
    @Test
    void 테이블그룹_해제_성공(){
        //given
        given(orderTableRepository.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(테이블_1, 테이블_2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        UngroupTableEvent event = UngroupTableEvent.from(단체_테이블.getId());

        //when
        ungroupTableEventHandler.handle(event);

        //then
        Assertions.assertThat(테이블_1.getTableGroupId()).isNull();
        Assertions.assertThat(테이블_2.getTableGroupId()).isNull();
    }

    @DisplayName("테이블그룹 해제 Event 실패: COOKING이나 MEAL 상태의 주문이 있으면 삭제할 수 없다")
    @Test
    void 테이블그룹_해제_실패_주문_상태_검증(){
        //given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
        UngroupTableEvent event = UngroupTableEvent.from(단체_테이블.getId());

        //then
        assertThrows(IllegalOrderException.class, () -> ungroupTableEventHandler.handle(event));
    }
}
