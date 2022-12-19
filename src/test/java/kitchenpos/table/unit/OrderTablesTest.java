package kitchenpos.table.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

@DisplayName("주문테이블 목록 관련 단위테스트")
public class OrderTablesTest {
	private OrderTable 빈_테이블_A;
	private OrderTable 빈_테이블_B;
	private OrderTable 채워진_테이블;

	@BeforeEach
	void setUp() {
		채워진_테이블 = OrderTable.of(2, false);
		빈_테이블_A = OrderTable.of(2, true);
		빈_테이블_B = OrderTable.of(2, true);
	}

	@DisplayName("주문테이블 목록 전체가 빈 테이블인지 확인할 수 있다.")
	@Test
	void isAllEmpty() {
		// given
		OrderTables 주문테이블목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 채워진_테이블));
		// when - then
		assertFalse(주문테이블목록.isAllEmpty());
	}

	@DisplayName("주문테이블중 단체지정된 테이블이 있는지 확인할 수 있다.")
	@Test
	void isAnyGrouped() {
		// given
		OrderTables 주문테이블목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 빈_테이블_B));
		TableGroup.of(주문테이블목록);
		// when - then
		assertTrue(주문테이블목록.isAnyGrouped());
	}

}
