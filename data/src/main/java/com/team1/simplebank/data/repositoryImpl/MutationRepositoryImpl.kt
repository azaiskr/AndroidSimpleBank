package com.team1.simplebank.data.repositoryImpl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.synrgy.xdomain.model.GetAmountsMutationUI
import com.synrgy.xdomain.model.MutationDataUI
import com.synrgy.xdomain.repositoryInterface.MutationRepository
import com.team1.simplebank.common.handler.ResourceState
import com.team1.simplebank.data.dataStore.AuthDataStore
import com.team1.simplebank.data.mapper.mapperGetAmountsMutationToGetAmountsUI
import com.team1.simplebank.data.mapper.mapperMutationResponseApiToMutationDataUI
import com.team1.simplebank.data.remote.api.ApiService
import com.team1.simplebank.data.repositoryImpl.pagingsource.MutationPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MutationRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
) : MutationRepository {
    override fun getDataMutation(
        inputDataNoAccount: String,
        inputDataMonth: Int,
        inputType: String?,
    ): Flow<PagingData<MutationDataUI>> {
        val index = MutationPagingSource.INITIAL_PAGE_INDEX
        Log.d(
            "MutationRepositoryImpl",
            "Creating PagingSource with noAccount: $index $inputDataNoAccount, month: $inputDataMonth, type: $inputType"
        )
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                MutationPagingSource(apiService, inputDataNoAccount, inputDataMonth, inputType)
            }
        ).flow
    }

    override fun getNoAccount(): Flow<String?> {
        return authDataStore.getNoAccount()
    }


}