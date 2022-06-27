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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private MenuGroupRepository menuGroupRepository;
    private MenuRepository menuRepository;
    private ProductRepository productRepository;

    @Transactional
    public Menu create(final MenuRequest request) {
        // 메뉴 그룹 id 체크
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                 .orElseThrow(NoSuchElementException::new);

        List<Long> productIds = request.getMenuProducts()
                                       .stream()
                                       .map(MenuProductRequest::getProductId)
                                       .collect(Collectors.toList());

        List<MenuProduct> menuProducts = new ArrayList<>();
        List<Product> products = productRepository.findByIdIn(productIds);

        // 상품 합계 금액 체크
        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            products.stream()
                    .filter(product -> product.getId().equals(menuProduct.getProductId()))
                    .findFirst()
                    .ifPresent(product -> menuProducts.add(new MenuProduct(product, menuProduct.getQuantity())));
        }

        // 저장
        return menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
    }

    public List<Menu> list() {
//        for (final Menu menu : menus) {
//            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
//        }
        return menuRepository.findAll();
    }
}
