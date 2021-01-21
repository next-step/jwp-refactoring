package kitchenpos.acceptance.table;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 지정 테스트")
public class TableGroupAcceptanceTest extends TableGroupAcceptance {

	private OrderTable orderTable1;
	private OrderTable orderTable2;
	private OrderTable orderTable3;
	private List<OrderTable> orderTables;

	@BeforeEach
	void initData() {
		orderTable1 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTable.class);
		orderTable2 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTable.class);
		orderTable3 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTable.class);

		orderTables = Arrays.asList(orderTable1, orderTable2, orderTable3);
	}

	@DisplayName("단체 지정을 생선한다.")
	@Test
	void createTableGroupTest() {
		// given
		TableGroup tableGroup = TableGroup.of(null, null, orderTables);

		// when
		ExtractableResponse<Response> response = 단체_지정_등록_요청(tableGroup);

		// then
		단체_지정_등록됨(response);
	}

	@DisplayName("단체 지정을 해제한다.")
	@Test
	void unTableGroupTest() {
		// given
		TableGroup expected = 단체_지정_등록_요청(TableGroup.of(null, null, orderTables)).as(TableGroup.class);

		// when
		ExtractableResponse<Response> response = 단체_지정_해제_요청(expected);

		// then
		단체_지정_해제됨(response);
	}
}
