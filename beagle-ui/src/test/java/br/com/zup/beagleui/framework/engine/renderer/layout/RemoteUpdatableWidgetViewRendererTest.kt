package br.com.zup.beagleui.framework.engine.renderer.layout

import android.content.Context
import android.view.View
import br.com.zup.beagleui.framework.engine.renderer.RootView
import br.com.zup.beagleui.framework.engine.renderer.ViewRendererFactory
import br.com.zup.beagleui.framework.extensions.once
import br.com.zup.beagleui.framework.testutil.RandomData
import br.com.zup.beagleui.framework.utils.toView
import br.com.zup.beagleui.framework.view.BeagleView
import br.com.zup.beagleui.framework.view.ViewFactory
import br.com.zup.beagleui.framework.widget.core.Widget
import br.com.zup.beagleui.framework.widget.layout.RemoteUpdatableWidget
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

private val URL = RandomData.httpUrl()

class RemoteUpdatableWidgetViewRendererTest {

    @MockK
    private lateinit var remoteUpdatableWidget: RemoteUpdatableWidget
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @RelaxedMockK
    private lateinit var beagleView: BeagleView
    @MockK
    private lateinit var initialStateView: View
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context

    @InjectMockKs
    private lateinit var renderer: RemoteUpdatableWidgetViewRenderer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic("br.com.zup.beagleui.framework.utils.WidgetExtensionsKt")

        val initialState = mockk<Widget>()

        every { viewFactory.makeBeagleView(any()) } returns beagleView
        every { rootView.getContext() } returns context
        every { remoteUpdatableWidget.initialState } returns initialState
        every { remoteUpdatableWidget.url } returns URL
        every { initialState.toView(rootView) } returns initialStateView
    }

    @Test
    fun build_should_call_make_a_BeagleView() {
        val actual = renderer.build(rootView)

        assertTrue(actual is BeagleView)
    }

    @Test
    fun build_should_add_initialState() {
        renderer.build(rootView)

        verify(exactly = once()) { beagleView.addView(initialStateView) }
    }
}