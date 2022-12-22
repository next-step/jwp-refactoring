package kitchenpos.acceptance.utils;

import static kitchenpos.acceptance.utils.RestAssuredUtils.*;
import static kitchenpos.table.ui.request.TableGroupRequest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.ui.request.TableGroupRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import kitchenpos.table.ui.response.TableGroupResponse;

public class TableGroupAcceptanceUtils {

	public static final String TABLE_GROUP_URL = "/api/table-groups";

	private TableGroupAcceptanceUtils() {
	}

	public static TableGroupResponse 단체_지정_되어잇음(List<Long> orderTableIds) {
		return 단체_지정_요청(orderTableIds).as(TableGroupResponse.class);
	}

	public static ExtractableResponse<Response> 단체_지정_요청(List<Long> orderTableIds) {
		return post(TABLE_GROUP_URL, tableGroupRequest(orderTableIds)).extract();
	}

	public static void 단체_지정_됨(ExtractableResponse<Response> response, List<Long> expectedOrderTableIds) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(TableGroupResponse.class))
				.satisfies(group -> {
					assertThat(group.getCreatedDate())
						.isEqualToIgnoringMinutes(LocalDateTime.now());
					assertThat(group.getOrderTables())
						.extracting(OrderTableResponse::getId, OrderTableResponse::getTableGroupId)
						.containsExactly(
							expectedOrderTableIds
								.stream()
								.map(id -> tuple(id, group.getId()))
								.toArray(Tuple[]::new)
						);
				})
		);
	}

	public static ExtractableResponse<Response> 단체_지정_해제_요청(long tableGroupId) {
		return delete(TABLE_GROUP_URL + "/" + tableGroupId).extract();
	}

	public static void 단체_지정_해제_됨(ExtractableResponse<Response> response) {
		List<OrderTableResponse> orderTableResponse = TableAcceptanceUtils.주문_테이블_목록_조회_요청()
			.as(new TypeRef<List<OrderTableResponse>>() {
			});
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(orderTableResponse)
				.extracting(OrderTableResponse::getTableGroupId)
				.containsExactly(null, null)
		);
	}

	private static TableGroupRequest tableGroupRequest(List<Long> orderTableIds) {
		return new TableGroupRequest(orderTableIds.stream()
			.map(OrderTableIdRequest::new)
			.collect(Collectors.toList()));
	}
}
