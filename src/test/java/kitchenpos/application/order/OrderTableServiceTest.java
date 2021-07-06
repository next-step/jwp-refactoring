package kitchenpos.application.order;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService orderTableService;

	OrderTable orderTable1;
	OrderTable orderTable2;

	@BeforeEach
	void setUp() {
		orderTable1 = new OrderTable(1L, null, 4, false);
		orderTable2 = new OrderTable(2L, null, 0, true);
	}

	@DisplayName("주문 테이블 생성.")
	@Test
	void 주문_테이블_생성() {
		given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

		Assertions.assertThat(orderTableService.create(orderTable2))
			.isEqualTo(orderTable2)
			.extracting("tableGroupId")
			.isNull();
	}

	@DisplayName("모든 주문 테이블 목록 조회.")
	@Test
	void 모든_주문_테이블_목록_조회() {
		given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

		Assertions.assertThat(orderTableService.list()).containsExactly(orderTable1, orderTable2);
	}

	@DisplayName("주문 테이블을 비움.")
	@Test
	void 주문_테이블을_비움() {
		given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
		given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

		Assertions.assertThat(orderTableService.changeEmpty(orderTable1.getId(), orderTable2)).isEqualTo(orderTable1);
		Assertions.assertThat(orderTableService.changeEmpty(orderTable1.getId(), orderTable2).isEmpty()).isTrue();
	}

	@DisplayName("주문 테이블을 비움. > 저장된 주문 테이블이 비어있으면 안됨.")
	@Test
	void 주문_테이블을_비움_저장된_주문_테이블이_비어있으면_안됨() {
		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTableService.changeEmpty(orderTable1.getId(), orderTable2));
	}

	@DisplayName("주문 테이블을 비움. > 저장된 주문 테이블의 단체 지정이 차있으면 안됨.")
	@Test
	void 주문_테이블을_비움_저장된_주문_테이블의_단체_지정이_차있으면_안됨() {
		given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));

		orderTable1.setTableGroupId(1L);
		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTableService.changeEmpty(orderTable1.getId(), orderTable2));
	}

	@DisplayName("주문 테이블을 비움. > 주문 테이블이 요리 중이거나 식사 중이면 안됨.")
	@Test
	void 주문_테이블을_비움_주문_테이블이_요리_중이거나_식사_중이면_안됨() {
		given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTableService.changeEmpty(orderTable1.getId(), orderTable2));
	}

	@DisplayName("방문한 손님 수 변경.")
	@Test
	void 방문한_손님_수_변경() {
		given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
		given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

		orderTable2.setNumberOfGuests(1);
		Assertions.assertThat(orderTableService.changeNumberOfGuests(orderTable1.getId(), orderTable2))
			.isEqualTo(orderTable1);
		Assertions.assertThat(
			orderTableService.changeNumberOfGuests(orderTable1.getId(), orderTable2).getNumberOfGuests()).isEqualTo(1);
	}

	@DisplayName("방문한 손님 수 변경. > 주문 테이블의 방문한 손님 수가 0보다 작으면 안됨.")
	@Test
	void 방문한_손님_수_변경_주문_테이블의_방문한_손님_수가_0보다_작으면_안됨() {
		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable1.getId(), orderTable2));
	}

	@DisplayName("방문한 손님 수 변경. > 저장된 주문 테이블이 비어있으면 안됨.")
	@Test
	void 방문한_손님_수_변경_저장된_주문_테이블이_비어있으면_안됨() {
		orderTable2.setNumberOfGuests(1);

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable1.getId(), orderTable2));
	}

}
