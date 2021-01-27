package kitchenpos.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService,
            final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest, menuGroup);
        return menuRepository.save(menuRequest.toMenu(menuGroup, menuProducts));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    public Menu findById(long id) {
        return menuRepository.findById(id).orElseThrow(() -> new MenuException("메뉴가 존재하지 않습니다", id));
    }

    public List<Menu> findAllById(List<Long> ids) {
        return menuRepository.findAllById(ids);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest, MenuGroup menuGroup) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        List<Product> products = productService.findAllById(menuRequest.getProductIds());
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (MenuProductRequest request : menuRequest.getMenuProducts()) {
            MenuProduct menuProduct = new MenuProduct(menuRequest.toMenu(menuGroup), productMap.get(request.getProductId()), request.getQuantity());
            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }
}
