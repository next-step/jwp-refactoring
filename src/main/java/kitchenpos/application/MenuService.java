package kitchenpos.application;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupService menuGroupService,
                       ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());

        Menu menu = Menu.builder()
                .name(menuRequest.getName())
                .price(new Money(menuRequest.getPrice()))
                .menuGroupId(menuGroup.getId())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        addMenuProducts(menu, menuRequest);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));
    }

    private void addMenuProducts(Menu menu, MenuRequest menuRequest) {
        Money sum = Money.ZERO;
        List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productService.findProductById(menuProductRequest.getProductId());
            sum.add(product.calculatePrice(menuProductRequest.getQuantity()));
            menu.addMenuProduct(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }

        checkMenuPrice(menu, sum);
    }

    private void checkMenuPrice(Menu menu, Money sum) {
        if (menu.priceIsGreaterThan(sum)) {
            throw new InvalidMenuPriceException("상품 가격 합계보다 비싼 메뉴를 등록할 수 없습니다.");
        }
    }
}
