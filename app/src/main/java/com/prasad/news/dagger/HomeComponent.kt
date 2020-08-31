package com.prasad.news.dagger


import com.prasad.news.core.dagger.BaseActivityComponent
import com.prasad.news.core.dagger.CoreComponent
import com.prasad.news.core.dagger.scope.FeatureScope
import com.prasad.news.ui.HomeActivity
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [HomeModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface HomeComponent : BaseActivityComponent<HomeActivity> {

    @Component.Builder
    interface Builder {

        fun build(): HomeComponent
        @BindsInstance
        fun homeActivity(activity: HomeActivity): Builder
        fun coreComponent(module: CoreComponent): Builder
    }
}