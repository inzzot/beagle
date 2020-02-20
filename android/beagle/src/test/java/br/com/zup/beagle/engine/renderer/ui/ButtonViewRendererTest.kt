package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.action.ActionExecutor
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.utils.setData
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.view.BeagleButtonView
import br.com.zup.beagle.widget.ui.Button
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

private const val DEFAULT_TEXT = "Hello"
private const val DEFAULT_STYLE = "DummyStyle"

class ButtonViewRendererTest {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var buttonView: BeagleButtonView
    @RelaxedMockK
    private lateinit var button: Button
    @MockK
    private lateinit var actionExecutor: ActionExecutor

    @InjectMockKs
    private lateinit var buttonViewRenderer: ButtonViewRenderer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        mockkStatic(TextViewCompat::class)
        mockkStatic("br.com.zup.beagle.utils.ViewExtensionsKt")

        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)
        every { button.style } returns DEFAULT_STYLE
        every { button.text } returns DEFAULT_TEXT
        every { button.action } returns null
        every { buttonView.setBackgroundResource(any()) } just Runs
        every { rootView.getContext() } returns context
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
    }

    @After
    fun after() {
        unmockkStatic(TextViewCompat::class)
        unmockkObject(BeagleEnvironment)
    }

    @Test
    fun build_should_return_a_button_instance_and_set_data() {
        // Given
        every { viewFactory.makeButton(context) } returns buttonView

        // When
        val view = buttonViewRenderer.build(rootView)

        // Then
        assertTrue(view is android.widget.Button)
        verify(exactly = 1) { buttonView.setData(button) }
    }
}