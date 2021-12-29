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
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.UngroupingTableEvent;

@ExtendWith(MockitoExtension.class)
class UngroupingTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UngroupingTableEventHandler ungroupingTableEventHandler;

    @DisplayName("테이블 그룹 제거 이벤트")
    @Test
    void handle() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 4, true),
            new OrderTable(2L, 1L, 2, true));
        테이블_그룹_ID로_조회_결과_반환(orderTables);

        요리_또는_식사중인_테이블_존재_여부_반환(false);

        UngroupingTableEvent ungroupingTableEvent = new UngroupingTableEvent(1L);

        // when and then
        assertThatCode(() -> ungroupingTableEventHandler.handle(ungroupingTableEvent))
            .doesNotThrowAnyException();
    }

    @DisplayName("요리 중이나 식사 중인 주문 테이블을 포함하고 있다면 삭제 불가능")
    @Test
    void handleFailWhenContainsMealOrCooking() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 4, true),
            new OrderTable(2L, 1L, 2, true));
        테이블_그룹_ID로_조회_결과_반환(orderTables);

        요리_또는_식사중인_테이블_존재_여부_반환(true);

        UngroupingTableEvent ungroupingTableEvent = new UngroupingTableEvent(1L);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> ungroupingTableEventHandler.handle(ungroupingTableEvent))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    private void 테이블_그룹_ID로_조회_결과_반환(List<OrderTable> orderTables) {
        Mockito.when(orderTableRepository.findAllByTableGroupId(Mockito.anyLong()))
            .thenReturn(orderTables);
    }

    private void 요리_또는_식사중인_테이블_존재_여부_반환(boolean expected) {
        Mockito.when(orderRepository.existsByOrderTableInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList()))
            .thenReturn(expected);
    }
}