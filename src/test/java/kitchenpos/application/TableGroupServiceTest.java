package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

	@InjectMocks
	private TableGroupService tableGroupService;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	@Mock
	private OrderDao orderDao;

	@DisplayName("단체 지정을 생성한다")
	@Test
	void createTest() {
		// given
		List<OrderTable> orderTableList = new ArrayList<>();
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTableList.add(orderTable1);
		orderTableList.add(orderTable2);

		TableGroup request = new TableGroup();
		request.setOrderTables(orderTableList);

		TableGroup persist = new TableGroup();
		persist.setOrderTables(orderTableList);

		given(orderTableDao.findAllByIdIn(any())).willReturn(orderTableList);
		given(tableGroupDao.save(any())).willReturn(persist);

		// when
		request = tableGroupService.create(request);

		// then
		assertThat(request.getOrderTables().size()).isEqualTo(persist.getOrderTables().size());
	}

	@DisplayName("생성 시, 주문 테이블 수가 2개 이상이어야 한다")
	@Test
	void createTest2() {
		// given
		List<OrderTable> orderTableList = new ArrayList<>();
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTableList.add(orderTable1);

		TableGroup request = new TableGroup();
		request.setOrderTables(orderTableList);

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 시, 주문 테이블들이 모두 존재해야 한다")
	@Test
	void createTest3() {
		// given
		List<OrderTable> orderTableList = new ArrayList<>();
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTableList.add(orderTable1);
		orderTableList.add(orderTable2);

		TableGroup request = new TableGroup();
		request.setOrderTables(orderTableList);

		given(orderTableDao.findAllByIdIn(any())).willReturn(new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 시, 테이블 인원이 0인 주문테이블은 하나라도 있으면 안된다")
	@Test
	void createTest4() {
		// given
		List<OrderTable> orderTableList = new ArrayList<>();
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(false);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTableList.add(orderTable1);
		orderTableList.add(orderTable2);
		TableGroup request = new TableGroup();
		request.setOrderTables(orderTableList);

		given(orderTableDao.findAllByIdIn(any())).willReturn(orderTableList);

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 해제한다")
	@Test
	void ungroupTest() {
		// given
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(false);

		// when
		tableGroupService.ungroup(1L);
	}

	@DisplayName("단체 지정을 해제 시, 조리 중이거나 식사 중인 테이블은 안된다")
	@Test
	void ungroupTest2() {
		// given
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(true);

		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class);

	}

}
