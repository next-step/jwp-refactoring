package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuProducts.*;
import static kitchenpos.product.domain.Name.*;
import static kitchenpos.product.domain.Price.*;

import kitchenpos.menu.domain.MenuGroupId;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateRequest(menuRequest);
        final List<Product> products = findProducts(menuRequest);
        Menu persistMenu = menuRepository.save(
                new Menu.Builder()
                .name(valueOf(menuRequest.getName()))
                .price(wonOf(menuRequest.getPrice()))
                .menuGroupId(new MenuGroupId(menuRequest.getMenuGroupId()))
                .menuProducts(of(menuRequest.toMenuProducts(products)))
                .build());
        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }

    private List<Product> findProducts(final MenuRequest menuRequest) {
        final List<Long> productIds = menuRequest.getProductIds();
        final List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("상품으로 등록되지 않은 메뉴는 등록할 수 없습니다.");
        }
        return products;
    }

    private void validateRequest(MenuRequest menuRequest) {
        validateExistMenuGroup(menuRequest.getMenuGroupId());
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없습니다.");
        }
    }

}
