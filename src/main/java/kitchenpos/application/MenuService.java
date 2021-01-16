package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
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
        final BigDecimal price = menuDto.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductRequest> menuProducts = menuDto.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(BigDecimal.valueOf(product.getPrice().amount * menuProduct.getQuantity()));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

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
}
