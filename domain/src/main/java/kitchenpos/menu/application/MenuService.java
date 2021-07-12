package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.CreateMenuDto;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuProductRepository menuProductRepository,
                       MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(CreateMenuDto menuDto) {

        List<MenuProduct> menuProducts =
            menuDto.getMenuProducts()
                   .stream()
                   .map(dto -> new MenuProduct(productRepository.findById(dto.getProductId())
                                                                .orElseThrow(NotFoundEntityException::new),
                                               dto.getQuantity()))
                   .collect(toList());

        Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(),
                             menuGroupRepository.findById(menuDto.getMenuGroupId())
                                                .orElseThrow(NotFoundEntityException::new));

        Menu persistMenu = menuRepository.save(menu);
        persistMenu.addMenuProducts(menuProducts);

        menuProducts.forEach(menuProductRepository::save);

        return MenuDto.of(persistMenu);
    }

    public List<MenuDto> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuDto::of)
                             .collect(toList());
    }
}
