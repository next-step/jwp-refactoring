package kitchenpos;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValueObjectId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    // for JPA
    protected ValueObjectId() {
    }

    public Long getSeq() {
        return seq;
    }
}
