package kitchenpos.auditor;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CommonAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // @LastModifiedBy, @CreatedBy가 추가될 때 사용
        return Optional.empty();
    }
}
