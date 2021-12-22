package kitchenpos.application.table;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.EmptyOrderTableException;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NegativeOfNumberOfGuestsException;
import kitchenpos.vo.OrderTableId;
import kitchenpos.vo.TableGroupId;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    @Mock
    private OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("주문테이블 유효성검사자는 주문테이블의 빈테이블 상태변경의 유효성을 검사하고 정합시 주문테이블이 생성된다.")
    @Test
    void generate_validatedOrderTable_changeEmpty() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(0, true);

        Orders 치킨주문 = Orders.of(OrderStatus.COMPLETION);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));
        when(orderService.findByOrderTableId(nullable(Long.class))).thenReturn(치킨주문);
        
        // when
        OrderTable orderTable = tableValidator.getValidatedOrderTableForChangeEmpty(치킨_주문테이블.getId());

        // then
        Assertions.assertThat(orderTable).isEqualTo(치킨_주문테이블);
    }

    @DisplayName("단체지정된 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_existOrderTableInTableGroup() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        TableGroup  단체지정_테이블 = TableGroup.of(1L);
        치킨_주문테이블.groupingTable(TableGroupId.of(단체지정_테이블.getId()));
        치킨2_주문_단체테이블.groupingTable(TableGroupId.of(단체지정_테이블.getId()));
        
        Orders 치킨주문 = Orders.of(OrderStatus.COMPLETION);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));
        when(orderService.findByOrderTableId(nullable(Long.class))).thenReturn(치킨주문);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> tableValidator.getValidatedOrderTableForChangeEmpty(치킨_주문테이블.getId()));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(10, false);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));
        when(orderService.findByOrderTableId(nullable(Long.class))).thenReturn(Orders.of(OrderTableId.of(치킨_주문테이블), OrderStatus.MEAL));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                    .isThrownBy(() -> tableValidator.getValidatedOrderTableForChangeEmpty(치킨_주문테이블.getId()));
    }

    @DisplayName("주문테이블 유효성검사자는 방문한 손님 수 변경의 유효성을 검사하고 정합시 주문테이블이 생성된다.")
    @Test
    void generate_validatedOrderTable_changeNumberOfGuests() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(10, false);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));
        
        // when
        OrderTable orderTable =  tableValidator.getValidatedOrderTableForChangeNumberOfGuests(치킨_주문테이블.getId(), 3);

        // then
        Assertions.assertThat(orderTable).isEqualTo(치킨_주문테이블);
    }

    @DisplayName("주문테이블의 방문한 손님수를 0이만으로 변경시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(10, false);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(NegativeOfNumberOfGuestsException.class)
                    .isThrownBy(() -> tableValidator.getValidatedOrderTableForChangeNumberOfGuests(치킨_주문테이블.getId(), numberOfGuests));

    }

    @DisplayName("빈테이블에 방문한 손님수 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨_주문테이블));

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderTableException.class)
                    .isThrownBy(() -> tableValidator.getValidatedOrderTableForChangeNumberOfGuests(치킨_주문테이블.getId(), 3));
    }
}
