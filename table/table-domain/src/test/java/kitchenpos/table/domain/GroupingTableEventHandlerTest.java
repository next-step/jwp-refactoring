package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.tablegroup.domain.GroupingTableEvent;

@ExtendWith(MockitoExtension.class)
class GroupingTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private GroupingTableEventHandler groupingTableEventHandler;

    @DisplayName("테이블 그룹화")
    @Test
    void handle() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, null, 2, true));
        테이블_조회_결과_반환(orderTables);

        GroupingTableEvent event = new GroupingTableEvent(1L, Arrays.asList(1L, 2L));

        // when and then
        groupingTableEventHandler.handle(event);

        // then
        Mockito.verify(orderTableRepository).saveAll(Mockito.anyList());
    }

    @DisplayName("다른 그룹에 등록되어 있는 주문 테이블이 포함되어 있는 경우 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTableIsInOtherGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, 1L, 2, true));
        테이블_조회_결과_반환(orderTables);

        GroupingTableEvent event = new GroupingTableEvent(1L, Arrays.asList(1L, 2L));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> groupingTableEventHandler.handle(event))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    private void 테이블_조회_결과_반환(List<OrderTable> orderTables) {
        Mockito.when(orderTableRepository.findAllByIdIn(Mockito.anyList()))
            .thenReturn(orderTables);
    }
}