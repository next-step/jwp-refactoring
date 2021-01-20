package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.model.Menu;
import kitchenpos.domain.model.Money;
import kitchenpos.domain.model.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuDto;
import kitchenpos.domain.repository.MenuDao;
import kitchenpos.domain.repository.MenuGroupDao;
import kitchenpos.domain.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public MenuDto create(final MenuCreateRequest menuDto) {
        validate(menuDto);
        final Menu savedMenu = menuDao.save(menuDto.toEntity());
        return MenuDto.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(MenuDto::of)
                .collect(Collectors.toList());
    }

    private void validate(MenuCreateRequest menuDto) {
        if (!menuGroupDao.existsById(menuDto.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴가 속한 메뉴 그룹이 존재해야 합니다");
        }

        final BigDecimal price = menuDto.getPrice();
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격이 null이여선 안됩니다.");
        }

        List<Long> productsIds = menuDto.getMenuProductIds();
        List<Product> products = productDao.findAllByIdIn(productsIds);

        if (productsIds.isEmpty() || productsIds.size() != products.size()) {
            throw new IllegalArgumentException("메뉴에 중복된 상품이 존재합니다.");
        }

        BigDecimal sum = calculateProductsPrice(products, menuDto.getMenuProducts());
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴에 속한 메뉴 상품의 총 합은 메뉴 가격보다 같거나 커야합니다.");
        }
    }

    private BigDecimal calculateProductsPrice(List<Product> products, List<MenuProductRequest> menuProducts) {
        Money sum = Money.ZERO;
        for (int i = 0; i < products.size(); i++) {
            MenuProductRequest menuProduct = menuProducts.get(i);
            Product product = products.get(i);
            sum = sum.plus(product.calculate(menuProduct.getQuantity()));
        }
        return sum.amount;
    }
}
