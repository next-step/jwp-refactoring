package kitchenpos.tablegroup.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.ui.OrderTableAcceptanceTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableGroupAcceptanceTest extends AcceptanceTest {

	@DisplayName("단체 테이블을 관리한다")
	@Test
	void manage() {
		//단체테이블을 등록한다
		//given
		ExtractableResponse<Response> orderTable1 = OrderTableAcceptanceTest
			  .주문_테이블_등록을_요청한다(new OrderTableRequest());
		ExtractableResponse<Response> orderTable2 = OrderTableAcceptanceTest
			  .주문_테이블_등록을_요청한다(new OrderTableRequest());
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(orderTable1.body().as(OrderTableResponse.class).getId());
		orderTableIds.add(orderTable2.body().as(OrderTableResponse.class).getId());

		//when
		ExtractableResponse<Response> createdResponse = 단체_테이블_등록을_요청한다(
			  orderTableIds);

		//then
		단체_테이블_정보가_등록됨(orderTableIds, createdResponse);

		//단체 테이블을 해제한다
		long tableGroupId = createdResponse.jsonPath().getLong("id");
		ExtractableResponse<Response> ungroupResponse = 단체_테이블_해제를_요청한다(
			  orderTableIds, tableGroupId);

		//then
		단체_테이블이_해제됨(ungroupResponse);
	}

	@DisplayName("조리, 식사중인 테이블은 단체 지정을 해제할 수 없다.")
	@Test
	void unGroupWithCooking() {
		//given
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(9L);
		orderTableIds.add(10L);

		//when
		ExtractableResponse<Response> response = 단체_테이블_해제를_요청한다(orderTableIds, 1L);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private ExtractableResponse<Response> 단체_테이블_해제를_요청한다(Set<Long> orderTableIds,
		  long tableGroupId) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new TableGroupRequest(orderTableIds))
			  .when().delete("/api/table-groups/" + tableGroupId)
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 단체_테이블_등록을_요청한다(Set<Long> orderTableIds) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new TableGroupRequest(orderTableIds))
			  .when().post("/api/table-groups")
			  .then().log().all()
			  .extract();
	}

	private void 단체_테이블_정보가_등록됨(Set<Long> orderTableIds,
		  ExtractableResponse<Response> createdResponse) {
		assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		List<OrderTableResponse> orderTables = createdResponse.jsonPath()
			  .getList("orderTables", OrderTableResponse.class);
		assertThat(orderTables).hasSize(orderTableIds.size());
		orderTables.forEach(orderTable ->
			assertThat(orderTable.getTableGroupId())
				  .isEqualTo(createdResponse.jsonPath().getLong("id"))
		);
	}

	private void 단체_테이블이_해제됨(ExtractableResponse<Response> ungroupResponse) {
		assertThat(ungroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
