package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.CreateMenuDto;
import kitchenpos.menu.dto.CreateMenuProductDto;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
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

        Menu menu = new Menu(menuDto.getName(),
                             menuDto.getPrice(),
                             menuGroupRepository.findById(menuDto.getMenuGroupId())
                                                .orElseThrow(IllegalArgumentException::new));

        final Menu savedMenu = menuRepository.save(menu);

        List<CreateMenuProductDto> menuProductDtos = menuDto.getMenuProducts();

        Price sum = new Price(0);

        for (CreateMenuProductDto menuProductDto : menuProductDtos) {
            Product product = productRepository.findById(menuProductDto.getProductId())
                                               .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getPrice().getValue() * menuProductDto.getQuantity());

            MenuProduct menuProduct = new MenuProduct(product, menuProductDto.getQuantity());
            savedMenu.addMenuProduct(menuProduct);
            menuProductRepository.save(menuProduct);
        }

        if (menu.getPrice().getValue() > sum.getValue()) {
            throw new IllegalArgumentException();
        }

        return MenuDto.of(savedMenu);
    }

    public List<MenuDto> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuDto::of)
                             .collect(toList());
    }
}
