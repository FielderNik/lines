package com.nikulin.lines.di

import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.core.LinesParser
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.domain.repositories.LinesRepositoryImpl
import com.nikulin.lines.presentation.main.MainViewModel
import com.nikulin.lines.presentation.splash.SplashViewModel
import com.nikulin.lines.presentation.upload.UploadViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


private val viewModelsModule = module {
    viewModelOf(::SplashViewModel)

    viewModelOf(::UploadViewModel)

    viewModelOf(::MainViewModel)
}



private val dataModule = module {

}

private val repositoryModule = module {
    single <LinesRepository> {
        LinesRepositoryImpl()
    }
}

private val useCaseModule = module {

}

private val coreModule = module {

    singleOf(::DispatchProvider)

    factoryOf(::LinesParser)
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