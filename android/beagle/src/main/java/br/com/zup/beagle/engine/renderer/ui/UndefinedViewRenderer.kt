package br.com.zup.beagle.engine.renderer.ui

import android.graphics.Color
import android.view.View
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.UIViewRenderer
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.setup.Environment
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.ui.UndefinedWidget

internal class UndefinedViewRenderer(
    override val component: UndefinedWidget,
    private val viewFactory: ViewFactory = ViewFactory()
) : UIViewRenderer<UndefinedWidget>() {

    override fun buildView(rootView: RootView): View {
        return if (BeagleEnvironment.beagleSdk.config.environment == Environment.DEBUG) {
            viewFactory.makeTextView(rootView.getContext()).apply {
                text = "undefined component"
                setTextColor(Color.RED)
                setBackgroundColor(Color.YELLOW)
            }
        } else {
            viewFactory.makeView(rootView.getContext())
        }
    }
}