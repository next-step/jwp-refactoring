package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    @Autowired
    public MenuService(MenuGroupRepository menuGroupRepository, MenuRepository menuRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                 .orElseThrow(NoSuchElementException::new);

        List<Long> productIds = request.getMenuProducts()
                                       .stream()
                                       .map(MenuProductRequest::getProductId)
                                       .collect(Collectors.toList());

        List<MenuProduct> menuProducts = new ArrayList<>();
        List<Product> products = productRepository.findByIdIn(productIds);

        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            Product product = products.stream()
                                      .filter(prod -> prod.getId().equals(menuProduct.getProductId()))
                                      .findFirst()
                                      .orElseThrow(() -> new IllegalArgumentException("등록하고자 하는 상품이 존재하지 않습니다."));
            menuProducts.add(new MenuProduct(product, menuProduct.getQuantity()));
        }

        return menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuResponse::from)
                             .collect(Collectors.toList());
    }
}
