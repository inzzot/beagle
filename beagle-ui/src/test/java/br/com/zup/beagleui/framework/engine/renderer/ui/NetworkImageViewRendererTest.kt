package br.com.zup.beagleui.framework.engine.renderer.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import br.com.zup.beagleui.framework.engine.mapper.ViewMapper
import br.com.zup.beagleui.framework.engine.renderer.RootView
import br.com.zup.beagleui.framework.extensions.once
import br.com.zup.beagleui.framework.view.ViewFactory
import br.com.zup.beagleui.framework.widget.ui.NetworkImage
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val DEFAULT_URL = "http://teste.com/test.png"

class NetworkImageViewRendererTest {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var viewMapper: ViewMapper
    @MockK
    private lateinit var imageView: ImageView
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var requestManager: RequestManager
    @MockK
    private lateinit var requestBuilder: RequestBuilder<Drawable>
    @MockK
    private lateinit var rootView: RootView

    private val scaleType = ImageView.ScaleType.FIT_CENTER
    private val networkImage = NetworkImage(DEFAULT_URL)

    private lateinit var networkImageViewRenderer: NetworkImageViewRenderer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Glide::class)

        every { Glide.with(any<View>()) } returns requestManager
        every { requestManager.load(any<String>()) } returns requestBuilder
        every { requestBuilder.into(any()) } returns mockk()
        every { viewFactory.makeImageView(context) } returns imageView
        every { viewMapper.toScaleType(any()) } returns scaleType
        every { imageView.scaleType = any() } just Runs
        every { rootView.getContext() } returns context

        networkImageViewRenderer = NetworkImageViewRenderer(networkImage, viewFactory, viewMapper)
    }

    @Test
    fun build_should_return_a_ImageView_with_desired_scaleType() {
        // Given
        val scaleTypeSlot = slot<ImageView.ScaleType>()
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        val view = networkImageViewRenderer.build(rootView)

        // Then
        assertTrue(view is ImageView)
        assertEquals(scaleType, scaleTypeSlot.captured)
    }

    @Test
    fun build_should_set_url_to_Glide() {
        networkImageViewRenderer.build(rootView)

        verify(exactly = once()) { Glide.with(imageView) }
        verify(exactly = once()) { requestManager.load(DEFAULT_URL) }
        verify(exactly = once()) { requestBuilder.into(imageView) }
    }
}
