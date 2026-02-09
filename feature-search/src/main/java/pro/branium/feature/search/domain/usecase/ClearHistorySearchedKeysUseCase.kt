package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import javax.inject.Inject

class ClearHistorySearchedKeysUseCase @Inject constructor(
    private val repository: HistorySearchedKeyRepository
) {
    suspend operator fun invoke() {
        repository.clearAllKeys()
    }
}