package kitchenpos.domain;

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

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NegativeOfNumberOfGuestsException;
import kitchenpos.ordertable.vo.OrderTableId;
import kitchenpos.tablegroup.vo.TableGroupId;
import kitchenpos.validation.TableValidator;

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
