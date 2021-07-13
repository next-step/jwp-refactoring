package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.TableGroupException;

@DisplayName("단체 지정 도메인 테스트")
public class TableGroupTest {

	OrderTable orderFirstTable;
	OrderTable orderSecondTable;

	@BeforeEach
	void setUp() {
		orderFirstTable = new OrderTable(1L, new NumberOfGuests(2), true);
		orderSecondTable = new OrderTable(2L, new NumberOfGuests(2), true);
	}

	@DisplayName("단체지정 생성")
	@Test
	void 단체지정_생성() {
		TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderFirstTable, orderSecondTable));

		assertThat(tableGroup).isNotNull();
		assertThat(orderFirstTable.getTableGroup()).isEqualTo(tableGroup);
	}

	@DisplayName("단체지정 생성 시, 2개 미만의 주문 테이블로 등록 시 에러 발생")
	@Test
	void 단체지정_생성_시_2개미만_주문테이블_에러발생() {
		assertThatThrownBy(
			() -> new TableGroup(1L, Arrays.asList(orderFirstTable))
		).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("단체지정 생성 시 주문테이블이 존재하지 않으면 에러 발생")
	@Test
	void 단체지정_생성_시_주문테이블이_존재하지_않으면_에러_발생() {
		assertThatThrownBy(
			() -> new TableGroup(1L, Arrays.asList())
		).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("단체지정 생성 시 주문 테이블이 비워져 있지 않는 경우 에러 발생")
	@Test
	void 단체지정_생성_시_주문_테이블이_비워져있지_않는_경우_에러_발생() {
		orderFirstTable = new OrderTable(1L, new NumberOfGuests(2), false);
		orderSecondTable = new OrderTable(2L, new NumberOfGuests(2), false);

		assertThatThrownBy(
			() -> new TableGroup(1L, Arrays.asList(orderFirstTable, orderSecondTable))
		).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("단체지정 생성 시 이미 다른 단체지정에 속한 경우 에러 발생")
	@Test
	void 단체지정_생성_시_이미_다른_단체지정에_속한_경우_에러_발생() {
		TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderFirstTable, orderSecondTable));
		orderFirstTable = new OrderTable(1L, new NumberOfGuests(2), false);
		orderSecondTable = new OrderTable(2L, new NumberOfGuests(2), false);

		assertThatThrownBy(
			() -> new TableGroup(1L, Arrays.asList(orderFirstTable, orderSecondTable))
		).isInstanceOf(TableGroupException.class);

	}

	@DisplayName("단체지정 해체")
	@Test
	void 단체지정_해체() {
		TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderFirstTable, orderSecondTable));
		tableGroup.unGroup();

		assertThat(orderFirstTable.getTableGroup()).isNull();
		assertThat(orderSecondTable.getTableGroup()).isNull();
	}

}


