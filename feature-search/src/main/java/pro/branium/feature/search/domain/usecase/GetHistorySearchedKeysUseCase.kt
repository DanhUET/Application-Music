package pro.branium.feature.search.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel
import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import javax.inject.Inject


class GetHistorySearchedKeysUseCase @Inject constructor(
    private val repository: HistorySearchedKeyRepository
) {
    operator fun invoke(): Flow<List<HistorySearchedKeyModel>> =
        repository.getHistorySearchedKeysForUser()
}