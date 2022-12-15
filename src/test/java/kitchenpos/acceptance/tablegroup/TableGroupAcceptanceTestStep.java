package kitchenpos.acceptance.tablegroup;

import java.util.function.ToLongFunction;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTestStep;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;

public class TableGroupAcceptanceTestStep extends AcceptanceTestStep<TableGroupRequest, TableGroupResponse> {

	static final String GROUP_REQUEST_PATH = "/api/table-groups";
	static final String UNGROUP_REQUEST_PATH = "/api/table-groups/{tableGroupId}";

	public TableGroupAcceptanceTestStep() {
		super(TableGroupRequest.class, TableGroupResponse.class);
	}

	@Override
	protected String getRequestPath() {
		return GROUP_REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<TableGroupResponse> idExtractor() {
		return TableGroupResponse::getId;
	}

	public ExtractableResponse<Response> 테이블_그룹_해제요청(TableGroupResponse tableGroupResponse) {
		return 삭제_요청(UNGROUP_REQUEST_PATH, tableGroupResponse.getId());
	}
}
