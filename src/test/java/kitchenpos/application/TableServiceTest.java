package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TableServiceTest {

  private static final int NUMBER_OF_GUESTS = 10;
  private static final int NEW_NUMBER_OF_GUESTS = 11;
  private static final boolean IS_EMPTY = false;

  @Autowired
  private TableService tableService;

  @Autowired
  private OrderTableDao orderTableDao;

  @Autowired
  private OrderDao orderDao;

  @DisplayName("테이블을 등록한다.")
  @Test
  void create() {
    // given
    OrderTable orderTable = new OrderTable();

    // when
    OrderTable savedOrderTable = tableService.create(orderTable);

    // then
    assertThat(savedOrderTable.getId()).isNotNull();
  }

  @DisplayName("테이블 목록을 조회한다.")
  @Test
  void list() {
    // given
    OrderTable orderTable = new OrderTable();

    // when
    OrderTable savedOrderTable = tableService.create(orderTable);

    // then
    List<OrderTable> list = tableService.list();
    assertThat(list).extracting("id").contains(savedOrderTable.getId());
  }

  @DisplayName("테이블을 빈테이블로 변경한다.")
  @Test
  void changeEmpty() {
    // given
    OrderTable orderTable = orderTableDao.findById(1L).get();
    orderTable.setTableGroupId(1L);

    Order order = new Order(orderTable.getId());
    order.setOrderStatus(OrderStatus.COMPLETION.name());
    orderDao.save(order);

    // when
    OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

    // then
    assertThat(changedOrderTable.isEmpty()).isTrue();
  }

  @DisplayName("테이블의 빈테이블 변경 예외 - `조리중`과 `식사중`에는 변경할 수 없다.")
  @Test
  void changeEmpty_exception1() {
    // given
    OrderTable orderTable = orderTableDao.findById(1L).get();
    orderTable.setTableGroupId(1L);

    Order order = new Order(orderTable.getId());
    order.setOrderStatus(OrderStatus.MEAL.name());
    orderDao.save(order);

    // when, then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
        .withMessage("`조리중`과 `식사중`에는 변경할 수 없다.");
  }

  @DisplayName("테이블의 게스트 숫자를 변경한다.")
  @Test
  void changeNumberOfGuests() {
    // given
    OrderTable orderTable = orderTableDao.findById(1L).get();
    orderTable.setNumberOfGuests(NEW_NUMBER_OF_GUESTS);

    // when
    OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

    // then
    assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(NEW_NUMBER_OF_GUESTS);
  }

  @DisplayName("테이블의 게스트 숫자 변경 예외 - 게스트 인원이 0보다 커야한다.")
  @Test
  void changeNumberOfGuests_exception1() {
    // given
    OrderTable orderTable = orderTableDao.findById(1L).get();
    orderTable.setNumberOfGuests(-1);

    // when, then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        .withMessage("게스트 인원이 0보다 커야한다.");
  }

  @DisplayName("테이블의 게스트 숫자 변경 예외 - 변경하고자 하는 테이블이 등록되어 있어야 한다.")
  @Test
  void changeNumberOfGuests_exception2() {
    // given
    OrderTable orderTable = orderTableDao.findById(1L).get();
    orderTable.setNumberOfGuests(NEW_NUMBER_OF_GUESTS);

    // when, then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> tableService.changeNumberOfGuests(999999L, orderTable))
        .withMessage("변경하고자 하는 테이블이 등록되어 있어야 한다.");
  }
}
