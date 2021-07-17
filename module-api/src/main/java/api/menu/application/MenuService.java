package api.menu.application;

import api.menu.application.exception.BadProductIdException;
import api.menu.dto.MenuProductRequest;
import api.menu.dto.MenuRequest;
import api.menu.dto.MenuResponse;
import domain.menu.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<Product> products = productService.findByIdIn(menuRequest.getProductsIds());
        List<MenuProduct> menuProducts = getMenuProductsBy(menuRequest.getMenuProducts(), products);
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProductsBy(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        return products.stream()
                .map(product -> createMenuProductWith(menuProductRequests, product))
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProductWith(List<MenuProductRequest> menuProductRequests, Product product) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> product.isProductId(menuProductRequest.getProductId()))
                .map(menuProductRequest -> MenuProduct.of(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadProductIdException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findMenuResponses() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    @Transactional(readOnly = true)
    public int countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
