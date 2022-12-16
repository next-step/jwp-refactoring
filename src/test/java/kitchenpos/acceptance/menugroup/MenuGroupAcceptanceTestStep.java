package kitchenpos.acceptance.menugroup;

import java.util.function.ToLongFunction;

import kitchenpos.AcceptanceTestStep;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;

public class MenuGroupAcceptanceTestStep extends AcceptanceTestStep<MenuGroupRequest, MenuGroupResponse> {

	static final String REQUEST_PATH = "/api/menu-groups";

	public MenuGroupAcceptanceTestStep() {
		super(MenuGroupResponse.class);
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<MenuGroupResponse> idExtractor() {
		return MenuGroupResponse::getId;
	}
}
