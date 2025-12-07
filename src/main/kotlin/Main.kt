import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *  Dispatcher
 *  코루틴이 어떤 스레드에서 실행될지를 결정하는 전략
 *
 *  Dispatchers.Default
 *  - CPU 작업(연산, 정렬, 파싱)에 최적화
 *  - CPU 코어 수 만큼 스레드풀 제공
 *  - 무거운 연산 => Default
 *
 *  Dispatchers.IO
 *  - 파일, DB, 네트워크 등 IO 작업 전용
 *  - Default보다 많은 스레드 보유
 *
 *
 *   launch(Dispatchers.IO) 처럼 개발자가 명시하지 않으면 부모 코루틴의 디스패처를 그대로 상속한다.
 *   부모가 없다면 Default를 사용한다.
 */


fun main() = runBlocking<Unit> {
    println("runBlocking: ${Thread.currentThread().name}")

    launch { println("launch: ${Thread.currentThread().name}") }

    launch(Dispatchers.IO) { println("launch(Dispatchers.IO): ${Thread.currentThread().name}") }

    launch(Dispatchers.Default) { println("launch(Dispatchers.Default): ${Thread.currentThread().name}") }
}
