package com.wtg.waytogo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wtg.waytogo.data.pref.UserPreferences
import com.wtg.waytogo.data.response.PlaceResponseItem
import com.wtg.waytogo.data.source.ApiService
import kotlinx.coroutines.flow.first

class PlacePagingSource(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : PagingSource<Int, PlaceResponseItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, PlaceResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = userPreferences.getSession().first().token
            val responseData = apiService.getPlace(token, position)

            LoadResult.Page(
                data = responseData.placeResponse!!,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.placeResponse.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}