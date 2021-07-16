package kitchenpos.application;

import kitchenpos.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public Menu create(final Menu menu) {
        validationByNewMenu(menu);

        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    protected void validationByNewMenu(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException(String.format("%s의 메뉴 그룹은 존재하지 않습니다.", menu.getMenuGroupId()));
        }

        if (!productRepository.existsByIdIn(menu.getProductIds())) {
            throw new IllegalArgumentException("상품에 등록된 메뉴만 등록할 수 있습니다.");
        }

        menu.validationByPrice(getProductPriceMap(menu));
    }

    @Transactional(readOnly = true)
    protected Map<Long, Price> getProductPriceMap(final Menu menu) {
        List<Product> allByIdIn = productRepository.findAllByIdIn(menu.getProductIds());

        return allByIdIn.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getPrice()));
    }
}
