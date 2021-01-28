package kitchenpos.menu;

import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.product.Product;
import kitchenpos.product.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuGroupService menuGroupService,
            final ProductService productService,
            final MenuRepository menuRepository
    ) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toMenu();

        menuGroupService.checkExistsMenuGroup(menu.getMenuGroupId());

        List<Product> persistProducts = productService.findProductsByIds(menu.getProductIds());
        menu.comparePriceAndSumOfMenuProducts(persistProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public void checkExistsMenus(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotFoundEntityException("등록되지 않은 메뉴가 있습니다.");
        }
    }
}
