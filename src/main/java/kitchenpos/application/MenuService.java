package kitchenpos.application;

import static kitchenpos.application.validator.MenuValidator.validatePrice;
import static kitchenpos.application.validator.MenuValidator.validatePriceGreaterThanSum;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupService menuGroupService,
            final MenuRepository menuRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupService = menuGroupService;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupService.findById(menuRequest.getMenuGroupId());
        validatePrice(menuRequest);
        validatePriceGreaterThanSum(getSumPriceFromMenuProducts(menuRequest.getMenuProducts()), menuRequest.getPrice());

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private BigDecimal getSumPriceFromMenuProducts(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new KitchenposException(ErrorCode.NOT_FOUND_PRODUCT));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }
}
