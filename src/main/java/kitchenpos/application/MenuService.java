package kitchenpos.application;


import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.validator.MenuValidator;
import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuGroupService menuGroupService,
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuGroupService = menuGroupService;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupService.findById(menuRequest.getMenuGroupId());
        menuValidator.validateCreate(menuRequest);

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
