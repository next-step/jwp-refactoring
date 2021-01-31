package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuMapper;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuMapper mapper = Mappers.getMapper(MenuMapper.class);

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        menuCreateRequest.checkPriceValidation();
        MenuGroup menuGroup = menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("invalid menugroup id"));
        final Menu menu = menuRepository.save(menuCreateRequest.toMenu(menuGroup));
        List<MenuProduct> products = menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> menuProductRepository.save(new MenuProduct(menu, productRepository.getOne(it.getProductId()), it.getQuantity())))
                .collect(Collectors.toList());
        menu.addMenuProducts(products);
        return mapper.toResponse(menu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
