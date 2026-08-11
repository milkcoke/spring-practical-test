package kioskapp;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
// Transactional 에 의해 자동 Rollback
// Repository 관련 Components 만 load
@DataJpaTest
public abstract class RepositoryTestSupport {
}
