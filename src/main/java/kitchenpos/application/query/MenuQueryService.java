package kitchenpos.application.query;

import kitchenpos.dto.response.MenuViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuQueryService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuQueryService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public List<MenuViewResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuViewResponse::of)
                .collect(Collectors.toList());
    }

    public MenuViewResponse findById(Long id) {
        return MenuViewResponse.of(menuRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new));
    }
}
