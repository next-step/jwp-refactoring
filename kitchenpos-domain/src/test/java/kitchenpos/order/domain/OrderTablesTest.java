package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTablesTest {

	private static final TableGroup 등록된_단체 = new TableGroup(1L, LocalDateTime.now());
	private static final OrderTable 단체가지정되지않은_빈테이블_1 = new OrderTable(1L, null, 0, true );
	private static final OrderTable 단체가지정되지않은_빈테이블_2 = new OrderTable(2L, null, 0, true );
	private static final OrderTable 단체가지정된_빈테이블_3 = new OrderTable(4L, 등록된_단체, 0, true );
	private static final OrderTable 단체가지정되지않은_비어있지않은테이블_4 = new OrderTable(5L, null, 0, false );


	@Test
	@DisplayName("단체의 테이블목록을 생성할 떄, 2개 이상의 단체가 지정되지 않은 빈 테이블로 생성을 시도하면 정상적으로 생성되어야한다.")
	void createOrderTables() {
		//given
		List<OrderTable> orderTables = Arrays.asList(단체가지정되지않은_빈테이블_1, 단체가지정되지않은_빈테이블_2);

		//when
		OrderTables result = new OrderTables(orderTables);

		//then
		assertThat(result.findAll()).containsExactlyInAnyOrder(단체가지정되지않은_빈테이블_1, 단체가지정되지않은_빈테이블_2);
	}

	@Test
	@DisplayName("단체의 테이블목록을 생성할 떄, 테이블이 없으면 IllegalArgumentException 을 throw 해야한다.")
	void createOrderTablesWithNoTable() {
		//given
		List<OrderTable> orderTables = Arrays.asList();

		//when-then
		assertThatThrownBy(() -> new OrderTables(orderTables))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체의 테이블목록을 생성할 떄, 테이블의 수가 1개면 IllegalArgumentException 을 throw 해야한다.")
	void createOrderTablesWithOneTable() {
		//given
		List<OrderTable> orderTables = Arrays.asList(단체가지정되지않은_빈테이블_1);

		//when-then
		assertThatThrownBy(() -> new OrderTables(orderTables))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체의 테이블목록을 생성할 떄, 비어있지 않은 테이블이 포함되면 IllegalArgumentException 을 throw 해야한다.")
	void createOrderTablesWithNotEmptyTable() {
		//given
		List<OrderTable> orderTables = Arrays.asList(단체가지정되지않은_빈테이블_1, 단체가지정되지않은_비어있지않은테이블_4);

		//when-then
		assertThatThrownBy(() -> new OrderTables(orderTables))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체의 테이블목록을 생성할 떄, 이미 단체로 지정된 테이블이 포함되면 IllegalArgumentException 을 throw 해야한다.")
	void createOrderTablesWithAlreadyGroupTable() {
		//given
		List<OrderTable> orderTables = Arrays.asList(단체가지정되지않은_빈테이블_1, 단체가지정된_빈테이블_3);

		//when-then
		assertThatThrownBy(() -> new OrderTables(orderTables))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("특정 사이즈만큼 단체의 테이블 목록이 생성되었는지 확인되어야 한다.")
	void sameSizeWith() {
		//given
		List<OrderTable> orderTables = Arrays.asList(단체가지정되지않은_빈테이블_1, 단체가지정되지않은_빈테이블_2);
		OrderTables orderTablesInGroup = new OrderTables(orderTables);

		//when
		boolean result = orderTablesInGroup.sameSizeWith(orderTables.size());

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("단체를 해제하면 단체 테이블 목록이 빈 상태로 초기화 되어야 한다.")
	void clear() {
		//given
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.addAll(Arrays.asList(단체가지정되지않은_빈테이블_1, 단체가지정되지않은_빈테이블_2));
		OrderTables orderTablesInGroup = new OrderTables(orderTables);

		//when
		orderTablesInGroup.clear();

		//then
		assertThat(orderTablesInGroup.findAll()).isEmpty();
	}
}
