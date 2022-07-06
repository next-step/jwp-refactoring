package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 - 식당의 테이블")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {


    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성.")
    void createOrderTable() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        OrderTable orderTable = new OrderTable(1L,null,0, false);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //when
        final OrderTableResponse saveOrderTable = tableService.create(orderTableRequest);

        //then
        assertAll(
                () -> assertThat(saveOrderTable.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(saveOrderTable.getTableGroupId()).isNull()

        );

    }

    @Test
    @DisplayName("주문 테이블을 조회한다.")
    void orderList() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, false);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        //when
        final List<OrderTableResponse> list = tableService.list();

        //then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).extracting("id").contains(orderTable1.getId(), orderTable2.getId())
        );

    }


    @Test
    @DisplayName("존재하지 않은 테이블인 경우에는 빈테이블로 변경 할수 없다.")
    void emptyTable() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tableService.changeEmpty(1L));

    }


    @Test
    @DisplayName("단체지정인 테이블인 경우에는 변경 할수 없다.")
    void changEmptyGroupTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(false);


        //when & then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeEmpty(1L));

    }


    @Test
    @DisplayName("주문 상태가 조리중인 경우 변경할 수 없다.")
    void notChangeTableStatusIsMeal() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        //주문상태가 조리중 경우
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(false);

        //when & then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeEmpty(1L));
    }

    @Test
    @DisplayName("주문 테이블을 빈테이블로 변경한다.")
    void changeEmptyTable(){
        //givne
        OrderTable orderTable = new OrderTable(1L,null,3, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(false);


        //when
        tableService.changeEmpty(1L);

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("방문자수가 음수 일 경우 변경할 수 없다.")
    void changeGuestNumberMinus() {
        //given
        final OrderTable savedOrderTable = new OrderTable(1, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest( -1, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(savedOrderTable));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                    tableService.changeNumberOfGuests(1L, orderTableRequest)
                );
    }

    @Test
    @DisplayName("존재하지 않은 테이블인 경우에는 방문자를 변경 할수 없다.")
    void existTableChangeGuestNumber() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest( 1, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() ->
                tableService.changeNumberOfGuests(1L, orderTableRequest)
        );
    }

    @Test
    @DisplayName("해당 주문테이블이 비어 있을경우 방문자를 변경 할 수 없다.")
    void emptyTableChangeGuestNumber() {
        //given
        final OrderTable savedOrderTable = new OrderTable(1, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest( 2, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(savedOrderTable));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                tableService.changeNumberOfGuests(1L, orderTableRequest)
        );
    }

    @Test
    @DisplayName("해당 주문테이블이 방문자수가 변경이 된다.")
    void changeNumberOfGuests() {
        //given
        final OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);
        OrderTableRequest orderTableRequest = new OrderTableRequest( 2, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(savedOrderTable));

        //when
        final OrderTableResponse changeOrderTable = tableService.changeNumberOfGuests(1L, orderTableRequest);

        //then
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(2L);
    }


}
