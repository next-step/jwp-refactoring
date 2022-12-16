package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTable orderTable;
    private TableService tableService;
    private OrderTableChangeEmptyRequest emptyRequest = new OrderTableChangeEmptyRequest(true);
    private OrderTableChangeNumberOfGuestsRequest numberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(1);


    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository);
    }

    @Test
    void 주문_테이블을_등록할_수_있다() {
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTable createOrderTable = tableService.create(new OrderTableRequest(null, 1, true));

        assertThat(createOrderTable).isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(orderTable));

        List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables).hasSize(1),
                () -> assertThat(orderTables).containsExactly(orderTable)
        );
    }

    @Test
    void 주문_테이블의_비어있음_여부를_수정할_수_있다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTable changeEmptyOrderTable = tableService.changeEmpty(1L, emptyRequest);

        assertThat(changeEmptyOrderTable).isEqualTo(orderTable);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_비어있음_여부를_수정할_수_있다() {
        given(orderTableRepository.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문테이블_수정 = () -> tableService.changeEmpty(1L, emptyRequest);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문테이블_수정);
    }


    @Test
    void 이미_단체_지정이_된_주문테이블은_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroup(new TableGroup(Arrays.asList(new OrderTable(1, true), new OrderTable(1, true))));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        ThrowingCallable 이미_단체_지정이_된_주문테이블_수정 = () -> tableService.changeEmpty(1L, emptyRequest);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문테이블_수정);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.addOrder(new Order(new OrderTable(1, false), Collections.singletonList(new OrderLineItem(1L, 1))));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        ThrowingCallable 조리_식사_상태의_주문이_포함_된_주문테이블_수정 = () -> tableService.changeEmpty(1L, emptyRequest);

        assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_된_주문테이블_수정);
    }

    @Test
    void 주문_테이블의_방문한_손님수를_수정할_수_있다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTable changeOrderTable = tableService.changeNumberOfGuests(1L, numberOfGuestsRequest);

        assertThat(changeOrderTable).isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블의_방문한_손님수를_0명_이하로_수정할_수_없다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        doThrow(IllegalArgumentException.class).when(orderTable).changeNumberOfGuests(-1);
        numberOfGuestsRequest.setNumberOfGuests(-1);

        ThrowingCallable 손님수_0명_이하로_수정 = () -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest);

        assertThatIllegalArgumentException().isThrownBy(손님수_0명_이하로_수정);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_방문한_손님수를_수정할_수_있다() {
        given(orderTableRepository.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문_테이블_수정 = () -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문_테이블_수정);
    }

    @Test
    void 빈_테이블은_수정할_수_없다() {
        OrderTable 빈_주문_테이블 = new OrderTable(1, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_주문_테이블));

        ThrowingCallable 빈_테이블_수정 = () -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블_수정);
    }
}
