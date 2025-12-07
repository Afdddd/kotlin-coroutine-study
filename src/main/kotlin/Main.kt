import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 *  async/await
 *  결과를 반환하는 비동기 작업
 * - 반환값 : Deferred(await() 가능)
 * - 여러 비동기 작업을 병렬로 실행할 때
 * - DB + 외부 API + 파일 읽기 등 병렬 처리할 때
 *
 *  Deferred
 *  나중에 결과가 도착할 비동기 작업을 표현한 객체
 * - async를 쓰면 항상 이것이 반환
 * - await()를 호출해 결과를 꺼낼수있다.
 */

fun main() = runBlocking {

    experimentSequential()   // 잘못된 사용: await를 바로 호출 → 직렬
    println()
    experimentParallel()     // 올바른 사용: async 먼저 실행 → await로 결과만 모음
    println()
}

/**
 * 직렬 (잘못된 사용)
 * async 호출 직후 await를 붙이면 각 작업이 끝날 때까지 순차적으로 대기한다.
 */
private suspend fun CoroutineScope.experimentSequential() {
    val userService = MockUserService()
    val apiService = MockApiService()

    val start = System.currentTimeMillis()
    println("Sequential (잘못된 사용)")

    // async 바로 뒤에 await() -> 사실상 순차 호출
    async { userService.getUserFromDb(1) }.await()
    async { apiService.fetchUserPosts(1) }.await()
    async { apiService.fetchUserLikes(1) }.await()

    val end = System.currentTimeMillis()
    println("Duration: ${end - start} ms")
    /**
     * 결과
     * DB에서 사용자 정보 조회중...
     * 사용자 정보 조회 완료!
     * 외부 API로 사용자 POST 불러오는중...
     * 사용자 POST 불러오기 완료!
     * 외부 API로 사용자 LIKES 불러오는중...
     * 사용자 LIKES 불러오기 완료!
     * Duration: 3025 ms
     */
}


/**
 * 병렬
 * async를 먼저 모두 실행해두고, 나중에 await로 결과만 모으면 병렬로 동작한다.
 */
private suspend fun CoroutineScope.experimentParallel() {
    val userService = MockUserService()
    val apiService = MockApiService()

    val start = System.currentTimeMillis()
    println("Parallel (권장)")

    // async로 작업을 동시에 시작
    val userDeferred = async { userService.getUserFromDb(1) }
    val postsDeferred = async { apiService.fetchUserPosts(1) }
    val likesDeferred = async { apiService.fetchUserLikes(1) }

    // 결과는 나중에 모아서 받기(이미 작업은 병렬로 진행 중)
    userDeferred.await()
    postsDeferred.await()
    likesDeferred.await()

    val end = System.currentTimeMillis()
    println("Duration: ${end - start} ms")
    /**
     * 결과
     * Parallel (권장)
     * DB에서 사용자 정보 조회중...
     * 외부 API로 사용자 POST 불러오는중...
     * 외부 API로 사용자 LIKES 불러오는중...
     * 사용자 정보 조회 완료!
     * 사용자 POST 불러오기 완료!
     * 사용자 LIKES 불러오기 완료!
     * Duration: 1006 ms
     */
}