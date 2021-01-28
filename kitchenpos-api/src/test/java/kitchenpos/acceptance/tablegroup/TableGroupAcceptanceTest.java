package kitchenpos.acceptance.tablegroup;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.ordertable.TableRestAcceptance;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("단체 지정 테스트")
public class TableGroupAcceptanceTest extends TableGroupAcceptance {

	private List<Long> orderTables;

	@BeforeEach
	void initData() {
		OrderTableResponse orderTable1 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);
		OrderTableResponse orderTable2 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);
		OrderTableResponse orderTable3 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);

		orderTables = Arrays.asList(orderTable1.getId(), orderTable2.getId(), orderTable3.getId());
	}

	@DisplayName("단체 지정을 생성한다.")
	@Test
	void createTableGroupTest() {
		// given
		TableGroupRequest request = TableGroupRequest.of(orderTables);

		// when
		ExtractableResponse<Response> response = 단체_지정_등록_요청(request);

		// then
		단체_지정_등록됨(response);
	}

	@DisplayName("단체 지정을 해제한다.")
	@Test
	void unTableGroupTest() {
		// given
		TableGroupRequest request = TableGroupRequest.of(orderTables);
		TableGroupResponse expected = 단체_지정_등록_요청(request).as(TableGroupResponse.class);

		// when
		ExtractableResponse<Response> response = 단체_지정_해제_요청(expected);

		// then
		단체_지정_해제됨(response);
	}
}
