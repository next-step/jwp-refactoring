package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.exception.NotExistMenuException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        Menu menu = menuDto.toMenu();

        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        List<Product> products = menuDto.getMenuProductDtos().stream()
                .map(menuProductDto ->  productService.findProductById(menuProductDto.getProductId()))
                .collect(toList());

        menu.checkSumPriceOfProducts(products);
        return MenuDto.of(menuRepository.save(menu));
    }

    public List<MenuDto> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuDto::of).collect(toList());
    }

    public void validateAllMenusExist(List<Long> orderItemMenuIds){
        if(orderItemMenuIds.size() == menuRepository.countByIdIn(orderItemMenuIds)){
            return;
        }
        throw new NotExistMenuException();
    }
}
