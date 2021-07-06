package kitchenpos.application.command;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menu.MenuCreateValidator;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Products;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuCreateValidator menuCreateValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuCreateValidator menuCreateValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuCreateValidator = menuCreateValidator;
    }
    public Long create(final MenuCreate create) throws Exception {
        MenuGroup menuGroup = menuGroupRepository.findById(create.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Products products = new Products(productRepository.findAllById(create.getProductsIdInMenuProducts()));

        return menuRepository.save(Menu.create(create, menuGroup, products, menuCreateValidator))
                .getId();
    }

}
