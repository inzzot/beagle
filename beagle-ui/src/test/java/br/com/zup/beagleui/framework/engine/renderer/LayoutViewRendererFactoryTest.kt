package br.com.zup.beagleui.framework.engine.renderer

import br.com.zup.beagleui.framework.engine.renderer.layout.ContainerViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.FlexSingleWidgetViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.FlexWidgetViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.FormViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.HorizontalViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.NavigatorViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.SpacerViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.StackViewRenderer
import br.com.zup.beagleui.framework.engine.renderer.layout.VerticalViewRender
import br.com.zup.beagleui.framework.widget.core.Widget
import br.com.zup.beagleui.framework.widget.form.Form
import br.com.zup.beagleui.framework.widget.layout.Container
import br.com.zup.beagleui.framework.widget.layout.FlexSingleWidget
import br.com.zup.beagleui.framework.widget.layout.FlexWidget
import br.com.zup.beagleui.framework.widget.layout.Horizontal
import br.com.zup.beagleui.framework.widget.layout.Spacer
import br.com.zup.beagleui.framework.widget.layout.Stack
import br.com.zup.beagleui.framework.widget.layout.Vertical
import br.com.zup.beagleui.framework.widget.navigation.Navigator
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import kotlin.test.assertFails

class LayoutViewRendererFactoryTest {

    private lateinit var viewRendererFactory: LayoutViewRendererFactory

    @Before
    fun setUp() {
        viewRendererFactory = LayoutViewRendererFactory()
    }

    @Test
    fun make_should_return_ContainerViewRenderer_when_widget_is_a_Container() {
        // Given
        val widget = mockk<Container>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is ContainerViewRenderer)
    }

    @Test
    fun make_should_return_VerticalViewRender_when_widget_is_a_Vertical() {
        // Given
        val widget = mockk<Vertical>()
        every { widget.children } returns listOf()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is VerticalViewRender)
    }

    @Test
    fun make_should_return_HorizontalViewRenderer_when_widget_is_a_Horizontal() {
        // Given
        val widget = mockk<Horizontal>()
        every { widget.children } returns listOf()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is HorizontalViewRenderer)
    }

    @Test
    fun make_should_return_StackViewRenderer_when_widget_is_a_Stack() {
        // Given
        val widget = mockk<Stack>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is StackViewRenderer)
    }

    @Test
    fun make_should_return_SpacerViewRenderer_when_widget_is_a_Spacer() {
        // Given
        val widget = mockk<Spacer>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is SpacerViewRenderer)
    }

    @Test
    fun make_should_return_FlexWidgetViewRenderer_when_widget_is_a_FlexWidget() {
        // Given
        val widget = mockk<FlexWidget>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is FlexWidgetViewRenderer)
    }

    @Test
    fun make_should_return_FlexSingleWidgetViewRenderer_when_widget_is_a_FlexSingleWidget() {
        // Given
        val widget = mockk<FlexSingleWidget>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is FlexSingleWidgetViewRenderer)
    }

    @Test
    fun make_should_return_NavigatorViewRenderer_when_widget_is_a_Navigator() {
        // Given
        val widget = mockk<Navigator>()

        // When
        val actual = viewRendererFactory.make(widget)

        // Then
        assertTrue(actual is NavigatorViewRenderer)
    }

    @Test
    fun make_should_return_a_FormViewRenderer_when_widget_is_a_layout_Form() {
        // Given
        val widget = mockk<Form>()

        // When
        val actual = viewRendererFactory.make(widget)

        assertTrue(actual is FormViewRenderer)
    }

    @Test
    fun make_should_throw_IllegalArgumentException_when_widget_is_not_a_layout_Widget() {
        // Given
        val widget = mockk<Widget>()

        // When
        val exception = assertFails("$widget is not a Layout Widget.") {
            viewRendererFactory.make(widget)
        }

        assertTrue(exception is IllegalArgumentException)
    }
}