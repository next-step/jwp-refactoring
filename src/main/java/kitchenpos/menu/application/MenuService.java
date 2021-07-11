package kitchenpos.menu.application;

import static kitchenpos.common.domain.Name.*;
import static kitchenpos.common.domain.Price.*;
import static kitchenpos.menu.domain.MenuProducts.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupId;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

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
        Menu persistMenu = menuRepository.save(
                new Menu.Builder()
                .name(valueOf(menuRequest.getName()))
                .price(wonOf(menuRequest.getPrice()))
                .menuGroupId(new MenuGroupId(menuRequest.getMenuGroupId()))
                .menuProducts(of(menuRequest.toMenuProducts()))
                .products(findProducts(menuRequest))
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
