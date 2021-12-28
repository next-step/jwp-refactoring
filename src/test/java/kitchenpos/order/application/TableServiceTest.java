package kitchenpos.order.application;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableValidator tableValidator;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블;
    private OrderTableRequest 주문테이블_Request;
    private OrderTable 테이블1번;
    private OrderTable 테이블2번;
    private List<OrderTable> orderTables;
    private TableGroup 단체_지정;

    @BeforeEach
    void setUp() {
        주문테이블 = OrderTableFixture.생성(0, false);
        주문테이블_Request = OrderTableFixture.샘플_Request();
        테이블1번 = OrderTableFixture.생성(0, true);
        테이블2번 = OrderTableFixture.생성(0, true);
        orderTables = Arrays.asList(테이블1번, 테이블2번);
        단체_지정 = TableGroup.empty();
    }

    @DisplayName("주문 테이블은 방문한 손님 수,빈 테이블 상태로 등록 할 수 있다.")
    @Test
    void create() {
        given(orderTableRepository.save(any())).willReturn(주문테이블);

        OrderTableResponse createOrderTable = tableService.create(주문테이블_Request);

        assertThat(createOrderTable).isNotNull();
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문테이블));

        List<OrderTableResponse> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(1),
                () -> assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(0)
        );
    }

    @DisplayName("주문 테이블 상태를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable 빈주문테이블 = OrderTableFixture.생성(0, true);

        given(orderTableRepository.findById(any())).willReturn(java.util.Optional.of(빈주문테이블));
        given(orderTableRepository.save(any())).willReturn(빈주문테이블);

        OrderTableResponse changeOrderTable = tableService.changeEmpty(any(), OrderTableFixture.생성_Request(0, false));

        assertThat(changeOrderTable.isEmpty()).isFalse();

    }

    @DisplayName("주문 테이블 상태를 요리중이거나 식사중일땐 바꿀 수 없다.")
    @Test
    void changeError() {
        OrderTable 빈주문테이블 = OrderTableFixture.생성(0, true);
        Order order = OrderFixture.생성(빈주문테이블);
        given(orderTableRepository.findById(any())).willReturn(java.util.Optional.of(주문테이블));
        doThrow(IllegalArgumentException.class).when(tableValidator).validateCompletion((OrderTable) any());

        assertThatThrownBy(
                () -> tableService.changeEmpty(any(), OrderTableFixture.생성_Request(0, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        given(orderTableRepository.findById(any())).willReturn(java.util.Optional.ofNullable(주문테이블));
        given(orderTableRepository.save(any())).willReturn(주문테이블);

        OrderTableResponse changeNumberOfGuests = tableService.changeNumberOfGuests(any(), OrderTableFixture.생성_Request(10, true));

        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("0명부터 가능하다.")
    @Test
    void changeNumberOfGuestsError() {
        OrderTable 주문테이블_손님_수_변경 = OrderTableFixture.생성(-10, true);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(any(), OrderTableFixture.생성_Request(-10, true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정")
    @Test
    void group() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
        given(orderTableRepository.saveAll(any())).willReturn(orderTables);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        tableService.grouped(1L, orderTableIds);

        assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(1L);
        assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("단체 지정 해체 할 수 있다.")
    @Test
    void ungroup() {
        List<OrderTable> 주문테이블_목록 = Arrays.asList(OrderTableFixture.단체지정_주문테이블(), OrderTableFixture.단체지정_주문테이블());
        given(orderTableRepository.findByTableGroupId(any())).willReturn(주문테이블_목록);

        tableService.ungrouped(단체_지정.getId());

        assertAll(
                () -> assertThat(주문테이블_목록.get(0).getTableGroupId()).isEqualTo(null),
                () -> assertThat(주문테이블_목록.get(0).getTableGroupId()).isEqualTo(null)
        );
    }
}
