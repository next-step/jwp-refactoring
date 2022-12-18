package kitchenpos.application;

import kitchenpos.common.ErrorCode;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        List<Product> products = findAllProductByIds(request.findAllProductIds());
        Menu menu = request.toMenu(menuGroup, products);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage()));
    }

    private List<Product> findAllProductByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (productIds.size() != products.size()) {
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage());
        }

        return products;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.list(menuRepository.findAll());
    }
}
