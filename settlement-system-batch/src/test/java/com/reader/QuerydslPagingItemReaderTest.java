package com.reader;

import com.config.QueryDslConfig;
import com.domain.user.entity.QUser;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import com.job.DataCollectionJobConfig;
import com.job.TestBatchLegacyConfig;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.generator.UserHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestBatchLegacyConfig.class, QueryDslConfig.class})
public class QuerydslPagingItemReaderTest {

    @Autowired
    private EntityManagerFactory emf; // EntityManagerFactory 주입

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 커스텀_reader_테스트() throws Exception {
        // given
        User savedUser = userRepository.save(createUser());
        User savedUser2 = userRepository.save(createUser());

        QUser qUser = QUser.user;
        int pageSize = 1;

        // when
        QuerydslPagingItemReader<User> reader = new QuerydslPagingItemReader<>(
                emf,
                pageSize,
                queryFactory -> queryFactory.selectFrom(qUser)
        );

        reader.open(new ExecutionContext());
        User readUser = reader.read(); // 첫 번째 사용자 읽기
        User readUser2 = reader.read(); // 두 번째 사용자 읽기
        reader.close(); // 리더 종료

        // then
        assertThat(readUser).isNotNull();
        assertThat(readUser.getId()).isEqualTo(savedUser.getId());
        assertThat(readUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(readUser2.getId()).isEqualTo(savedUser2.getId());
        assertThat(readUser2.getEmail()).isEqualTo(savedUser2.getEmail());
    }
}
