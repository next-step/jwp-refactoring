package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.EmptyOrderTableException;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NegativeOfNumberOfGuestsException;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문테이블이 생성된다.")
    @Test
    void create_orderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(치킨_주문_단체테이블);

        OrderTableDto 주문테이블_생성전문 = OrderTableDto.of(0);

        // when
        OrderTableDto createdOrderTable = tableService.create(주문테이블_생성전문);

        // then
        assertAll(
            () -> Assertions.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(0),
            () -> Assertions.assertThat(createdOrderTable.getEmpty()).isTrue()  
        );
    }

    @DisplayName("주문테이블이 조회된다.")
    @Test
    void search_orderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findAll()).thenReturn(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));

        // when
        List<OrderTableDto> searchedOrderTable = tableService.list();

        // then
        Assertions.assertThat(searchedOrderTable).isEqualTo(List.of(OrderTableDto.of(치킨_주문_단체테이블), OrderTableDto.of(치킨2_주문_단체테이블)));
    }

    //Todo Exception
    @DisplayName("주문테이블이 빈테이블 전환 여부가 변경된다.")
    @Test
    void update_orderTable_emptyStatus() {
        // given
        OrderTable 치킨_주문_개인테이블 =  OrderTable.of(10, false);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_개인테이블));

        OrderTableDto 주문테이블_빈테이블전환_요청전문 = OrderTableDto.of(0);

        // when
        OrderTableDto changedOrderTable = tableService.changeEmpty(치킨_주문_개인테이블.getId(), 주문테이블_빈테이블전환_요청전문);

        // then
        Assertions.assertThat(changedOrderTable.getEmpty()).isTrue();
    }

    @DisplayName("단체지정된 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_existOrderTableInTableGroup() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);
        TableGroup 단체주문테이블 = TableGroup.of(Lists.newArrayList(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        치킨_주문_단체테이블.groupingTable(단체주문테이블);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_단체테이블));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> tableService.changeEmpty(치킨_주문_단체테이블.getId(), OrderTableDto.of(치킨_주문_단체테이블)));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_단체테이블));
        when(orderService.isNotCompletionOrder(nullable(Long.class))).thenReturn(true);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                      .isThrownBy(() -> tableService.changeEmpty(치킨_주문_단체테이블.getId(), OrderTableDto.of(치킨_주문_단체테이블)));
    }

    @DisplayName("주문테이블의 방문한 손님수가 변경된다.")
    @Test
    void update_orderTable_numberOfGuests() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_단체테이블));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(치킨_주문_단체테이블);

        OrderTableDto 손님수변경_요청전문 = OrderTableDto.of(3);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(치킨_주문_단체테이블.getId(), 손님수변경_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3),
            () -> Assertions.assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블의 방문한 손님수를 0이만으로 변경시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_단체테이블));

        치킨_주문_단체테이블.changeNumberOfGuests(numberOfGuests);

        // when
        // then
        Assertions.assertThatExceptionOfType(NegativeOfNumberOfGuestsException.class)
                    .isThrownBy(() -> tableService.changeNumberOfGuests(치킨_주문_단체테이블.getId(), OrderTableDto.of(치킨_주문_단체테이블)));

    }

    @DisplayName("빈테이블에 방문한 손님수 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문_단체테이블));

        치킨_주문_단체테이블.changeNumberOfGuests(3);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderTableException.class)
                   .isThrownBy(() -> tableService.changeNumberOfGuests(치킨_주문_단체테이블.getId(), OrderTableDto.of(치킨_주문_단체테이블)));
    }
}
