package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class TableServiceTest extends BaseServiceTest {

    @Autowired
    private TableService tableService;

    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private TableGroup 그룹_테이블_1;
    private OrderTable 그룹_지정된_테이블_1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        빈테이블_1 = new OrderTable(1L, null, 0, true);
        빈테이블_2 = new OrderTable(2L, null, 0, true);
        그룹_테이블_1 = new TableGroup(1L, new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2)));
        그룹_지정된_테이블_1 = new OrderTable(10L, 그룹_테이블_1.getId(), 0, false);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void createOrderTable() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        //when
        OrderTableResponse result = tableService.create(orderTableRequest);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isEqualTo(false);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllOrderTable() {
        // when
        List<OrderTableResponse> responses = tableService.findAll();

        // then
        assertThat(responses.size()).isEqualTo(12);
        assertThat(responses.stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList())).containsAll(Arrays.asList(1L, 2L, 3L, 4L, 5L, 12L));
    }

    @Test
    @DisplayName("빈 테이블로 설정 또는 해지할 수 있다.")
    void changeTableStatusEmpty() {
        // given
        Long orderTableId = 빈테이블_1.getId();
        boolean changeEmpty = !빈테이블_1.isEmpty();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(changeEmpty);

        // when
        OrderTableResponse result = tableService.changeEmpty(orderTableId, changeEmptyRequest);

        // then
        assertThat(result.getId()).isEqualTo(orderTableId);
        assertThat(result.isEmpty()).isEqualTo(changeEmpty);
    }

    @DisplayName("단체 지정이 되어 있다면 상태를 변경할 수 없다.")
    @Test
    void alreadyExistTableGroup() {
        // given
        Long orderTableId = 그룹_지정된_테이블_1.getId();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableId, changeEmptyRequest);
        }).withMessageMatching("그룹 지정이 되어 있어 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문이 조리 중이거나 식사 중일때는 상태를 변경할 수 없다.")
    @Test
    void notChangeStatusWhenCookingOrMeal() {
        //given
        Long orderTableId = 그룹_지정된_테이블_1.getId();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    void changeNumberOfGuests() {
        //given
        Long orderTableId = 그룹_지정된_테이블_1.getId();
        int changeNumberOfGuests = 5;
        OrderTableRequest changeNumberOfGuestsRequest = new OrderTableRequest(changeNumberOfGuests);

        //when
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest);

        //then
        assertThat(result.getId()).isEqualTo(orderTableId);
        assertThat(result.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
    }

    @DisplayName("방문한 손님 수는 0 명 미만으로 입력할 수 없다.")
    @Test
    void requireNumberOfGuests() {
        // given
        Long orderTableId = 그룹_지정된_테이블_1.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(-100);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        }).withMessageMatching("손님 수는 0 보다 작을 수 없습니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 등록할 등록할 수 없다.")
    @Test
    void emptyOrderTableStatus() {
        // given
        Long orderTableId = 빈테이블_1.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(2);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        }).withMessageMatching("빈 테이블의 손님 수는 변경할 수 없습니다.");
    }
}
