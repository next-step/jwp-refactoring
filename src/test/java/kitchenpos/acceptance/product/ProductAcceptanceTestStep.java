package kitchenpos.acceptance.product;

import java.util.function.ToLongFunction;

import kitchenpos.AcceptanceTestStep;
import kitchenpos.menu.ui.dto.ProductRequest;
import kitchenpos.menu.ui.dto.ProductResponse;

public class ProductAcceptanceTestStep extends AcceptanceTestStep<ProductRequest, ProductResponse> {

	private static final String REQUEST_PATH = "/api/products";

	public ProductAcceptanceTestStep() {
		super(ProductResponse.class);
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<ProductResponse> idExtractor() {
		return ProductResponse::getId;
	}
}
