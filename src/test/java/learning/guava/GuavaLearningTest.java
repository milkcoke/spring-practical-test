package learning.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.*;

/**
 * 테스트를 학습의 도구로 사용한다.
 * 구체적인 테스트 케이스를 작성해보며 능동적으로 이해할 수 있다.
 */
public class GuavaLearningTest {

  @Test
  @DisplayName("1-10 숫자를 3개 파티션 리스트로 나누면 3으로 나뉜다.")
  public void listPartition() {
    // given
    List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    // when
    List<List<Integer>> partitionList = Lists.partition(integers, 3);

    // then
    assertThat(partitionList).hasSize(4)
        .isEqualTo(
            List.of(
                List.of(1,2,3), List.of(4,5,6), List.of(7,8,9), List.of(10)
            )
        );
  }

  @DisplayName("카페 메뉴 품목 및 개수 테스트")
  @TestFactory
  Collection<DynamicTest> multiMap() {
    // given
    Multimap<String, String> menuMap = ArrayListMultimap.create();
    menuMap.put("커피", "아메리카노");
    menuMap.put("커피", "카페라떼");
    menuMap.put("커피", "카푸치노");
    menuMap.put("빵", "소보로");
    menuMap.put("빵", "크루아상");

    return List.of(
        dynamicTest("커피 메뉴는 총 3개로 아메리카노, 카페라떼, 카푸치노로 구성된다.", () -> {
          // when
          Collection<String> coffeeMenus = menuMap.get("커피");
          // then
          assertThat(coffeeMenus).hasSize(3)
              .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
        }),
        dynamicTest("빵 메뉴는 총 2개로 소보로, 크루아상으로 구성된다.", () -> {
          // when
          var breadMenus = menuMap.get("빵");

          // then
          assertThat(breadMenus).hasSize(2)
              .isEqualTo(List.of("소보로", "크루아상"));
        }),
        dynamicTest("커피의 카푸치노 삭제시 아메리카노와 카페라떼만 남는다.", ()->{
          // when
          menuMap.remove("커피", "카푸치노");
          var coffeeMenus = menuMap.get("커피");
          // then
          assertThat(coffeeMenus).hasSize(2)
              // 순서도 지켜져야한다.
              .isEqualTo(List.of("아메리카노", "카페라떼"));
        }),
        dynamicTest("커피 메뉴 삭제시 모든 커피가 삭제된다", ()->{
          // when
          menuMap.removeAll("커피");
          var coffeeMenus = menuMap.get("커피");
          // then
          assertThat(coffeeMenus).hasSize(0)
              .isEmpty();
        })
    );
  }
}
