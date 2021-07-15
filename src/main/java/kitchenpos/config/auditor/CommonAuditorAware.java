package kitchenpos.config.auditor;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class CommonAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // @LastModifiedBy, @CreatedBy가 추가될 때 사용
        return Optional.empty();
    }
}
