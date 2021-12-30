package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
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
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = menuRequest.toMenu();
        validateMenu(menu);

        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

    private void validateMenu(Menu menu) {
        checkMenuGroup(menu.getMenuGroupId());
        checkMenuPrice(menu);
    }

    private void checkMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new BadRequestException("해당 메뉴 그룹을 찾을 수 없습니다."));
    }

    private void checkMenuPrice(Menu menu) {
        BigDecimal totalPrice = calculateTotalPrice(menu.getMenuProducts());
        Price menuPrice = menu.getPrice();

        if (menuPrice.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateTotalPrice(List<MenuProduct> menuProduct) {
        return menuProduct.stream()
                .map(this::calculateMenuProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMenuProductPrice(MenuProduct menuProduct) {
        Product product = findProductById(menuProduct.getProductId());
        BigDecimal productPrice = product.getPrice();
        return menuProduct.multiplyByQuantity(productPrice);
    }
}
