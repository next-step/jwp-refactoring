package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private TableGroup 단체_테이블그룹;
    private OrderTable 주문_개인테이블;
    private OrderTable 주문1_단체테이블;
    private OrderTable 손님_10명_개인테이블;
    private OrderTable 빈_개인테이블;

    @BeforeEach
    void setUp() {
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);
        주문_개인테이블 = OrderTableFixtureFactory.create(1L, false);
        주문1_단체테이블 = OrderTableFixtureFactory.create(2L, true);
        빈_개인테이블 = OrderTableFixtureFactory.create(3L, true);
        손님_10명_개인테이블 = OrderTableFixtureFactory.createWithGuests(3L, 10, true);

        단체_테이블그룹.setOrderTables(Arrays.asList(주문1_단체테이블));
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());
    }

    @DisplayName("OrderTable 을 등록한다.")
    @Test
    void create1() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);

        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문_개인테이블);

        // when
        OrderTableResponse orderTableResponse = tableService.create(orderTable);

        // then
        assertThat(orderTableResponse).isEqualTo(OrderTableResponse.from(주문_개인테이블));
    }

    @DisplayName("OrderTable 목록을 조회한다.")
    @Test
    void findList() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문_개인테이블));

        // when
        List<OrderTableResponse> orderTableResponses = tableService.list();

        // then
        assertThat(orderTableResponses).containsExactly(OrderTableResponse.from(주문_개인테이블));
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경한다.")
    @Test
    void changeEmpty1() {
        // given
        주문_개인테이블.setEmpty(false);

        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));

        // when
        OrderTableResponse orderTableResponse = tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블);

        // then
        assertThat(orderTableResponse).isEqualTo(OrderTableResponse.from(주문_개인테이블));
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmpty2() {
        // given
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블));

    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void changeEmpty3() {
        // given
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문1_단체테이블.getId(), 빈_개인테이블));
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void changeEmpty4() {
        // given
        given(ordersRepository.existsByOrderTableAndOrderStatusIn(any(OrderTable.class), anyList())).willReturn(true);
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests1() {
        // given
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));

        // when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(주문_개인테이블.getId(), 손님_10명_개인테이블);

        // then
        assertThat(orderTableResponse).isEqualTo(OrderTableResponse.from(주문_개인테이블));
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(손님_10명_개인테이블.getNumberOfGuests());
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 손님의 수가 음수이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10})
    void changeNumberOfGuests2(int wrongNumberOfGuests) {
        // given
        OrderTable 손님_음수_개인테이블 = OrderTable.of(wrongNumberOfGuests, true);
        손님_음수_개인테이블.setNumberOfGuests(wrongNumberOfGuests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_개인테이블.getId(),
                                                                                                손님_음수_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests3() {
        // given
        given(orderTableRepository.findById(주문_개인테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_개인테이블.getId(),
                                                                                                손님_10명_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests4() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(빈_개인테이블.getId(),
                                                                                                손님_10명_개인테이블));
    }
}