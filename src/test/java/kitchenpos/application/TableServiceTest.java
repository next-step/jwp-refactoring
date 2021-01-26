package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TableServiceTest {
	private final OrderTable defaultOrderTable = createOrderTable(0, true, null);
	@Autowired
	private TableService tableService;
	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private OrderService orderService;

	@DisplayName("테이블 등록")
	@Test
	void create() {
		OrderTable orderTable = createOrderTable(0, false);

		OrderTable resultOrderTable = tableService.create(orderTable);

		assertThat(resultOrderTable.getId()).isNotNull();
		assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(0);
		assertThat(resultOrderTable.isEmpty()).isFalse();
		assertThat(resultOrderTable.getTableGroupId()).isNull();
	}

	@DisplayName("테이블 목록 조회")
	@Test
	void list() {
		List<OrderTable> orderTables = tableService.list();

		assertThat(orderTables).hasSize(8);
	}

	@DisplayName("등록되지 않은 테이블의 상태 변경시 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(9L, defaultOrderTable));
	}

	@DisplayName("단체 지정 테이블 변경시 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException2() {
		TableGroup tableGroup = createTableGroup(1L, 2L);

		tableGroupService.create(tableGroup);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(1L, defaultOrderTable));
	}

	@DisplayName("주문 상태가 요리 중 또는 식사 중이면 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException3() {
	}

	@DisplayName("테이블을 상태를 변경 한다.")
	@Test
	void changeEmpty() {
		OrderTable resultTable = tableService.changeEmpty(1L, createOrderTable(false));

		assertThat(resultTable.getId()).isNotNull();
		assertThat(resultTable.isEmpty()).isFalse();
	}

	@DisplayName("방문한 손님 수가 0 보다 작을 경우 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(1L, createOrderTable(-1)));
	}

	@DisplayName("등록되지 않은 테이블의 방문한 손님 변경시 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException2() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(9L, defaultOrderTable));
	}

	@DisplayName("빈 테이블 변경시 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException3() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(1L, defaultOrderTable));
	}

	@DisplayName("테이블의 방문한 손님 수 변경")
	@Test
	void changeNumberOfGuests() {
		tableService.changeEmpty(1L, createOrderTable(false));

		OrderTable resultTable = tableService.changeNumberOfGuests(1L, createOrderTable(4));

		assertThat(resultTable.getId()).isNotNull();
		assertThat(resultTable.getNumberOfGuests()).isEqualTo(4);
	}
}
