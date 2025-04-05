### 사용한 디자인 패턴

1. **전략 패턴(Strategy Pattern)**: 이벤트 처리 로직을 `EventProcessingStrategy` 인터페이스로 추상화하고, 이벤트 상태별로 다른 구현체(`PendingEventStrategy`, `ActiveEventStrategy`, `EndedEventStrategy`)를 제공했습니다. 이를 통해 이벤트 상태에 따른 처리 로직을 캡슐화하고 런타임에 전략을 교체할 수 있습니다.
2. **팩토리 메소드 패턴**: `EventParticipationResponse`의 `success()`, `failure()` 정적 메소드를 통해 응답 객체 생성을 캡슐화했습니다.
3. **빌더 패턴(Builder Pattern)**: `Coupon` 클래스에 빌더 패턴을 적용하여 복잡한 객체 생성 과정을 단순화했습니다.
4. **싱글톤 패턴**: `ConcurrentLockManager`를 통해 이벤트별 락을 관리하여 동시성 문제를 해결했습니다.
5. **리포지토리 패턴(Repository Pattern)**: 데이터 접근 로직을 `EventRepository`와 `CouponRepository` 인터페이스로 추상화하고, 구현체를 분리했습니다.

### SOLID 원칙 적용

1. **단일 책임 원칙(SRP)**: 각 클래스가 명확한 하나의 책임을 가지도록 설계했습니다. 예를 들어, `Event` 클래스는 이벤트 정보와 상태 관리, `EventService`는 이벤트 참여 로직 조정, 각 전략 클래스는 특정 상태의 이벤트 처리만 담당합니다.
2. **개방-폐쇄 원칙(OCP)**: 새로운 이벤트 처리 전략이 필요할 때 기존 코드를 수정하지 않고 `EventProcessingStrategy` 인터페이스를 구현하는 새 클래스를 추가할 수 있습니다. 또한 새로운 저장소 구현체도 인터페이스를 통해 쉽게 추가할 수 있습니다.
3. **리스코프 치환 원칙(LSP)**: 모든 전략 구현체는 `EventProcessingStrategy` 인터페이스의 계약을 준수하며, 상위 타입으로 참조해도 동작이 보장됩니다.
4. **인터페이스 분리 원칙(ISP)**: 리포지토리 인터페이스는 필요한 메소드만 정의하고 있으며, 처리 전략 인터페이스도 최소한의 메소드만 포함합니다.
5. **의존성 역전 원칙(DIP)**: `EventService`는 구체적인 구현체가 아닌 `EventRepository`, `CouponRepository`, `EventProcessingStrategy` 인터페이스에 의존합니다.

### 동시성 처리

1. **세밀한 락 관리**: 이벤트별로 별도의 락을 사용하여 동시성 문제를 해결하면서도 서로 다른 이벤트의 처리가 상호 간섭하지 않도록 했습니다.
2. **원자적 연산**: `AtomicInteger`와 CAS(Compare-And-Swap) 연산을 사용하여 쿠폰 차감 과정이 원자적으로 처리되도록 했습니다.
3. **불변성(Immutability)**: `Coupon` 객체를 불변으로 설계하여 동시성 문제를 최소화했습니다.
4. **Thread-safe 컬렉션**: `ConcurrentHashMap`, `CopyOnWriteArrayList` 등 스레드 안전한 컬렉션을 사용했습니다.

이 설계는 대량의 동시 요청을 효율적으로 처리하면서도 정확한 선착순 순서를 보장하며, 이벤트 상태에 따른 다양한 처리 로직을 유연하게 지원합니다. 또한 락 관리와 원자적 연산을 통해 데이터 일관성을 유지하면서 높은 처리량을 제공합니다.