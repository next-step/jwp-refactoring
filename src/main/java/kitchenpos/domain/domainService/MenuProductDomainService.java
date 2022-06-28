package kitchenpos.domain.domainService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductDomainService {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuProductDomainService(ProductRepository productRepository,
        MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateComponentForCreateMenu(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validatePriceSmallThenSum(menuRequest.getMenuProducts(), menuRequest.getPrice());
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private void validatePriceSmallThenSum(List<MenuProductDTO> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;

        List<Long> productIds = menuProducts.stream()
            .map(MenuProductDTO::getProductId)
            .collect(Collectors.toList());

        productRepository.findAllByIdIn(productIds);

        for (final MenuProductDTO menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(
                product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
