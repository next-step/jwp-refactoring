package kitchenpos.ordertable.application;

import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 기능 테스트")
@SpringBootTest
@Transactional
class TableServiceTest {
    @Autowired
    private TableService tableService;

    private OrderTableResponse savedOrderTable1;

    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        orderTableRequest = new OrderTableRequest(10, true);
        savedOrderTable1 = tableService.create(new OrderTableRequest(10, false));
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTableResponse response = tableService.create(orderTableRequest);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(response.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
        assertThat(response.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 리스트를 반환한다.")
    @Test
    void list() {
        // given
        List<OrderTableResponse> orderTables = Arrays.asList(tableService.create(orderTableRequest),
                tableService.create(orderTableRequest),
                tableService.create(orderTableRequest));

        // when then
        final List<Long> tableIds = orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList());
        assertThat(tableService.findAll())
                .map(OrderTableResponse::getId)
                .containsAll(tableIds);
    }

    @DisplayName("테이블의 비어있는 상태를 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTableResponse response1 =  tableService.changeEmpty(savedOrderTable1.getId(), savedOrderTable1.isEmpty());

        assertThat(response1.isEmpty()).isFalse();
    }

    @DisplayName("존재하지 않는 테이블의 비어있는 상태 변경시 예외 발생.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty_NotExistOrderTable(boolean isEmpty) {
        OrderTableResponse response1 =  tableService.changeEmpty(savedOrderTable1.getId(), savedOrderTable1.isEmpty());

        assertThatThrownBy(() -> tableService.changeEmpty(-32L, response1.isEmpty()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("주문 테이블이 없습니다.");
    }

    @DisplayName("테이블의 인원수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 99})
    void changeNumberOfGuests(int numberOfGuests) {
        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(savedOrderTable1.getId(), numberOfGuests);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("존재하지 않는 테이블의 인원 변경시 예외 발생시킨다.")
    @Test
    void changeNumberOfGuests_NotExistTable() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("주문 테이블이 없습니다.");
    }
}