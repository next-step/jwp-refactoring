package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void 주문테이블_등록() {
        // given
        int numberOfGuests = 5;
        boolean empty = false;
        TableRequest request = new TableRequest(numberOfGuests, empty);

        // when
        TableResponse actual = tableService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(actual.isEmpty()).isEqualTo(empty);

    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문테이블_목록_조회() {
        // given
        int savedTableSize = 8;

        // when
        List<TableResponse> actual = tableService.list();

        // then
        assertThat(actual.size()).isEqualTo(savedTableSize);
    }

    @DisplayName("주문테이블을 빈테이블로 변경할 수 있다.")
    @Test
    void 빈_테이블로_변경() {
        // given
        Long orderTableId = 1L;
        TableRequest request = new TableRequest(5, false);

        // when
        TableResponse actual = tableService.changeEmpty(orderTableId, request);

        // then
        assertThat(actual.getId()).isEqualTo(orderTableId);
        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 빈_테이블_변경_예외_존재하지_않는_주문_테이블() {
        // given
        Long unregisteredOrderTableId = 10L;
        TableRequest request = new TableRequest(5, false);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(unregisteredOrderTableId, request)
        );
    }

    @DisplayName("주문 테이블 그룹이 단체 지정 되어있으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 빈_테이블_변경_예외_단체_지정됨() {
        // given
        Long orderTableId = 1L;
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
        tableGroupService.create(tableGroupRequest);

        TableRequest request = new TableRequest(3, true);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(orderTableId, request)
        );
    }

    @DisplayName("주문 테이블이 '조리' 나 '식사' 상태이면 빈 테이블로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 빈_테이블_변경_예외_조리_또는_식사_상태(String statusName) {
        // given
        Long orderTableId = 1L;
        TableRequest request = new TableRequest(3, true);

        주문_생성(orderTableId, statusName);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(orderTableId, request)
        );
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void 테이블_인원_변경() {
        // given
        Long orderTableId = 1L;
        TableRequest request = new TableRequest(3, false);
        tableService.changeEmpty(orderTableId, request);

        // when
        TableResponse actual = tableService.changeNumberOfGuests(orderTableId, request);

        // then
        assertThat(actual.getId()).isEqualTo(orderTableId);
        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("변경할 손님 수가 0보다 작으면 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 테이블_인원_변경_예외_인원_0보다_작음() {
        // given
        Long orderTableId = 1L;
        TableRequest request = new TableRequest(-2, false);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, request)
        );
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 테이블_인원_변경_예외_빈테이블() {
        // given
        Long orderTableId = 1L;
        TableRequest tableEmptyRequest = new TableRequest(0, true);
        tableService.changeEmpty(orderTableId, tableEmptyRequest);

        TableRequest request = new TableRequest(5, false);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, request)
        );
    }

    private OrderResponse 주문_생성(Long orderTableId, String statusName) {
        tableService.changeEmpty(orderTableId, new TableRequest(5, false));
        OrderStatus orderStatus = OrderStatus.valueOf(statusName);
        OrderRequest request = new OrderRequest(orderTableId, orderStatus, OrderServiceTest.ORDER_LINE_ITEMS);
        return orderService.create(request);
    }

}
