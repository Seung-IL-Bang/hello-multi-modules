package io.hello.demo.testmodule.unittest.simplesystem.userinputvalidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInputValidatorTest {

    private UserInputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserInputValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"홍길동", "John Doe", "김 철수", "Lee Ji Eun", "박 선 아"})
    @DisplayName("유효한 이름 입력에 대해 isValidName은 true를 반환한다.")
    void isValidName_validInput(String name) {
        assertTrue(validator.isValidName(name));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNames")
    @DisplayName("유효하지 않은 이름 입력에 대해 isValidName은 false를 반환한다.")
    void isValidName_invalidInput(String name) {
        assertFalse(validator.isValidName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name@subdomain.example.co.kr", "very.long.email.address-with-hyphens@and.multiple-subdomains.example.museum", "test123@gmail.com"})
    @DisplayName("유효한 이메일 주소 입력에 대해 isValidEmail은 true를 반환한다.")
    void isValidEmail_validInput(String email) {
        assertTrue(validator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"testexample.com", "test@example", "@example.com", "test@.com", "test@example.", "test@example!.com", "test@example com"})
    @DisplayName("유효하지 않은 이메일 주소 입력에 대해 isValidEmail은 false를 반환한다.")
    void isValidEmail_invalidInput(String email) {
        assertFalse(validator.isValidEmail(email));
    }

    /**
     * @ValueSource는 null 값을 지원하지 않아!
     * @NullSource는 null 값을 테스트할 때 사용해. 이 경우, null 값이 들어간 경우에 대한 테스트를 작성했어.
     */
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 빈 문자열 값의 이메일 주소 입력에 대해 isValidEmail은 false를 반환한다.")
    void isValidEmail_NullInput(String email) {
        assertFalse(validator.isValidEmail(email));
    }

    /**
     * Java에서 @ValueSource는 컴파일 타임에 상수로 결정되는 값만 사용할 수 있어. 그런데 아래 코드:
     * 여기서 " a".repeat(51)는 런타임에 평가되는 표현식이기 때문에, @ValueSource에서는 사용할 수 없어. → 컴파일 에러 발생
     * 해결방법. @MethodSource로 변경 -> " a".repeat(51)처럼 동적으로 만든 문자열도 테스트에 사용할 수 있어.
     */
    @ParameterizedTest
    @MethodSource("provideValidNames")
    @DisplayName("최대 허용 길이 이하의 이름에 대해 isValidName은 true를 반환한다.")
    void isValidName_maxLength(String name) {
        assertTrue(validator.isValidName(name));
    }

    @Test
    @DisplayName("공백으로만 이루어진 이름에 대해 isValidName은 false를 반환한다.")
    void isValidName_onlyWhitespace() {
        assertFalse(validator.isValidName("   "));
    }

    private static Stream<String> provideInvalidNames() {
        return Stream.of(
                "홍길동123",
                "John!Doe",
                "김#철수",
                "Lee^Ji*Eun",
                "   a".repeat(51),
                "",
                null
        );
    }

    private static Stream<String> provideValidNames() {
        return Stream.of(
                "a".repeat(50),
                "가".repeat(20) + "b".repeat(20)
        );
    }

}