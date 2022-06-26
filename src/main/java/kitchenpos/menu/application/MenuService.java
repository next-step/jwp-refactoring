package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotExistException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotExistException("메뉴 그룹이 존재하지 않습니다."));
        final Menu menu = new Menu.Builder(menuRequest.getName())
                .setPrice(Price.of(menuRequest.getPrice()))
                .setMenuGroup(menuGroup)
                .build();

        for (MenuProductRequest menuProduct : menuRequest.getMenuProducts()) {
            final Product persistProduct = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new NotExistException("제품이 존재하지 않습니다."));
            menu.addProduct(persistProduct, menuProduct.getQuantity());
        }

        menu.validateProductsTotalPrice();
        final Menu persist = menuRepository.save(menu);
        return persist.toMenuResponse();
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(Menu::toMenuResponse)
                .collect(Collectors.toList());
    }
}
