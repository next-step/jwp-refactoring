package kitchenpos.event;

import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.event.OrderTableEventHandler;
import kitchenpos.ordertable.exception.IllegalOrderTableEmptyChangeException;
import kitchenpos.tablegroup.event.TableGroupCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableEventHandlerTest {

    @Mock
    OrderTableRepository orderTableRepository;

    TableGroupCreatedEvent tableGroupCreatedEvent;
    OrderTableEventHandler orderTableEventHandler;

    @BeforeEach
    void setUp() {
        tableGroupCreatedEvent = new TableGroupCreatedEvent(Collections.emptyList());
        orderTableEventHandler = new OrderTableEventHandler(orderTableRepository);
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
}
