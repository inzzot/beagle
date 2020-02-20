package br.com.zup.beagle.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.zup.beagle.action.Action
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.utils.CoroutineDispatchers
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class BeagleViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var component: ServerDrivenComponent
    @MockK
    private lateinit var action: Action
    @MockK
    private lateinit var beagleService: BeagleService

    @InjectMockKs
    private lateinit var beagleUIViewModel: BeagleViewModel

    private val viewStateResult: (t: ViewState) -> Unit = {
        viewModelStates.add(it)
    }

    private var viewModelStates: MutableList<ViewState> = mutableListOf()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        CoroutineDispatchers.Main = Dispatchers.Unconfined

        coEvery { beagleService.fetchComponent(any()) } returns component
        coEvery { beagleService.fetchAction(any()) } returns action

        viewModelStates.clear()

        beagleUIViewModel.state.observeForever(viewStateResult)
    }

    @After
    fun tearDown() {
        beagleUIViewModel.state.removeObserver(viewStateResult)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun fetch_should_return_render_ViewState() {
        // Given
        val url = RandomData.httpUrl()

        // When
        beagleUIViewModel.fetchComponent(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(
            component,
            (viewModelStates[1] as ViewState.Result<ServerDrivenComponent>).data
        )
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun fetch_should_return_a_error_ViewState() {
        // Given
        val url = RandomData.httpUrl()
        val exception = BeagleException("Error")
        coEvery { beagleService.fetchComponent(any()) } throws exception

        // When
        beagleUIViewModel.fetchComponent(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(ViewState.Error(exception), viewModelStates[1])
        assertLoading(viewModelStates[2], false)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun fetchAction_should_return_render_ViewState() {
        // Given
        val url = RandomData.httpUrl()

        // When
        beagleUIViewModel.fetchAction(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(action, (viewModelStates[1] as ViewState.Result<Action>).data)
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun fetchAction_should_return_a_error_ViewState() {
        // Given
        val url = RandomData.httpUrl()
        val exception = BeagleException("Error")
        coEvery { beagleService.fetchAction(any()) } throws exception

        // When
        beagleUIViewModel.fetchAction(url)

        // Then
        assertLoading(viewModelStates[0], true)
        assertEquals(ViewState.Error(exception), viewModelStates[1])
        assertLoading(viewModelStates[2], false)
    }

    @Test
    fun onCleared_should_call_cancel() {
        // Given
        val viewModelSpy = spyk(beagleUIViewModel)
        every { viewModelSpy.cancel() } just Runs

        // When
        viewModelSpy.onCleared()

        // Then
        verify(exactly = once()) { viewModelSpy.cancel() }
    }

    private fun assertLoading(viewState: ViewState, expected: Boolean) {
        assertTrue(viewState is ViewState.Loading)
        assertEquals(expected, viewState.value)
    }
}