package kitchenpos.menu.application;

import kitchenpos.menugroup.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
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

    public MenuResponse create(MenuRequest request) {
        Menu menu = toEntity(request);
        addProducts(menu, request.getMenuProducts());
        return fromEntity(menuRepository.save(menu));
    }

    private void addProducts(Menu menu, List<MenuProductRequest> menuProductRequests) {
        Map<Long,MenuProductRequest> menuProductRequestMap = menuProductRequests.stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, it -> it));
        List<Product> products = findProducts(menuProductRequestMap.keySet());

        for (Product product : products) {
            MenuProductRequest request = menuProductRequestMap.get(product.getId());
            menu.add(product, request.getQuantity());
        }
    }

    private List<Product> findProducts(Set<Long> menuIds) {
        List<Product> products = productRepository.findAllById(menuIds);

        if (products.size() != menuIds.size()) {
            throw new EntityNotFoundException("등록되지 않은 제품입니다.");
        }
        return products;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private Menu toEntity(MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(EntityNotFoundException::new);
        return new Menu(request.getName(), request.getPrice(), menuGroup);
    }

    private MenuResponse fromEntity(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId());
    }
}
