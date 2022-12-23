package kitchenpos.acceptance.menu;

import java.util.List;
import java.util.function.ToLongFunction;
import kitchenpos.acceptance.AcceptanceTestStep;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptanceTestStep;
import kitchenpos.acceptance.menugroup.MenuGroupFixture;
import kitchenpos.acceptance.product.ProductAcceptanceTestStep;
import kitchenpos.acceptance.product.ProductFixture;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.ProductResponse;

public class MenuAcceptanceTestStep extends AcceptanceTestStep<MenuRequest, MenuResponse> {

	private static final String REQUEST_PATH = "/api/menus";

	private final ProductAcceptanceTestStep products = new ProductAcceptanceTestStep();
	private final MenuGroupAcceptanceTestStep menuGroups = new MenuGroupAcceptanceTestStep();

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

	public MenuResponse 등록되어_있음() {
		List<ProductResponse> 상품목록 = products.등록되어_있음(ProductFixture.상품목록(3));
		MenuGroupResponse 메뉴그룹 = menuGroups.등록되어_있음(MenuGroupFixture.메뉴그룹());

		return 등록되어_있음(MenuFixture.메뉴(상품목록, 메뉴그룹));
	}
}
