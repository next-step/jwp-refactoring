package kitchenpos.event;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.ordertable.event.OrderTableEventHandler;
import kitchenpos.ordertable.exception.IllegalOrderTableEmptyChangeException;
import kitchenpos.ordertable.exception.IllegalOrderTableIdRequestException;
import kitchenpos.ordertable.exception.OrderStatusNotCompleteException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.event.TableGroupCreatedEvent;
import kitchenpos.tablegroup.event.TableGroupUnlinkEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderTableEventHandlerTest {

    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    OrderRepository orderRepository;

    TableGroupCreatedEvent tableGroupCreatedEvent;
    OrderTableEventHandler orderTableEventHandler;
    TableGroupUnlinkEvent tableGroupUnlinkEvent;

    @BeforeEach
    void setUp() {
        tableGroupCreatedEvent = new TableGroupCreatedEvent(Collections.emptyList());
        orderTableEventHandler = new OrderTableEventHandler(orderTableRepository, orderRepository);
        tableGroupUnlinkEvent = new TableGroupUnlinkEvent(1L);
    }

    @DisplayName("주문 테이블이 1개 이하인 테이블 그룹을 생성하려는 경우")
    @Test
    void 주문_테이블이_1개_이하인_테이블_그룹을_생성() {
        //given
        tableGroupCreatedEvent = new TableGroupCreatedEvent(Collections.emptyList());

        //when, then
        assertThatThrownBy(() -> orderTableEventHandler.orderTablesEmptyChange(tableGroupCreatedEvent))
                .isInstanceOf(IllegalOrderTableEmptyChangeException.class);
    }

    @DisplayName("요청한 주문 테이블이 중복되거나 없는 경우")
    @Test
    void 요청한_주문_테이블이_중복되거나_없는_경우() {
        //given
        tableGroupCreatedEvent = new TableGroupCreatedEvent(
                Arrays.asList(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L),
                        new OrderTableIdRequest(3L)
                )
        );
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(
                Arrays.asList(
                        new OrderTable(1L),
                        new OrderTable(2L)
                        )
        );

        //when, then
        assertThatThrownBy(() -> orderTableEventHandler.orderTablesEmptyChange(tableGroupCreatedEvent))
                .isInstanceOf(IllegalOrderTableIdRequestException.class);
    }

    @DisplayName("주문의 상태값이 결제 완료가 아닌 경우")
    @Test
    void 주문의_상태값이_결제_완료가_아닌_경우() {
        //given
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(Arrays.asList(new OrderTable(1L, new TableGroup(), 4, true)));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

        assertThatThrownBy(() -> orderTableEventHandler.orderTableUnlinkTableGroup(tableGroupUnlinkEvent))
                .isInstanceOf(OrderStatusNotCompleteException.class);
    }
}
