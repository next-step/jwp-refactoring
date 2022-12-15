package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductIdQuantityPair;
import kitchenpos.dto.ProductQuantityPair;
import kitchenpos.exception.NotExistIdException;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(NotExistIdException::new);
        List<ProductQuantityPair> productQuantityPairs = makePairs(request.getMenuProducts());
        Menu menu = new Menu(request.getPrice(), request.getName(), menuGroup, productQuantityPairs);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(toList());
    }

    private List<ProductQuantityPair> makePairs(List<ProductIdQuantityPair> pairs) {
        return pairs.stream()
            .map(pair -> new ProductQuantityPair(findProduct(pair.getProductId()), pair.getQuantity()))
            .collect(toList());
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(NotExistIdException::new);
    }
}
