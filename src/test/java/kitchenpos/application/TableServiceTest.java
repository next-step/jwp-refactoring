package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련")
@SpringBootTest
class TableServiceTest {
    @Autowired
    TableService tableService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderTableDao orderTableDao;

    Long orderTableId;
    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        setOrderTable();
    }

    void setOrderTable() {
        orderTableId = 1L;
        orderTable = new OrderTable(orderTableId, null, 0, false);
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));
    }

    @DisplayName("개별 주문 테이블을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        // when
        tableService.create(orderTable);

        // then
        verify(orderTableDao).save(orderTable);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        tableService.list();

        // then
        verify(orderTableDao).findAll();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다")
    @Test
    void changeEmpty() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        // when
        OrderTable empty = new OrderTable(null, null, 0, true);
        tableService.changeEmpty(orderTableId, empty);

        // then
        verify(orderTableDao).save(orderTable);
    }

    @DisplayName("없는 주문 테이블은 변경할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        Long notExistsOrderTableId = 1000L;

        // when then
        assertSoftly(softAssertions -> {
            OrderTable empty = new OrderTable(null, null, 0, true);
            softAssertions.assertThatThrownBy(() -> tableService.changeEmpty(notExistsOrderTableId, empty))
                    .isInstanceOf(IllegalArgumentException.class);

            OrderTable guest2 = new OrderTable(null, null, 2, false);
            softAssertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistsOrderTableId, guest2))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @DisplayName("단체 지정된 주문 테이블은 변경할 수 없다")
    @Test
    void groupedTable_is_cannot_change() {
        // given
        Long groupedOrderTableId = 1000L;
        OrderTable groupedOrderTable = new OrderTable(groupedOrderTableId, 1L, 10, false);
        when(orderTableDao.findById(groupedOrderTableId)).thenReturn(Optional.of(groupedOrderTable));

        // when then
        OrderTable empty = new OrderTable(null, null, 0, true);
        assertThatThrownBy(() -> tableService.changeEmpty(groupedOrderTableId, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문의 상태가 조리, 식사인 경우 변경할 수 없다")
    @Test
    void orderStatus_is_not_in_cooking_meal() {
        // given
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when then
        OrderTable empty = new OrderTable(null, null, 0, true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        // when
        OrderTable guest2 = new OrderTable(null, null, 2, false);
        tableService.changeNumberOfGuests(orderTableId, guest2);

        // then
        verify(orderTableDao).save(orderTable);
    }

    @DisplayName("방문한 손님 수는 0명 이상이어야 한다")
    @Test
    void number_is_0_over() {
        // when then
        OrderTable minus = new OrderTable(null, null, -1, false);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, minus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 변경할 수 없다")
    @Test
    void empty_table_cannot_change() {
        // given
        Long emptyOrderTableId = 1000L;
        OrderTable emptyOrderTable = new OrderTable(emptyOrderTableId, null, 10, true);
        when(orderTableDao.findById(emptyOrderTableId)).thenReturn(Optional.of(emptyOrderTable));

        // when
        OrderTable guest2 = new OrderTable(null, null, 2, false);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyOrderTableId, guest2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
