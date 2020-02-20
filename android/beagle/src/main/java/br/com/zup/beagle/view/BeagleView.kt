package br.com.zup.beagle.view

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.BeagleViewModel
import br.com.zup.beagle.data.ViewState
import br.com.zup.beagle.engine.renderer.ActivityRootView
import br.com.zup.beagle.engine.renderer.FragmentRootView
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.interfaces.OnStateUpdatable
import br.com.zup.beagle.utils.implementsGenericTypeOf
import br.com.zup.beagle.utils.toView

sealed class BeagleViewState {
    data class Error(val throwable: Throwable) : BeagleViewState()
    object LoadStarted : BeagleViewState()
    object LoadFinished : BeagleViewState()
}

interface StateChangedListener {
    fun onStateChanged(state: BeagleViewState)
}

internal class BeagleView(
    context: Context
) : BeagleFlexView(context) {

    var stateChangedListener: StateChangedListener? = null

    private lateinit var rootView: RootView

    private val viewModel by lazy { BeagleViewModel() }

    fun loadView(rootView: RootView, url: String) {
        loadView(rootView, url, null)
    }

    fun updateView(rootView: RootView, url: String, view: View) {
        loadView(rootView, url, view)
    }

    private fun loadView(rootView: RootView, url: String, view: View?) {
        this.rootView = rootView
        viewModel.state.observe(rootView.getLifecycleOwner(), Observer<ViewState> { state ->
            handleResponse(state, view)
        })

        viewModel.fetchComponent(url)

    }

    private fun handleResponse(
        state: ViewState?, view: View?) {
        when (state) {
            is ViewState.Loading -> handleLoading(state.value)
            is ViewState.Error -> handleError(state.throwable)
            is ViewState.Result<*> -> renderComponent(state.data as ServerDrivenComponent, view)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        val state = if (isLoading) {
            BeagleViewState.LoadStarted
        } else {
            BeagleViewState.LoadFinished
        }
        stateChangedListener?.onStateChanged(state)
    }

    private fun handleError(throwable: Throwable) {
        stateChangedListener?.onStateChanged(BeagleViewState.Error(throwable))
    }

    private fun renderComponent(component: ServerDrivenComponent, view: View? = null) {
        if (view != null) {
            if (component.implementsGenericTypeOf(OnStateUpdatable::class.java, component::class.java)) {
                (component as? OnStateUpdatable<ServerDrivenComponent>)?.onUpdateState(component)
            } else {
                val componentView = component.toView(rootView)
                removeView(view)
                addView(componentView)
            }
        } else {
            val componentView = component.toView(rootView)
            removeAllViewsInLayout()
            addView(componentView)
        }
    }

    private fun generateViewModelInstance(): BeagleViewModel {
        return when (rootView) {
            is ActivityRootView -> {
                val activity = (rootView as ActivityRootView).activity
                ViewModelProviders.of(activity)[BeagleViewModel::class.java]
            } else -> {
                val fragment = (rootView as FragmentRootView).fragment
                ViewModelProviders.of(fragment)[BeagleViewModel::class.java]
            }
        }
    }
}
