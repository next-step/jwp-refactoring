package kitchenpos.ordertable.application;

import kitchenpos.ServiceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 서비스 테스트")
class OrderTableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(2, true);

        // when
        OrderTableResponse savedOrderTableResponse = orderTableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(savedOrderTableResponse.getId()).isNotNull(),
                () -> assertThat(savedOrderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(savedOrderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @Test
    @DisplayName("테이블의 목록을 조회한다.")
    void list() {
        // given
        테이블_저장(true);

        // when
        List<OrderTableResponse> orderTableResponses = orderTableService.list();

        // then
        assertThat(orderTableResponses.size()).isOne();
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        // when
        OrderTableResponse modifiedOrderTableResponse = orderTableService.changeEmpty(
                savedOrderTableResponse.getId(), orderTableRequest);

        // then
        assertThat(modifiedOrderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 테이블 ID로 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException1() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderTableService.changeEmpty(0L, orderTableRequest));
    }

    @Test
    @DisplayName("테이블 그룹에 등록된 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException2() {
        // given
        TableGroupResponse savedTableGroupResponse = 테이블_그룹_저장();
        OrderTableResponse savedOrderTableResponse = 테이블_조회(savedTableGroupResponse.getOrderTableIds().get(0));
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableService.changeEmpty(savedOrderTableResponse.getId(), orderTableRequest));
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4);

        // when
        OrderTableResponse modifiedOrderTableResponse = orderTableService.changeNumberOfGuests(
                savedOrderTableResponse.getId(), orderTableRequest);

        // then
        assertThat(modifiedOrderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @Test
    @DisplayName("0명 이하의 손님 수로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException1() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        () -> orderTableService.changeNumberOfGuests(savedOrderTableResponse.getId(), orderTableRequest));
    }

    @Test
    @DisplayName("존재하지 않는 테이블 ID로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException2() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4);

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderTableService.changeNumberOfGuests(0L, orderTableRequest));
    }

    @Test
    @DisplayName("비어있는 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException3() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        () -> orderTableService.changeNumberOfGuests(savedOrderTableResponse.getId(), orderTableRequest));
    }
}
