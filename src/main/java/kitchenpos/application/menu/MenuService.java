package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    public static final String NOT_EXIST_MENU_GROUP_ERROR_MESSAGE = "메뉴 그룹 정보가 존재하지 않습니다.";

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        final BigDecimal price = createMenuRequest.getPrice();

        MenuGroup menuGroup = menuGroupRepository.findById(createMenuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MENU_GROUP_ERROR_MESSAGE));

        // TODO : productDao.findByAllIds 를 이용하여 In절로 조회 -> 조회 쿼리 수 감소
        // TODO : List<MenuProductRequest>와 List<Product>의 매핑 처리를 stream 내에서 구현 후 List<MenuProduct> 반환
        BigDecimal sum = BigDecimal.ZERO;

        final List<MenuProduct> menuProducts = new ArrayList<>();
        final List<MenuProductRequest> menuProductRequests = createMenuRequest.getMenuProductRequests();

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            // TODO : Menu 도메인 내부에서 처리하도록 로직 이동 -> e.g. menu.calculateSum()
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
            menuProducts.add(menuProductRequest.toMenuProduct(product));
        }

        // TODO : Menu 도메인 내부의 sum 계산 메소드에서 유효성 검증을 완료한 후 반환 하도록 수정
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(createMenuRequest.toMenu(menuGroup, menuProducts));
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        // TODO N+1 발생
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
