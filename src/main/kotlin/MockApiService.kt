import kotlinx.coroutines.delay

class MockApiService {
    suspend fun fetchUserPosts(userId: Long) {
        println("외부 API로 사용자 POST 불러오는중...")
        delay(1000).let {
            println("사용자 POST 불러오기 완료!")
        }
    }
    suspend fun fetchUserLikes(userId: Long) {
        println("외부 API로 사용자 LIKES 불러오는중...")
        return delay(1000).let {
            println("사용자 LIKES 불러오기 완료!")
        }
    }
}