package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class MenuServiceTestSupport {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuServiceTestSupport(ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
                                  MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu 신메뉴_강정치킨_가져오기() {
        Product 강정치킨 = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 신메뉴 = menuGroupRepository.save(new MenuGroup("신메뉴"));
        Menu 강정치킨_메뉴 = new Menu("강정치킨", BigDecimal.valueOf(15_000), 신메뉴.getId());
        강정치킨_메뉴.addMenuProducts(Arrays.asList(new MenuProduct(강정치킨.getId(), 1L)));
        return menuRepository.save(강정치킨_메뉴);
    }
}
