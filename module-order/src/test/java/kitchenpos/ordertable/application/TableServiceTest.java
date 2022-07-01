package kitchenpos.ordertable.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTableValidator orderTableValidator;
    @InjectMocks
    private TableService tableService;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
    }

    @DisplayName("주문테이블 생성 테스트")
    @Test
    void create() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(2, false);
        given(orderTableRepository.save(orderTableRequest.toOrderTable())).willReturn(주문테이블);
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        assertAll(
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(orderTableResponse.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Lists.newArrayList(주문테이블));
        List<OrderTableResponse> orderTables = tableService.list();
        Assertions.assertThat(orderTables).containsExactlyElementsOf(Lists.newArrayList(OrderTableResponse.from(주문테이블)));
    }

    @DisplayName("주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(2, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블));
        OrderTableResponse changedOrderTable = tableService.changeEmpty(1L, orderTableRequest);
        assertAll(
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
        verify(orderTableValidator).validateComplete(주문테이블.id());
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(2, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .withMessage("주문테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문테이블의 단체지정이 있는 경우 주문테이블 비어있는지 여부 변경 테스트")
    @Test
    void changeEmptyWithTableGroup() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(2, false);
        OrderTable orderTable = createOrderTable(1L, 2, true);
        orderTable.reserve(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .withMessage("단체지정이 없어야 합니다.");
    }

    @DisplayName("주문테이블 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuests() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(4, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(1L, orderTableRequest);
        assertAll(
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블이 등록이 안되어있을 때 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithNotFoundOrderTable() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(4, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .withMessage("주문테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문테이블이 비어있는 경우 손님 수 변경 테스트")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        OrderTableRequest orderTableRequest = createOrderTableRequest(4, false);
        OrderTable orderTable = createOrderTable(1L, 2, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }

    @DisplayName("주문테이블 찾기 테스트")
    @Test
    void findOrderTable() {
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        OrderTable orderTable = tableService.findOrderTable(1L);
        assertThat(orderTable).isEqualTo(주문테이블);
    }

    @DisplayName("주문테이블 찾을 수 없음")
    @Test
    void findOrderTableEmpty() {
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.findOrderTable(1L))
                .withMessage("주문테이블을 찾을 수 없습니다.");
    }
}
