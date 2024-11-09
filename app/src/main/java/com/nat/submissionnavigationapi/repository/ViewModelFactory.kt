package com.nat.submissionnavigationapi.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nat.submissionnavigationapi.ui.detail.DetailViewModel
import com.nat.submissionnavigationapi.ui.favorite.FavoriteViewModel // Tambahkan import ini

class ViewModelFactory private constructor(private val repository: EventRepository) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(EventRepository.Injection.provideRepository(application)).also { instance = it }
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) { // Tambahkan blok ini
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}