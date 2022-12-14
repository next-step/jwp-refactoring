package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 등록한다.")
    void createTable() {
        // given
        OrderTableRequest 주문테이블_요청 = orderTableRequest(2, false);
        given(orderTableRepository.save(any())).willReturn(주문테이블);

        // when
        OrderTableResponse actual = tableService.create(주문테이블_요청);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(2L),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회하면 주문 테이블 목록을 반환한다.")
    void findOrderTables() {
        // given
        OrderTable 주문테이블2 = orderTable(2L, null, 4, false);
        List<OrderTable> orderTables = Arrays.asList(주문테이블, 주문테이블2);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(OrderTableResponse::getId)).containsExactly(주문테이블.id(), 주문테이블2.id())
        );
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 테이블로 등록되어 있어야 한다.")
    void updateEmptyTableByCreatedOrderTable() {
        // given
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.id()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다. ID : 2");
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 테이블은 단체 테이블이 아니어야 한다.")
    void updateEmptyTableByNoneTableGroup() {
        // given
        OrderTable 단체테이블 = orderTable(2L, 1L, 8, false);
        given(orderTableRepository.findById(단체테이블.id())).willReturn(Optional.of(단체테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(단체테이블.id()))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 상태가 조리 중 또는 식사 중이면 안된다.")
    void updateEmptyTableByOrderStatusIsCookingOrMeal() {
        // given
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.id(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.id()));
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 수정한다.")
    void updateEmptyTable() {
        // given
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.id(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        OrderTableResponse actual = tableService.changeEmpty(주문테이블.id());

        // then
        assertTrue(actual.isEmpty());
    }
    
    @ParameterizedTest(name = "[{index}] 주문 테이블 손님 수 수정시 손님은 0명 이상이어야 한다.")
    @ValueSource(ints = {-2, -1, Integer.MIN_VALUE})
    void updateTableNumberOfGuestByGuestMoreThanZero(int numberOfGuests) {
        // given
        OrderTableRequest changeTable = orderTableRequest(numberOfGuests, false);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(주문테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), changeTable))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("최소 인원 수는 0명 이상입니다.");
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정시 주문 테이블로 등록되어 있어야 한다.")
    void updateTableNumberOfGuestByCreatedOrderTable() {
        // given
        OrderTableRequest changeTable = orderTableRequest(3, false);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), changeTable))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다. ID : 2");
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 수정할 수 없다.")
    void updateTableNumberOfGuestByEmptyTable() {
        // given
        OrderTableRequest changeTable = orderTableRequest(3, false);
        OrderTable 주문테이블 = orderTable(1L, null, 2, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), changeTable))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다. ID : 1");
    }
    
    @Test
    @DisplayName("주문 테이블의 손님 수를 수정한다.")
    void updateTableNumberOfGuest() {
        // given
        OrderTableRequest changeTable = orderTableRequest(3, false);
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(주문테이블));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(주문테이블.id(), changeTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }
}
