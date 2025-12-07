import kotlinx.coroutines.delay

class MockUserService {
    suspend fun getUserFromDb(id: Long) {
        println("DB에서 사용자 정보 조회중...")
        delay(1000).let {
            println("사용자 정보 조회 완료!")
        }

    }
}