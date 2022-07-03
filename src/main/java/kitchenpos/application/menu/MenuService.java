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

        // TODO : Menu Entity 객체 생성 시점에 원시 타입 포장 객체를 이용한 유효성 검증
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        // TODO : 오류 메세지 추가
        MenuGroup menuGroup = menuGroupRepository.findById(createMenuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException());

        // TODO : productDao.findByAllIds 를 이용하여 In절로 조회 -> 조회 쿼리 수 감소
        // TODO : findBByAllIds와 menuProducts size를 비교하는 구문으로 유효성 검증 변경
        BigDecimal sum = BigDecimal.ZERO;

        final List<MenuProductRequest> menuProductRequests = createMenuRequest.getMenuProductRequests();
        final List<MenuProduct> menuProducts = new ArrayList<>();
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

        // TODO : Menu Entity와 MenuProduct Entity의 생명 주기가 같도록 영속성 전이 옵션 설정
        // TODO : e.g. @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
        final Menu savedMenu = menuRepository.save(createMenuRequest.toMenu(menuGroup, menuProducts));
        menuProductRepository.saveAll(menuProducts);

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
