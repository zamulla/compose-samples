package com.example.jetcaster.core.data.di

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.example.jetcaster.core.data.JetcasterDispatchers
import com.example.jetcaster.core.data.JetcasterDispatchers.ioDispatcher
import com.example.jetcaster.core.data.JetcasterDispatchers.mainDispatcher
import com.example.jetcaster.core.data.database.JetcasterDatabase
import com.example.jetcaster.core.data.database.dao.TransactionRunner
import com.example.jetcaster.core.data.network.PodcastsFetcher
import com.example.jetcaster.core.data.repository.CategoryStore
import com.example.jetcaster.core.data.repository.EpisodeStore
import com.example.jetcaster.core.data.repository.LocalCategoryStore
import com.example.jetcaster.core.data.repository.LocalEpisodeStore
import com.example.jetcaster.core.data.repository.LocalPodcastStore
import com.example.jetcaster.core.data.repository.PodcastStore
import com.example.jetcaster.core.data.repository.PodcastsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

// TODO we should name this module properly
val otherModule = module {
    single { get<JetcasterDatabase>().categoriesDao() }
    single { get<JetcasterDatabase>().podcastCategoryEntryDao() }
    single { get<JetcasterDatabase>().podcastsDao() }
    single { get<JetcasterDatabase>().episodesDao() }
    single { get<JetcasterDatabase>().podcastFollowedEntryDao() }

    factory<TransactionRunner> {
        TransactionRunner { tx ->
            // TODO is this the 1:1 mapping?
            get<JetcasterDatabase>().useWriterConnection {
                it.immediateTransaction { tx() }
            }
        }
    }

    single<CoroutineDispatcher>(ioDispatcher) { Dispatchers.IO }
    single<CoroutineDispatcher>(mainDispatcher) { Dispatchers.Main }

    single<EpisodeStore> { LocalEpisodeStore(get()) }

    single<PodcastStore> {
        LocalPodcastStore(
            podcastDao = get(),
            podcastFollowedEntryDao = get(),
            transactionRunner = get(),
        )
    }

    single<CategoryStore> {
        LocalCategoryStore(
            episodesDao = get(),
            podcastsDao = get(),
            categoriesDao = get(),
            categoryEntryDao = get(),
        )
    }

    single {
        PodcastsFetcher(
            ioDispatcher = get<CoroutineDispatcher>(ioDispatcher),
        )
    }

    single {
        PodcastsRepository(
            podcastsFetcher = get<PodcastsFetcher>(),
            podcastStore = get<PodcastStore>(),
            episodeStore = get<EpisodeStore>(),
            categoryStore = get<CategoryStore>(),
            transactionRunner = get<TransactionRunner>(),
            mainDispatcher = get<CoroutineDispatcher>(mainDispatcher),
        )
    }
}