package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(4, false);
        OrderTable orderTable = orderTableCreateRequest.toEntity();
        given(orderTableDao.save(any())).willReturn(orderTable);

        // when
        OrderTableResponse result = tableService.create(orderTableCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTableGroup()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTableCreateRequest.getNumberOfGuests());
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // given
        given(orderTableDao.findAll()).willReturn(new ArrayList<>());

        // when
        List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(4, false);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when
        OrderTableResponse result = tableService.changeEmpty(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 빈 테이블로 변경할 수 없다.")
    void changeEmpty_not_exist_order_table() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체 지정이 되어 있으면 빈 테이블로 변경할 수 없다.")
    void changeEmpty_table_group() {
        // given
        OrderTable orderTable = new OrderTable(new TableGroup(LocalDateTime.now()));
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 단체로 지정되어 있어서 빈 테이블로 변경할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블이 조리 또는 식사 중이면 빈 테이블로 변경로 변경할 수 없다.")
    void changeEmpty_cooking_or_meal(OrderStatus orderStatus) {
        // given
        Order order = new Order(orderStatus);
        OrderTable orderTable = new OrderTable(order);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조리 또는 식사 중이면 빈 테이블로 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable saveOrderTable = new OrderTable(0, false);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));
        int numberOfGuests = 4;

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(1L, numberOfGuests);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_not_exist_order_table() {
        // given
        OrderTable saveOrderTable = new OrderTable(0, false);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블입니다.");
    }

    @Test
    @DisplayName("변경할 방문 손님 수가 0보다 작으면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_guest_count_zero() {
        // given
        OrderTable saveOrderTable = new OrderTable(0, false);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경할 방문 손님수가 0보다 작습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_empty_table() {
        // given
        OrderTable saveOrderTable = new OrderTable(0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 주문 테이블은 빈 테이블입니다.");
    }
}
