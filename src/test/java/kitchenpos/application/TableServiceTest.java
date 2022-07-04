package kitchenpos.application;

import kitchenpos.fixture.TestOrderTableRequestFactory;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.NumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepositoryMock;

    @Mock
    private TableValidator tableValidator;

    @Mock
    private OrderRepository orderRepository;


    @InjectMocks
    private TableService tableService;
    @InjectMocks
    private TableValidator tableValidatorInject;

    private OrderTableRequest 주문_테이블_요청_1;
    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private OrderTableEmptyRequest 비움_요청;

    @BeforeEach
    void setUp() {
        주문_테이블_요청_1 = TestOrderTableRequestFactory.create(1, false);
        주문_테이블_1 = new OrderTable(1L, new TableGroup(), 1, false);
        주문_테이블_2 = new OrderTable(2L, new TableGroup(), 2, false);
        비움_요청 = new OrderTableEmptyRequest(true);
    }

    @DisplayName("주문테이블을 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(orderTableRepositoryMock.save(any())).willReturn(주문_테이블_1);

        // when
        OrderTableResponse orderTable = tableService.create(주문_테이블_요청_1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("전체 테이블을 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(orderTableRepositoryMock.findAll()).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        List<OrderTableResponse> orderTable = tableService.list();

        // then
        assertThat(orderTable).hasSize(2);
    }

    @DisplayName("테이블을 비운다")
    @Test
    void changeEmpty() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(1L, null, 1, false);
        given(orderTableRepositoryMock.findById(any())).willReturn(Optional.of(주문_테이블_1));
        doNothing().when(tableValidator).validateEmpty(any());

        // when
        OrderTableResponse orderTable = tableService.changeEmpty(1L, 비움_요청);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 비울 수 없다")
    @Test
    void changeEmptyException1() throws Exception {
        // given
        given(orderTableRepositoryMock.findById(any())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 비움_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
    }

    @DisplayName("단체 테이블이면 비울 수 없다")
    @Test
    void changeEmptyException2() throws Exception {
        // when & then
        assertThatThrownBy(() -> tableValidatorInject.validateEmpty(주문_테이블_1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 주문상태가 COOKING, MEAL 이면 초기화할 수 없다")
    @Test
    void changeEmptyException3() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(3, false);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatThrownBy(() -> tableValidatorInject.validateEmpty(주문_테이블_1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 5명에서 10명으로 변경한다")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        given(orderTableRepositoryMock.findById(any())).willReturn(Optional.ofNullable(주문_테이블_1));
        doNothing().when(tableValidator).validateOrderTableEmpty(any());

        // when
        OrderTableResponse orderTable = tableService.changeNumberOfGuests(주문_테이블_1.getId(), new NumberOfGuestsRequest(10));

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("변경할 손님 수가 0명 미만이면 변경할 수 없다")
    @Test
    void changeNumberOfGuestsException1() throws Exception {
        // given
        given(orderTableRepositoryMock.findById(any())).willReturn(Optional.ofNullable(주문_테이블_1));
        doNothing().when(tableValidator).validateOrderTableEmpty(any());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_1.getId(), new NumberOfGuestsRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수 변경은 주문 테이블이 비어 있으면 수정할 수 없다")
    @Test
    void changeNumberOfGuestsException3() throws Exception {
        // given
        주문_테이블_1 = new OrderTable(1L, new TableGroup(), 1, true);
        given(orderTableRepositoryMock.findById(any())).willReturn(Optional.ofNullable(주문_테이블_1));
        doThrow(IllegalArgumentException.class).when(tableValidator).validateOrderTableEmpty(주문_테이블_1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_1.getId(), new NumberOfGuestsRequest(3)))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
