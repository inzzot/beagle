package br.com.zup.beagle.engine.renderer

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.layout.ContainerViewRenderer
import br.com.zup.beagle.engine.renderer.layout.FormViewRenderer
import br.com.zup.beagle.engine.renderer.layout.HorizontalViewRenderer
import br.com.zup.beagle.engine.renderer.layout.LazyComponentViewRenderer
import br.com.zup.beagle.engine.renderer.layout.TouchableViewRenderer
import br.com.zup.beagle.engine.renderer.layout.PageViewRenderer
import br.com.zup.beagle.engine.renderer.layout.ScreenViewRenderer
import br.com.zup.beagle.engine.renderer.layout.ScrollViewRenderer
import br.com.zup.beagle.engine.renderer.layout.SpacerViewRenderer
import br.com.zup.beagle.engine.renderer.layout.StackViewRenderer
import br.com.zup.beagle.engine.renderer.layout.VerticalViewRender
import br.com.zup.beagle.widget.layout.ScreenComponent
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Horizontal
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.layout.Spacer
import br.com.zup.beagle.widget.layout.Stack
import br.com.zup.beagle.widget.layout.Vertical
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.navigation.Touchable

internal class LayoutViewRendererFactory : AbstractViewRendererFactory {

    @Throws(IllegalArgumentException::class)
    override fun make(component: ServerDrivenComponent): ViewRenderer<*> {

        return when (component) {
            is Container -> ContainerViewRenderer(component)
            is ScreenComponent -> ScreenViewRenderer(component)
            is Vertical -> VerticalViewRender(component)
            is Horizontal -> HorizontalViewRenderer(component)
            is Stack -> StackViewRenderer(component)
            is Spacer -> SpacerViewRenderer(component)
            is Touchable -> TouchableViewRenderer(component)
            is Form -> FormViewRenderer(component)
            is ScrollView -> ScrollViewRenderer(component)
            is PageView -> PageViewRenderer(component)
            is LazyComponent -> LazyComponentViewRenderer(component)
            else -> throw IllegalArgumentException("$component is not a Layout Widget.")
        }
    }
}