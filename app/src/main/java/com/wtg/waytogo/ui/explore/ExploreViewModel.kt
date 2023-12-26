package com.wtg.waytogo.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wtg.waytogo.data.pref.UserModel
import com.wtg.waytogo.data.repository.UserRepository
import com.wtg.waytogo.data.response.PlaceItem

class ExploreViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getPlace(token: String): LiveData<PagingData<PlaceItem>> = repository.getPlaces(token).cachedIn(viewModelScope)

}