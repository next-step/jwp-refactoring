package kitchenpos.product.product.application;

import java.util.List;
import kitchenpos.product.product.domain.ProductCommandService;
import kitchenpos.product.product.domain.ProductQueryService;
import kitchenpos.product.product.ui.request.ProductRequest;
import kitchenpos.product.product.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {


    private final ProductCommandService commandService;
    private final ProductQueryService queryService;

    public ProductService(ProductCommandService commandService,
        ProductQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        return ProductResponse.from(commandService.save(request.toEntity()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.listFrom(queryService.findAll());
    }
}
