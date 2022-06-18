package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.EmptyRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService service;
    @Autowired
    private OrderTableServiceTestSupport orderTableServiceTestSupport;

    private OrderTable 테이블;

    @BeforeEach
    public void setUp(@Autowired OrderTableRepository orderTableRepository) {
        테이블 = orderTableRepository.save(new OrderTable(0, false));

    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTableRequest request = new OrderTableRequest(0, true);

        OrderTableResponse response = service.create(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTableGroupId()).isNull();
    }

    @DisplayName("테이블의 비움 여부를 변경한다.")
    @Test
    void changeEmpty() {
        EmptyRequest request = new EmptyRequest(false);

        OrderTableResponse response = service.changeEmpty(테이블.getId(), request);

        assertThat(response.isEmpty()).isFalse();
    }

    @DisplayName("계산이 완료되지 않은 테이블의 비움 여부를 변경한다.")
    @Test
    void changeEmptyWithNotCompletedOrder() {
        orderTableServiceTestSupport.강정치킨_주문하기(테이블);

        assertThatThrownBy(() -> {
            service.changeEmpty(테이블.getId(), new EmptyRequest(true));
        }).isInstanceOf(CannotChangeEmptyException.class)
        .hasMessageContaining("비움 여부를 변경할 수 없습니다.");
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        NumberOfGuestsRequest request = new NumberOfGuestsRequest(5);

        OrderTableResponse response = service.changeNumberOfGuests(테이블.getId(), request);

        assertThat(response.getNumberOfGuests()).isEqualTo(5);
    }


    @DisplayName("존재하지 않는 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        NumberOfGuestsRequest request = new NumberOfGuestsRequest(5);

        assertThatThrownBy(() -> {
            service.changeNumberOfGuests(Long.MAX_VALUE, request);
        }).isInstanceOf(NotFoundOrderTableException.class);
    }
}
