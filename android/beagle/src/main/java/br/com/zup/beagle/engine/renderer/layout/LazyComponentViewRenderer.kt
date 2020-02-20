package br.com.zup.beagle.engine.renderer.layout

import android.view.View
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.lazy.LazyComponent

internal class LazyComponentViewRenderer(
    override val component: LazyComponent,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<LazyComponent>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        return viewFactory.makeBeagleView(rootView.getContext()).apply {
            val initialState = component.initialState.toView(rootView)
            addView(initialState)
            updateView(rootView, component.path, initialState)
        }
    }
}