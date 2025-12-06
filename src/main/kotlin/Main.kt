import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * 결과
 * 시간: 2초
 *
 * 메인 시작
 * [1] 시작: main
 * [1] 완료: main
 * [2] 시작: main
 * [2] 완료: main
 * 결과: Data-1, Data-2
 * 메인 종료
 *
 * launch/async 등으로 새로운 코루틴을 만들지 않았기 때문에
 * suspend 함수(fetchData)는 순차적으로 실행된다.
 */

class Main {
    suspend fun fetchData(id: Int): String { // suspend = 이 메서드는 멈출 수 있다는것을 컴파일러 알려줌
        println("[$id] 시작: ${Thread.currentThread().name}")
        delay(1000) // 1초 대기
        println("[$id] 완료: ${Thread.currentThread().name}")
        return "Data-$id"
    }
}

fun main() = runBlocking { // runBlocking = 메인 스레드를 블로킹하고 스코프 생성
    println("메인 시작")
    val main = Main()
    val data1 = main.fetchData(1)
    val data2 = main.fetchData(2)
    println("결과: $data1, $data2")
    println("메인 종료")
}


