import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Main {
    suspend fun fetchData(id: Int): String {
        println("[$id] 시작: ${Thread.currentThread().name}")
        delay(1000)
        println("[$id] 완료: ${Thread.currentThread().name}")
        return "Data-$id"
    }
}

/**
 * launch - 결과 없는 병렬 실행
 * 
 * 핵심 내용:
 * 1. launch는 Job을 반환 (코루틴의 생명주기 관리 객체)
 * 2. join() 없이는 메인이 먼저 종료될 수 있음
 * 3. 병렬 실행으로 2초 → 1초로 단축
 * 
 * Job 주요 기능:
 * - job.join()   : 완료될 때까지 대기 (suspend)
 * - job.cancel() : 즉시 취소
 * - job.isActive / isCompleted / isCancelled : 상태 확인
 */

fun main() = runBlocking {
     experiment1()
//     experiment2()
}

/**
 * 실험 1: join() 없이 launch만 사용
 * 
 * 결과:
 * 메인 시작
 * 메인 종료: 1 ms        ← 메인이 먼저 끝남
 * [1] 시작: main
 * [2] 시작: main
 * [1] 완료: main
 * [2] 완료: main
 * 
 * 관찰: 
 * - launch는 즉시 반환
 * - 코루틴은 백그라운드에서 실행
 * - runBlocking이 자식 코루틴을 자동으로 기다려줌
 */
suspend fun CoroutineScope.experiment1() {

        println("\n=== 실험 1: launch without join ===")
        println("메인 시작")
        val main = Main()
        val startTime = System.currentTimeMillis()


        launch { main.fetchData(1) }
        launch { main.fetchData(2) }

        val elapsed = System.currentTimeMillis() - startTime
        println("메인 종료: $elapsed ms")
}

/**
 * 실험 2: join()으로 명시적 대기
 * 
 * 결과:
 * 메인 시작
 * [1] 시작: main
 * [2] 시작: main
 * [1] 완료: main
 * [2] 완료: main
 * 메인 종료: 1016 ms     ← 1초 후에 종료!
 * 
 * 관찰:
 * - join()은 해당 Job이 완료될 때까지 대기 (suspend)
 * - 두 작업이 동시 실행되어 총 1초만 소요
 * - join() 순서는 상관없음 (이미 시작된 상태)
 */
suspend fun CoroutineScope.experiment2() {
    println("\n=== 실험 2: launch with join ===")
    println("메인 시작")
    val main = Main()
    val startTime = System.currentTimeMillis()


    val job1 = launch { main.fetchData(1) }
    val job2 = launch { main.fetchData(2) }

    job2.join()  // job2 완료 대기
    job1.join()  // job1 완료 대기

    val elapsed = System.currentTimeMillis() - startTime
    println("메인 종료: $elapsed ms")
}