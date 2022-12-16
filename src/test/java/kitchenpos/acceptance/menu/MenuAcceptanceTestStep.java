package kitchenpos.acceptance.menu;

import java.util.function.ToLongFunction;

import kitchenpos.AcceptanceTestStep;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;

public class MenuAcceptanceTestStep extends AcceptanceTestStep<MenuRequest, MenuResponse> {

	static final String REQUEST_PATH = "/api/menus";

	public MenuAcceptanceTestStep() {
		super(MenuResponse.class);
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<MenuResponse> idExtractor() {
		return MenuResponse::getId;
	}
}
