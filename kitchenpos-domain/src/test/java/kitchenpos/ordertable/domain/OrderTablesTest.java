package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OrderTablesTest {

	@DisplayName("주문 테이블이 2개 미만인 경우 단체 지정을 할 수 없음")
	@Test
	void createWithOneOrderTable() {
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new OrderTables(Arrays.asList(new OrderTable()), 1))
			  .withMessage("단체 지정은 주문 테이블이 최소 2개 이상이어야 합니다.");
	}

	@DisplayName("미등록 주문 테이블은 단체 지정을 할 수 없음")
	@Test
	void createWithNotExistOrderTable() {
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new OrderTables(Arrays.asList(new OrderTable(), new OrderTable()), 3))
			  .withMessage("주문 테이블 정보를 찾을 수 없습니다.");
	}

	@DisplayName("다른 단체에 포함된 테이블은 단체 지정을 할 수 없음")
	@Test
	void createWithOtherTableGroup() {
		TableGroup tableGroup = TableGroup.newInstance();
		OrderTable otherGroupTable = new OrderTable();
		ReflectionTestUtils.setField(otherGroupTable, "tableGroup", tableGroup);

		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new OrderTables(Arrays.asList(otherGroupTable, new OrderTable()), 2))
			  .withMessage("단체 지정이 불가능한 테이블입니다.");
	}
}
