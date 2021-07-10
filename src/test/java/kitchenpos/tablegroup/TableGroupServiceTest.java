package kitchenpos.tablegroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.TableServiceTest;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;
	@InjectMocks
	private TableGroupService tableGroupService;

	TableGroup 단체지정;
	OrderTable 주문테이블1번;
	OrderTable 주문테이블2번;

	@BeforeEach
	void setUp() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1));
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1));
		단체지정 = 단체지정생성(1L, Arrays.asList(주문테이블1번, 주문테이블2번));
	}

	@DisplayName("단체지정 생성")
	@Test
	void 단체지정_생성() {
		given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()))).willReturn(
			Arrays.asList(주문테이블1번, 주문테이블2번));
		given(tableGroupDao.save(단체지정)).willReturn(단체지정);
		given(orderTableDao.save(주문테이블1번)).willReturn(주문테이블1번);
		given(orderTableDao.save(주문테이블2번)).willReturn(주문테이블2번);

		TableGroup created = tableGroupService.create(단체지정);

		단체지정_생성_확인(created, 단체지정);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 빈테이블 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_빈테이블일_경우_단체_지정에_등록될_수_없다() {
		Order order = new Order(); //todo
		주문테이블1번.changeEmpty(false, order);

		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 2개 미만인 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_2개_미만인_경우_단체_지정에_등록될_수_없다() {
		단체지정.setOrderTables(Arrays.asList(주문테이블1번));

		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 존재하지않으면 단체 지정을 등록할 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_존재하지않으면_단체_지정을_등록할_수_없다() {
		given(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()))).willReturn(
			Arrays.asList(주문테이블1번));

		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 해체")
	@Test
	void 단체지정_해체() {
		OrderTable 해체된주문테이블1번 = 주문테이블1번;
		//해체된주문테이블1번.setTableGroupId(null);
		OrderTable 해체된주문테이블2번 = 주문테이블2번;
		//해체된주문테이블2번.setTableGroupId(null);
		given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(Arrays.asList(주문테이블1번, 주문테이블2번));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.willReturn(false);
		given(orderTableDao.save(주문테이블1번)).willReturn(해체된주문테이블1번);
		given(orderTableDao.save(주문테이블2번)).willReturn(해체된주문테이블2번);

		tableGroupService.ungroup(단체지정.getId());

		단체지정_해체_확인(주문테이블1번);
		단체지정_해체_확인(주문테이블2번);
	}

	@DisplayName("단체지정 해체 - 주문 테이블들이 조리중이거나 식사중일 경우 단체지정을 해체할 수 없다.")
	@Test
	void 단체지정_해체_주문_테이블들이_조리중이거나_식사중일_경우_단체지정을_해체할_수_없다() {
		given(orderTableDao.findAllByTableGroupId(단체지정.getId())).willReturn(Arrays.asList(주문테이블1번, 주문테이블2번));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.willReturn(true);

		assertThatThrownBy(() -> {
			tableGroupService.ungroup(단체지정.getId());
		}).isInstanceOf(IllegalArgumentException.class);
	}

	private void 단체지정_해체_확인(OrderTable ungroupOrderTable) {
		//assertThat(ungroupOrderTable.getTableGroupId()).isEqualTo(null);
	}

	private void 단체지정_생성_확인(TableGroup created, TableGroup expected) {
		assertThat(created.getCreatedDate()).isEqualTo(expected.getCreatedDate());
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getOrderTables()).containsAll(expected.getOrderTables());
		OrderTable createdOrderTable = created.getOrderTables().stream().findAny().get();
		assertThat(createdOrderTable.isEmpty()).isEqualTo(false);
		//assertThat(createdOrderTable.getTableGroupId()).isEqualTo(created.getId());
	}

	public static TableGroup 단체지정생성(Long id, List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		tableGroup.setId(id);
		return tableGroup;
	}
}
