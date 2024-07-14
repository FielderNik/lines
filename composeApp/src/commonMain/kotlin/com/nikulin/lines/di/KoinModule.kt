package com.nikulin.lines.di

import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.domain.repositories.LinesRepositoryImpl
import com.nikulin.lines.presentation.splash.SplashViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module


private val viewModelsModule = module {
    viewModelOf(::SplashViewModel)
}


private val dataModule = module {

}

private val repositoryModule = module {
    factory<LinesRepository> {
        LinesRepositoryImpl()
    }
}

private val useCaseModule = module {

}

private val coreModule = module {
    single<DispatchProvider> {
        DispatchProvider()
    }
}

fun initKoin() {

    startKoin {
        modules(
            coreModule,
            viewModelsModule,
            dataModule,
            repositoryModule,
            useCaseModule,
            platformModule
        )
    }
}