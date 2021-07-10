package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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

        menu.validationByPrice();
    }
}
