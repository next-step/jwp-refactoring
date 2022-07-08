package kitchenpos.table.domain;

import kitchenpos.tablegroup.event.TableGroupEventPublisher;
import kitchenpos.tablegroup.event.TableUnGroupEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTableGroupHandlerTest {

    @InjectMocks
    private OrderTableGroupHandler orderTableGroupHandler;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Long 테이블_그룹_ID;
    private OrderTable 비어있는_테이블;
    private OrderTable 테이블_그룹이_존재하는_테이블;

    @BeforeEach
    void setUp() {
        테이블_그룹_ID = 1L;
        비어있는_테이블 = OrderTable.of(2L, null, NumberOfGuests.of(0), Empty.of(true));
        테이블_그룹이_존재하는_테이블 = OrderTable.of(2L, 테이블_그룹_ID, NumberOfGuests.of(0), Empty.of(true));
    }

    @Test
    @DisplayName("테이블 그룹이 등록되는 경우 주문 테이블도 함께 등록 된다.")
    void group() {
        // given
        TableGroupEventPublisher tableGroupEventPublisher = new TableGroupEventPublisher(
                테이블_그룹_ID, Arrays.asList(비어있는_테이블.getId())
        );

        // when
        when(orderTableRepository.findByIdIn(any())).thenReturn(Arrays.asList(비어있는_테이블));
        orderTableGroupHandler.group(tableGroupEventPublisher);

        // then
        assertThat(비어있는_테이블.getTableGroupId()).isEqualTo(테이블_그룹_ID);
    }

    @Test
    @DisplayName("테이블 그룹이 등록되는 경우 조회되는 테이블 정보가 존재하지 않는다면 실패 된다.")
    void validateOrderTable() {
        // given
        TableGroupEventPublisher tableGroupEventPublisher = new TableGroupEventPublisher(
                테이블_그룹_ID, Arrays.asList(비어있는_테이블.getId())
        );

        // when
        when(orderTableRepository.findByIdIn(any())).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableGroupHandler.group(tableGroupEventPublisher))
                .withMessage(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH)
        ;
    }

    @Test
    @DisplayName("테이블 그룹을 해제 하는 경우 테이블 정보내 테이블 그룹 정보도 해제 된다.")
    void unGroup() {
        // given
        TableUnGroupEventPublisher tableUnGroupEventPublisher = new TableUnGroupEventPublisher(테이블_그룹_ID);

        // when
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(테이블_그룹이_존재하는_테이블));
        orderTableGroupHandler.unGroup(tableUnGroupEventPublisher);

        // then
        assertThat(테이블_그룹이_존재하는_테이블.getTableGroupId()).isNull();
    }
}
