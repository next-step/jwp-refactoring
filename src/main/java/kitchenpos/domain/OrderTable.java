package kitchenpos.domain;

import kitchenpos.exception.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests = 0;

    public void update(Integer numberOfGuests) {
        if (numberOfGuests == null) {
            throw new BadRequestException("손님 수는 null로 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return numberOfGuests.equals(0);
    }

    public void group(Long tableGroupId) {
        if (tableGroupId == null) {
            throw new BadRequestException("Group Id는 null로 세팅할 수 없습니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean isGroupped() {
        return Objects.nonNull(tableGroupId);
    }
}
