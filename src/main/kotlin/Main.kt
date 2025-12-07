import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope


fun main() = runBlocking {
//    runCase1()
//    runCase2()
//    runCase3()
}

/**
 * case1:
 * 예외 없이 두 자식 코루틴이 실행되는 동안
 * 일정 시간 후 부모 job.cancel() 을 통해 전체 취소되는 케이스.
 * → 단순히 "부모 취소 = 자식 모두 취소" 규칙 확인
 */
suspend fun CoroutineScope.runCase1() {
    val job = launch {
        launch {
            repeat(10) {
                println("CASE1 - 자식1: $it")
                delay(500)
            }
        }
        launch {
            repeat(10) {
                println("CASE1 - 자식2: $it")
                delay(500)
            }
        }
    }

    delay(1500)
    job.cancel()
}


/**
 * case2:
 * supervisorScope 없이 launch 자식 내부에서 예외 발생.
 * launch는 예외를 부모로 전파하므로,
 * 부모 → runBlocking → 메인 스레드까지 예외가 올라가 프로그램이 종료된다.
 * → "하나 터지면 전체가 터지는" 구조
 */
suspend fun CoroutineScope.runCase2() {
    val job = launch {
        launch {
            repeat(10) {
                println("CASE2 - 자식1: $it")
                delay(500)
            }
        }
        launch {
            repeat(10) {
                println("CASE2 - 자식2: $it")
                if (it == 5) throw RuntimeException("CASE2 예외 발생: $it")
                delay(500)
            }
        }
    }

    delay(3000)
    job.cancel()
}


/**
 * case3
 * supervisorScope 안에서 자식 코루틴이 예외를 던져도
 * 부모 코루틴과 runBlocking(메인 스레드)로 전파되지 않는다.
 * → "문제 있는 코루틴만 죽고 나머지는 계속 실행" 구조
 */
suspend fun CoroutineScope.runCase3() {
    val job = launch {
        launch {
            repeat(10) {
                println("CASE3 - 자식1: $it")
                delay(500)
            }
        }
        supervisorScope {
            launch {
                repeat(10) {
                    println("CASE3 - 자식2: $it")
                    if (it == 1)
                        throw IllegalArgumentException("CASE3 자식2 예외 발생: $it")
                    delay(500)
                }
            }
        }
    }
    delay(3000)
    job.cancel()
}

