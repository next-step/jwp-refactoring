package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.util.dto.SaveMenuDto;
import org.springframework.stereotype.Component;

@Component
public class MenuTestFixture {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuTestFixture(ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu 메뉴_만들기(SaveMenuDto saveMenuDto) {
        List<MenuProduct> menuProducts = saveMenuDto.getMenuProducts();
        List<Product> products = menuProducts.stream()
            .map(MenuProduct::getProduct)
            .collect(Collectors.toList());
        this.productRepository.saveAll(products);

        MenuGroup menuGroup = this.menuGroupRepository.save(saveMenuDto.getMenuGroup());

        Menu menu = new Menu(
            saveMenuDto.getMenuName(), BigDecimal.valueOf(saveMenuDto.getPrice()), menuGroup.getId(), menuProducts);

        return this.menuRepository.save(menu);
    }

}
