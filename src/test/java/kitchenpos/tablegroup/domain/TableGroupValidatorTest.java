package kitchenpos.tablegroup.domain;

import kitchenpos.exception.CannotUpdateException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.common.Message.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    private final boolean 비어있음 = true;
    private final boolean 비어있지_않음 = false;
    private final OrderTable 단체지정_안된_비어있는_테이블 = new OrderTable(null, 3, 비어있음);
    private final OrderTable 단체지정_안된_비어있지_않은_테이블 = new OrderTable(null, 3, 비어있지_않음);
    private final OrderTable 단체지정_된_비어있지_않은_첫번째_테이블 = new OrderTable(1L, 3, 비어있지_않음);
    private final OrderTable 단체지정_된_비어있지_않은_두번째_테이블 = new OrderTable(1L, 4, 비어있지_않음);

    private TableGroupRequest 테이블그룹_요청;

    @DisplayName("단체지정시 주문 테이블 목록이 비어있는 경우, 예외가 발생한다")
    @Test
    void 주문테이블_목록_비어있는_경우_예외발생() {
        //Given
        테이블그룹_요청 = new TableGroupRequest(1L, LocalDateTime.now(), new ArrayList<>());
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Collections.EMPTY_LIST);

        //When + Then
        Throwable 주문테이블_비어있음_예외 = catchThrowable(() -> tableGroupValidator.validateGrouping(new TableGroupRequest()));
        assertThat(주문테이블_비어있음_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
    }

    @DisplayName("단체지정시 주문 테이블 목록이 2개 미만인 경우, 예외가 발생한다")
    @Test
    void 주문테이블_목록_2개_미만_예외발생() {
        //Given
        테이블그룹_요청 = new TableGroupRequest(1L, LocalDateTime.now(), Arrays.asList(1L));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(단체지정_안된_비어있지_않은_테이블));

        //When + Then
        Throwable 주문테이블_2개미만_예외 = catchThrowable(() -> tableGroupValidator.validateGrouping(테이블그룹_요청));
        assertThat(주문테이블_2개미만_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
    }

    @DisplayName("단체지정 시 이미 단체 지정된 주문 테이블을 입력한 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_이미_단체지정된_경우_예외발생() {
        //Given
        테이블그룹_요청 = new TableGroupRequest(1L, LocalDateTime.now(), Arrays.asList(1L, 2L));
        List<OrderTable> 테이블목록 = Arrays.asList(단체지정_된_비어있지_않은_첫번째_테이블, 단체지정_안된_비어있지_않은_테이블);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(테이블목록);


        //When + Then
        Throwable 이미_단체지정된_테이블_예외 = catchThrowable(() -> tableGroupValidator.validateGrouping(테이블그룹_요청));
        assertThat(이미_단체지정된_테이블_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
    }

    @DisplayName("단체지정 시 주문 테이블이 비어있지 않은 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_비어있지_않은_경우_예외발생() {
        //Given
        테이블그룹_요청 = new TableGroupRequest(1L, LocalDateTime.now(), Arrays.asList(1L, 2L));
        List<OrderTable> 테이블목록 = Arrays.asList(단체지정_안된_비어있지_않은_테이블, 단체지정_안된_비어있는_테이블);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(테이블목록);

        //When + Then
        Throwable 이미_단체지정된_테이블_예외 = catchThrowable(() -> tableGroupValidator.validateGrouping(테이블그룹_요청));
        assertThat(이미_단체지정된_테이블_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
    }

    @DisplayName("단체지정 해제시 주문이 완료되지 않은 경우, 예외가 발생한다")
    @Test
    void 주문이_완료되지_않은_경우_단체지정_해제_예외발생() {
        //Given
        List<OrderTable> 테이블목록 = Arrays.asList(단체지정_된_비어있지_않은_첫번째_테이블, 단체지정_된_비어있지_않은_두번째_테이블);
        List<Order> 주문목록 = Arrays.asList(new Order(1L, OrderStatus.COMPLETION), new Order(1L, OrderStatus.MEAL));
        when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(테이블목록);
        when(orderRepository.findAllByOrderTableIdIn(any())).thenReturn(주문목록);

        //When + Then
        Throwable 이미_단체지정된_테이블_예외 = catchThrowable(() -> tableGroupValidator.validateUngrouping(1L));
        assertThat(이미_단체지정된_테이블_예외).isInstanceOf(CannotUpdateException.class)
                .hasMessage(ERROR_TABLE_GROUP_CANNOT_BE_UNGROUPED_WHEN_ORDERS_NOT_COMPLETED.showText());
    }
}
