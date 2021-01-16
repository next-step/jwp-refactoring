package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.repository.MenuDao;
import kitchenpos.repository.MenuGroupDao;
import kitchenpos.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
        final BigDecimal price = menuDto.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = calculateProductsPrice(menuDto.getMenuProducts());
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateProductsPrice(List<MenuProductRequest> menuProducts) {
        List<Long> productsIds = menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productDao.findAllByIdIn(productsIds);

        if (productsIds.isEmpty() || productsIds.size() != products.size()) {
            throw new IllegalArgumentException();
        }

        Money sum = Money.ZERO;
        for (int i = 0; i < products.size(); i++) {
            long quantity = menuProducts.get(i).getQuantity();
            Money price = products.get(i).getPrice();
            sum = sum.plus(price.times(quantity));
        }
        return BigDecimal.valueOf(sum.amount);
    }
}
