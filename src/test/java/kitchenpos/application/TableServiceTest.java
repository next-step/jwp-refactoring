package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTable orderTable;
    private TableService tableService;
    private OrderTableChangeEmptyRequest emptyRequest = new OrderTableChangeEmptyRequest(true);
    private OrderTableChangeNumberOfGuestsRequest numberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(1);


    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @Test
    void 주문_테이블을_등록할_수_있다() {
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTable createOrderTable = tableService.create(new OrderTableRequest(1, true));

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
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTable.validateAlreadyTableGroup()).willThrow(IllegalArgumentException.class);

        ThrowingCallable 이미_단체_지정이_된_주문테이블_수정 = () -> tableService.changeEmpty(1L, emptyRequest);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문테이블_수정);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTable.validateOrderStatus(any())).willThrow(IllegalArgumentException.class);

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
        given(orderTable.changeNumberOfGuests(-1)).willThrow(IllegalArgumentException.class);
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
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTable.validateEmpty()).willThrow(IllegalArgumentException.class);

        ThrowingCallable 빈_테이블_수정 = () -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블_수정);
    }
}
