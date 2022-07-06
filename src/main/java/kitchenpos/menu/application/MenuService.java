package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(findProductById(menuProduct.getProductId()), menuProduct.getQuantity()))
                .collect(toList());

        final MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(toList());
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴 그룹이 존재하지 않습니다."));
    }
}
