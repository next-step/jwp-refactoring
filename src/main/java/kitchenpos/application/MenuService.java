package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.validator.MenuValidator;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuGroupRepository menuGroupRepository, MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                 .orElseThrow(NoSuchElementException::new);

        List<MenuProduct> menuProducts = menuValidator.validateMenuProducts(request);

        return menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuResponse::from)
                             .collect(Collectors.toList());
    }
}
