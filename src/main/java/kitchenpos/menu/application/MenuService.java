package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            ProductRepository productRepository,
            MenuGroupRepository menuGroupRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = request.toEntity();
        menuValidator.validate(menu);
        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAllJoinFetch());
    }
}
