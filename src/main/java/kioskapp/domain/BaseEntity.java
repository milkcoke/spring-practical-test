package kioskapp.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
// Entity 변화 감지하여 엔티티와 매핑된 테이블 데이터 조작.
// AuditingEntityListener: JPA 이벤트 리스너로 엔티티 영속, 수정 이벤트 감지
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;
}

