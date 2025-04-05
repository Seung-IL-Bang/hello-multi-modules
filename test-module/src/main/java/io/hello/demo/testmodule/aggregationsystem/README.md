### 사용한 디자인 패턴

1. **전략 패턴(Strategy Pattern)**: `StatisticsCalculator` 인터페이스와 이를 구현한 여러 계산기 클래스(`PaymentAmountCalculator`, `PaymentCountCalculator`, `PaymentMethodRatioCalculator`)를 통해 다양한 통계 계산 알고리즘을 캡슐화했습니다. 이를 통해 클라이언트가 통계 유형에 따라 적절한 알고리즘을 선택할 수 있습니다.
2. **팩토리 패턴(Factory Pattern)**: `StatisticsCalculatorFactory`에서 통계 유형에 따라 적절한 계산기 인스턴스를 생성하여 제공합니다. 이를 통해 클라이언트는 구체적인 계산기 구현체를 직접 생성하지 않아도 됩니다.
3. **빌더 패턴(Builder Pattern)**: `Coupon.Builder`를 통해 복잡한 객체 생성 과정을 단순화했습니다.
4. **캐싱 패턴(Caching Pattern)**: `StatisticsService`에서 동일한 요청에 대한 결과를 캐싱하여 성능을 최적화했습니다. 읽기/쓰기 락을 사용하여 스레드 안전성을 보장했습니다.
5. **리포지토리 패턴(Repository Pattern)**: `PaymentRepository` 인터페이스를 통해 데이터 접근 로직을 추상화하고, 구현체를 분리했습니다.
6. **템플릿 메소드 패턴**: 각 계산기에서 공통적인 계산 흐름을 정의하고, 세부 구현은 각 하위 클래스에서 담당하도록 했습니다.

### SOLID 원칙 적용

1. **단일 책임 원칙(SRP)**:
    - 각 클래스가 명확한 하나의 책임을 가지도록 설계했습니다.
    - `StatisticsService`는 통계 요청 조율 및 캐싱 담당
    - 각 `StatisticsCalculator` 구현체는 특정 유형의 통계 계산만 담당
    - `DateUtil`은 날짜 관련 유틸리티 기능만 담당
2. **개방-폐쇄 원칙(OCP)**:
    - 새로운 통계 유형이 필요할 때 기존 코드를 수정하지 않고 `StatisticsCalculator` 인터페이스를 구현하는 새 클래스를 추가할 수 있습니다.
    - `StatisticsCalculatorFactory`의 `registerCalculator` 메소드를 통해 새로운 계산기를 동적으로 등록할 수 있습니다.
3. **리스코프 치환 원칙(LSP)**:
    - 모든 `StatisticsCalculator` 구현체는 인터페이스의 계약을 준수하며, 상위 타입으로 대체해도 정상 동작합니다.
    - 각 구현체는 `calculate` 메소드를 통해 동일한 입력(`List<Payment>, StatisticsRequest`)에 대해 일관된 출력(`StatisticsResponse`)을 제공합니다.
4. **인터페이스 분리 원칙(ISP)**:
    - `StatisticsCalculator` 인터페이스는 통계 계산에 필요한 최소한의 메소드만 정의합니다.
    - `PaymentRepository` 인터페이스도 필요한 조회 메소드만 포함합니다.
5. **의존성 역전 원칙(DIP)**:
    - 고수준 모듈(`StatisticsService`)이 저수준 모듈(`InMemoryPaymentRepository` 등)에 직접 의존하지 않고, 추상화(`PaymentRepository`, `StatisticsCalculator`)에 의존합니다.
    - 이를 통해 다양한 구현체로 쉽게 교체할 수 있는 유연한 구조를 갖습니다.

### 추가적인 설계 고려사항

1. **동시성 처리**:
    - `ConcurrentHashMap`과 `ReadWriteLock`을 사용하여 캐시의 스레드 안전성을 보장했습니다.
    - 읽기 작업은 여러 스레드가 동시에 수행 가능하도록 했습니다.
2. **캐싱 전략**:
    - 결과 캐싱을 통해 반복적인 계산을 피하고 성능을 향상시켰습니다.
    - 캐시 만료 시간을 설정하여 데이터의 신선도를 보장했습니다.
3. **확장성**:
    - 새로운 통계 유형이나 기간 단위, 그룹화 기준을 쉽게 추가할 수 있는 구조로 설계했습니다.
    - 대용량 데이터 처리를 위해 스트림 API와 병렬 처리를 활용했습니다.

이 설계는 다양한 통계 요구사항을 유연하게 지원하며, 확장성과 유지보수성이 뛰어납니다. 또한 효율적인 데이터 처리를 위한 캐싱 전략과 동시성 제어 메커니즘을 갖추고 있어 대용량 결제 데이터를 효과적으로 처리할 수 있습니다.