package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
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

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(()-> new EntityNotFoundException("메뉴그룹", menuRequest.getMenuGroupId()));
        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest){
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(()-> new EntityNotFoundException("상품", menuProductRequest.getProductId()));
        return MenuProduct.of(product, menuProductRequest.getQuantity());
    }
}
