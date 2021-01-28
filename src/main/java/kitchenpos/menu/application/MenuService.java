package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductDao,
            final ProductRepository productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductDao;
        this.productRepository = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        Menu menu = this.menuRequestToMenu(menuRequest);

        this.validateMenu(menu);

        final Menu savedMenu = menuRepository.save(menu);
        this.addPersistMenuProducts(menu, savedMenu);

        return MenuResponse.of(menu);
    }

    private Menu menuRequestToMenu(MenuRequest menuRequest) {
        Menu menu = menuRequest.toMenu();
        menu.changeMenuGroup(this.menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 없으면 메뉴를 생성 할 수 없습니다.")));
        menu.changeMenuProducts(new MenuProducts(this.menuProductRepository.findAllById(menuRequest.getMenuProductIds())));
        return menu;
    }

    /**
     * 저장된 메뉴에 포함 된 메뉴상품들을 저장하고, 해당 메뉴에 추가합니다.
     * @param savedMenu
     */
    private void addPersistMenuProducts(Menu menu, Menu savedMenu) {
        final List<MenuProduct> menuProducts
                = menuProductRepository.findAllByMenuId(menu.getMenuGroup().getId());

        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(savedMenu);
            savedMenu.addMenuProducts(this.menuProductRepository.save(menuProduct));
        }
    }

    /**
     * 요청 받은 메뉴를 등록 할 수 있는지 검증합니다.
     * @param menu
     */
    private void validateMenu(Menu menu) {
        menu.validatePrice();
        this.existMenuGroup(menu);
        this.compareMenuProductsSum(menu);
    }

    /**
     * 메뉴 상품의 총 가격과 메뉴의 가격을 비교합니다.
     * @param menu
     * @throws IllegalArgumentException
     */
    private void compareMenuProductsSum(Menu menu) {
        if (menu.getPrice().compareTo(this.calculateMenuProductsSum(menu)) > 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 메뉴상품의 전체 가격을 구합니다
     * @param menu 
     * @return
     */
    private BigDecimal calculateMenuProductsSum(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    /**
     * 요청 받은 메뉴의 그룹이 실제 존재하는지 확인합니다.
     * @param menu
     * @throws IllegalArgumentException
     */
    private void existMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }
    }


    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(new MenuProducts(menuProductRepository.findAllByMenuId(menu.getId())));
        }

        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }
}
