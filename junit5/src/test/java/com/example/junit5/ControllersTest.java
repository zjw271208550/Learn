package com.example.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.api.DynamicTest.stream;

/*
    https://junit.org/junit5/docs/current/user-guide/
 */
@SpringBootTest
/*
    改变一：
    与 JUnit4中的 @Rule 不同，@ExtendWith允许在一个测试类上拥有任意个扩展。
    也不同于 @RunWith,Spring通过 SpringExtension实现了 ParameterResolver（允许动态参数）+JUnit Jupiter
 */
@ExtendWith({SpringExtension.class})
@DisplayName("用户service的测试")
public class ControllersTest {

    @Autowired
    Services services;

    /*
        改变二
        在 JUnit 5 之前，标准的测试类和测试方法是不允许有额外的参数的。
        JUnit 5 除了提供内置的标准参数之外，还可以通过扩展机制来支持额外的参数。
     */
    @DisplayName("用户字符串型变量转整型")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "ab", "1233", "1.235","    " })
    @Order(1)
    void parse(String strValue){
        assertDoesNotThrow(()->Integer.parseInt(strValue));
    }

    @DisplayName("用户整型值加和")
    @ParameterizedTest
    @CsvSource({
            "1,     1",
            "-1,    66",
            "22,    11"
    })
    @Order(2)
    void add(int val1,int val2){
        assertEquals(val1+val2,services.addUserValue(val1,val2));
    }



    /*
        改变三
        以上的 JUnit 5 测试方法的创建都是静态的，JUnit 5 新增了对动态测试的支持
        返回 DynamicContainer或 DynamicTest的流、集合或迭代器。
        一个就是一个测试
        好处：
        1.不用手动输入参数
        2.同一组参数可以同时测多个实例
     */
    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(
                dynamicTest("1st dynamic test", () -> assertTrue(true)),
                dynamicTest("2nd dynamic test", () -> assertEquals(4, 2 * 2))
        );
    }
    @TestFactory
    Stream<DynamicTest> generateRandomNumberOfTests() {
        // 参数输入生成器（迭代器）：生成一个 0~100的整数直到不可整除 7
        Iterator<Integer> inputGenerator = new Iterator<Integer>() {

            Random random = new Random();
            int current;

            @Override
            public boolean hasNext() {
                current = random.nextInt(100);
                return current % 7 != 0;
            }

            @Override
            public Integer next() {
                return current;
            }
        };

        // 测试显示名生成器（方法）
        Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

        // 测试方法
        ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

        return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
    }


    /*
        改变四
        通过 Java 中的内部类和 @Nested 注解实现嵌套测试，从而可以更好的把相关的测试方法组织在一起。
        好处：严格的执行顺序，测试A时会把A之前的先执行
     */
    Map<String, Object> map;
    @Nested
    @DisplayName("when a new")
    class WhenNew {
        @BeforeEach
        void create() {
            map = new HashMap<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(map.isEmpty());
        }

        @Nested
        @DisplayName("after adding a new entry")
        class AfterAdd {
            String key = "key";
            Object value = "value";

            @BeforeEach
            void add() {
                map.put(key, value);
            }

            @Test
            @DisplayName("is not empty")
            void isNotEmpty() {
                assertFalse(map.isEmpty());
            }

            @Test
            @DisplayName("returns value when getting by key")
            void returnValueWhenGettingByKey() {
                assertEquals(value, map.get(key));
            }

            @Nested
            @DisplayName("after removing the entry")
            class AfterRemove {
                @BeforeEach
                void remove() {
                    map.remove(key);
                }

                @Test
                @DisplayName("is empty now")
                void isEmpty() {
                    assertTrue(map.isEmpty());
                }

                @Test
                @DisplayName("returns null when getting by key")
                void returnNullForKey() {
                    assertNull(map.get(key));
                }
            }
        }
    }

}
