package kitchenpos.table.application;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TableServiceTest {

    private OrderTable 주문테이블1;

    @MockBean
    private OrderTableRepository orderTableRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(3), true);
        tableService = new TableService(orderTableRepository, publisher);
    }

    @DisplayName("테이블생성테스트")
    @Test
    void createTableTest() {
        //given
        when(orderTableRepository.save(any(OrderTable.class)))
                .thenReturn(주문테이블1);

        //when
        OrderTableResponse result = tableService.create(OrderTableToOrderTableRequest(주문테이블1));

        //then
        assertAll(
                () -> assertThat(result.getId())
                        .isEqualTo(주문테이블1.getId()),
                () -> assertThat(result.getNumberOfGuests())
                        .isEqualTo(주문테이블1.getNumberOfGuests().getValue())
        );
    }

    private OrderTableRequest OrderTableToOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty());
    }

    @DisplayName("빈 상태로 변경테스트")
    @Test
    void changeEmptyTableTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(2), false);

        when(orderTableRepository.findById(orderTable.getId()))
                .thenReturn(Optional.ofNullable(orderTable));

        //when
        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), true);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 빈 테이블 변경 오류 테스트")
    @Test
    void notExistIdChangeEmptyTableExceptionTest() {
        //given
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("게스트숫자 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        int changeGuestNumber = 2;
        final OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(3), false);

        when(orderTableRepository.findById(주문테이블.getId()))
                .thenReturn(Optional.ofNullable(주문테이블));

        //when
        final OrderTableResponse result = tableService.changeNumberOfGuests(주문테이블.getId(), changeGuestNumber);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(changeGuestNumber);
    }

    @DisplayName("게스트숫자 0보다 작은 값으로 변경 오류 테스트")
    @Test
    void changeNumberOfGuestsUnderZeroExceptionTest() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 id로 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestNotExistTableIdExceptionTest() {
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블에서 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestEmptyTableIdExceptionTest() {
        //given
        final OrderTable 빈테이블 = new OrderTable(1L, null, new NumberOfGuests(3), true);
        when(orderTableRepository.findById(빈테이블.getId()))
                .thenReturn(Optional.ofNullable(빈테이블));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
