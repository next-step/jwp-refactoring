package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuRequest menuRequest) {
        verifyExistsMenuGroupById(menuRequest.getMenuGroupId());
        BigDecimal sum = sumMenuPrice(menuRequest.getMenuProducts());
        verifyAvailablePrice(menuRequest.getPrice(), sum);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuRequest.getMenuProducts()
                .forEach(menuProductRequest -> menuProducts.add(new MenuProduct(null,
                        menuProductRequest.getProductId(), menuProductRequest.getQuantity())));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
        Menu saveMenu = menuRepository.save(menu);
        return MenuResponse.of(saveMenu);
    }

    private void verifyAvailablePrice(BigDecimal price, BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("입력받은 메뉴가격이 상품의 총 가격보다 같거나 작아야합니다.");
        }
    }

    public BigDecimal sumMenuPrice(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    BigDecimal sum = BigDecimal.ZERO;
                    sum = sum.add(productRepository.findById(menuProductRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."))
                            .getPrice()
                            .multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
                    return sum;
                })
                .reduce((a, b) -> a.add(b))
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private void verifyExistsMenuGroupById(Long id) {
        if (!menuGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("메뉴그룹이 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
