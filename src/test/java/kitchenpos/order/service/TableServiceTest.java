package kitchenpos.order.service;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
  @InjectMocks
  private TableService tableService;

  @Mock
  private OrderDao orderDao;

  @Mock
  private OrderTableDao orderTableDao;

  private OrderTable 요청_주문_테이블;

  @BeforeEach
  void setUp() {
    요청_주문_테이블 = OrderFactory.ofOrderTable(1L, null, false, 4);
  }

  @DisplayName("주문 테이블을 생성한다.")
  @Test
  void 주문_테이블_생성() {
    // given
    given(orderTableDao.save(요청_주문_테이블)).willReturn(요청_주문_테이블);

    // when
    OrderTable response = tableService.create(요청_주문_테이블);

    // then
    assertThat(response.getTableGroupId()).isEqualTo(null);
  }

  @DisplayName("주문 테이블 목록을 조회한다.")
  @Test
  void 주문_테이블_목록_조회() {
    // given
    given(orderTableDao.findAll()).willReturn(Collections.singletonList(요청_주문_테이블));

    // when
    List<OrderTable> response = tableService.list();

    // then
    assertThat(response.size()).isEqualTo(1);
  }

  @DisplayName("주문 테이블을 빈 테이블 여부를 변경한다.")
  @Test
  void 주문_테이블_빈_테이블_여부_변경() {
    // given
    OrderTable 저장된_주문_테이블_일번 = OrderFactory.ofOrderTable(1L, null, false, 10);
    given(orderTableDao.findById(요청_주문_테이블.getId())).willReturn(Optional.of(저장된_주문_테이블_일번));
    given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            요청_주문_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
    given(orderTableDao.save(저장된_주문_테이블_일번)).willReturn(저장된_주문_테이블_일번);

    // when
    OrderTable response = tableService.changeEmpty(요청_주문_테이블.getId(), 저장된_주문_테이블_일번);

    // then
    assertThat(response.isEmpty()).isFalse();
  }

  @DisplayName("주문 테이블이 특정 테이블 그룹에 속해 있으면 변경할 수 없다.")
  @Test
  void 빈_테이블_여부_변경_테이블_그룹_존재_예외() {
    // given
    OrderTable 저장된_주문_테이블_일번 = OrderFactory.ofOrderTable(1L, 2L, false, 10);
    given(orderTableDao.findById(요청_주문_테이블.getId())).willReturn(Optional.of(저장된_주문_테이블_일번));

    // when
    Throwable thrown = catchThrowable(() -> tableService.changeEmpty(요청_주문_테이블.getId(), 요청_주문_테이블));

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문의 상태가 `조리` 또는 `식사`인 주문이 한 건 이상 있을 경우 변경할 수 없다.")
  @Test
  void 주문_상태_조리_또는_식사_중_변경_불가_예외() {
    // given
    OrderTable 저장된_주문_테이블_일번 = OrderFactory.ofOrderTable(1L, null, false, 10);
    given(orderTableDao.findById(요청_주문_테이블.getId())).willReturn(Optional.of(저장된_주문_테이블_일번));
    given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            요청_주문_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

    // when
    Throwable thrown = catchThrowable(() -> tableService.changeEmpty(요청_주문_테이블.getId(), 요청_주문_테이블));

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("주문 테이블의 손님 수를 변경한다.")
  @Test
  void 주문_테이블_손님_수_변경() {
    // given
    OrderTable 저장된_주문_테이블_일번 = OrderFactory.ofOrderTable(1L, null, false, 4);
    given(orderTableDao.findById(요청_주문_테이블.getId())).willReturn(Optional.of(저장된_주문_테이블_일번));
    given(orderTableDao.save(저장된_주문_테이블_일번)).willReturn(저장된_주문_테이블_일번);

    // when
    OrderTable response = tableService.changeNumberOfGuests(요청_주문_테이블.getId(), 요청_주문_테이블);

    // then
    assertThat(response.getNumberOfGuests()).isEqualTo(요청_주문_테이블.getNumberOfGuests());
  }

  @DisplayName("손님 수를 변경 빈테이블일 경우 변경할 수 없다.")
  @Test
  void 주문_테이블_손님_수_변경_빈_테이블일_경우_예외() {
    // given
    OrderTable 저장된_주문_테이블_일번 = OrderFactory.ofOrderTable(1L, null, true, 4);
    given(orderTableDao.findById(요청_주문_테이블.getId())).willReturn(Optional.of(저장된_주문_테이블_일번));

    // when
    Throwable thrown = catchThrowable(() -> tableService.changeNumberOfGuests(요청_주문_테이블.getId(), 요청_주문_테이블));

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }
}
