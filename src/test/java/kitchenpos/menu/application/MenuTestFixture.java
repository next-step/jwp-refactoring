package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.Arrays;
import javax.transaction.Transactional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    public Menu 후라이드_양념_세트_가져오기() {
        Product 후라이드 = this.productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        Product 양념치킨 = this.productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000)));
        MenuProduct 후라이드_메뉴상품 = new MenuProduct(후라이드, 1);
        MenuProduct 양념치킨_메뉴상품 = new MenuProduct(양념치킨, 1);

        MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        Menu 후라이드_양념_세트 =
            new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        return menuRepository.save(후라이드_양념_세트);
    }

}
